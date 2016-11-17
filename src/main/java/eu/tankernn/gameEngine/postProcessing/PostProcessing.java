package eu.tankernn.gameEngine.postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import eu.tankernn.gameEngine.loader.models.RawModel;
import eu.tankernn.gameEngine.postProcessing.bloom.BrightFilter;
import eu.tankernn.gameEngine.postProcessing.bloom.CombineFilter;
import eu.tankernn.gameEngine.postProcessing.gaussianBlur.HorizontalBlur;
import eu.tankernn.gameEngine.postProcessing.gaussianBlur.VerticalBlur;
import eu.tankernn.gameEngine.renderEngine.Loader;

public class PostProcessing {
	
	private static final int blurFactor = 2;
	
	private static final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};
	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static HorizontalBlur hBlur[] = new HorizontalBlur[blurFactor];
	private static VerticalBlur vBlur[] = new VerticalBlur[blurFactor];
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;
	
	public static void init(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		for (int i = 0; i < blurFactor; i++) {
			int temp = i + 1;
			hBlur[i] = new HorizontalBlur(Display.getWidth() / temp, Display.getHeight() / temp);
			vBlur[i] = new VerticalBlur(Display.getWidth() / temp, Display.getHeight() / temp);
		}
		brightFilter = new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2);
		combineFilter = new CombineFilter();
	}
	
	public static void doPostProcessing(int colorTexture, int brightTexture) {
		start();
		//brightFilter.render(colorTexture);
		int bloomTexture = brightTexture;
		for (int i = 0; i < blurFactor; i++) {
			hBlur[i].render(bloomTexture);
			vBlur[i].render(hBlur[i].getOutputTexture());
			bloomTexture = vBlur[i].getOutputTexture();
		}
		combineFilter.render(colorTexture, bloomTexture);
		//contrastChanger.render(bloomTexture);
		end();
	}
	
	public static void cleanUp() {
		contrastChanger.cleanUp();
		for (int i = 0; i < blurFactor; i++) {
			hBlur[i].cleanUp();
			vBlur[i].cleanUp();
		}
		brightFilter.cleanUp();
		combineFilter.cleanUp();
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
