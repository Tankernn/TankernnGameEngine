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

public class PostProcessor {

	private final int blurFactor = 0;

	private final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private RawModel quad;
	private List<PostProcessingEffect<?>> effects = new ArrayList<PostProcessingEffect<?>>();
	private CombineFilter combineFilter;
	
	public PostProcessor(Loader loader) {
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

	public void doPostProcessing(Texture colorTexture, Texture brightTexture) {
		start();
		for (PostProcessingEffect<?> effect : effects) {
			effect.render(colorTexture, brightTexture);
			colorTexture = effect.getOutputColorTexture();
			brightTexture = effect.getOutputBrightTexture();
		}
		combineFilter.render(colorTexture, brightTexture);
		end();
	}

	public void cleanUp() {
		effects.forEach(p -> p.cleanUp());
		combineFilter.cleanUp();
	}

	private void start() {
		quad.bind(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		quad.unbind(0);
	}

}
