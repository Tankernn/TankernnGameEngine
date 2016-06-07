package eu.tankernn.gameEngine.postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import eu.tankernn.gameEngine.models.RawModel;
import eu.tankernn.gameEngine.postProcessing.gaussianBlur.HorizontalBlur;
import eu.tankernn.gameEngine.postProcessing.gaussianBlur.VerticalBlur;
import eu.tankernn.gameEngine.renderEngine.Loader;

public class PostProcessing {
	
	private static final int blurFactor = 0;
	
	private static final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};
	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static HorizontalBlur hBlur[] = new HorizontalBlur[blurFactor];
	private static VerticalBlur vBlur[] = new VerticalBlur[blurFactor];
	
	public static void init(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		for (int i = 0; i < blurFactor; i++) {
			int temp = i + 1;
			hBlur[i] = new HorizontalBlur(Display.getWidth() / temp, Display.getHeight() / temp);
			vBlur[i] = new VerticalBlur(Display.getWidth() / temp, Display.getHeight() / temp);
		}
	}
	
	public static void doPostProcessing(int colorTexture) {
		start();
		int currentTexture = colorTexture;
		for (int i = 0; i < blurFactor; i++) {
			hBlur[i].render(currentTexture);
			vBlur[i].render(hBlur[i].getOutputTexture());
			currentTexture = vBlur[i].getOutputTexture();
		}
		contrastChanger.render(currentTexture);
		end();
	}
	
	public static void cleanUp() {
		contrastChanger.cleanUp();
		for (int i = 0; i < blurFactor; i++) {
			hBlur[i].cleanUp();
			vBlur[i].cleanUp();
		}
	}
	
	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
}
