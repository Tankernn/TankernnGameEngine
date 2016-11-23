package eu.tankernn.gameEngine.postProcessing;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.postProcessing.bloom.BrightFilter;
import eu.tankernn.gameEngine.postProcessing.bloom.CombineFilter;
import eu.tankernn.gameEngine.postProcessing.gaussianBlur.HorizontalBlur;
import eu.tankernn.gameEngine.postProcessing.gaussianBlur.VerticalBlur;
import eu.tankernn.gameEngine.renderEngine.RawModel;

public class PostProcessing {

	private static final int blurFactor = 2;

	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static RawModel quad;
	private static List<IPostProcessingEffect> effects = new ArrayList<IPostProcessingEffect>();
	private static CombineFilter combineFilter;
	
	public static void init(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		effects.add(new ContrastChanger());
		for (int i = 0; i < blurFactor; i++) {
			int temp = i + 1;
			effects.add(new HorizontalBlur(Display.getWidth() / temp, Display.getHeight() / temp));
			effects.add(new VerticalBlur(Display.getWidth() / temp, Display.getHeight() / temp));
		}
		effects.add(new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2));
		combineFilter = new CombineFilter();
	}

	public static void doPostProcessing(Texture colorTexture, Texture brightTexture) {
		for (IPostProcessingEffect effect : effects) {
			effect.render(colorTexture, brightTexture);
			brightTexture = effect.getOutputTexture();
		}
		
		start();
		combineFilter.render(colorTexture, brightTexture);
		end();
	}

	public static void cleanUp() {
		effects.forEach(p -> p.cleanUp());
		combineFilter.cleanUp();
	}

	private static void start() {
		quad.bind(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		quad.unbind(0);
	}

}
