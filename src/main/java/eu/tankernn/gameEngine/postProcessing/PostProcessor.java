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
import eu.tankernn.gameEngine.renderEngine.Vao;

public class PostProcessor {
	
	private final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};
	private Vao quad;
	private List<PostProcessingEffect<?>> effects = new ArrayList<PostProcessingEffect<?>>();
	private CombineFilter combineFilter;
	public final int blurFactor;
	
	public PostProcessor(Loader loader, boolean underWater) {
		this.blurFactor = underWater ? 2 : 0;
		quad = loader.loadToVAO(POSITIONS, 2);
		if (!underWater)
			effects.add(new ContrastChanger());
		for (int i = 1; i < blurFactor + 1; i++) {
			effects.add(new HorizontalBlur(Display.getWidth() / i, Display.getHeight() / i));
			effects.add(new VerticalBlur(Display.getWidth() / i, Display.getHeight() / i));
		}
		effects.add(new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2));
		combineFilter = new CombineFilter();
	}
	
	public PostProcessor(Loader loader) {
		this(loader, false);
	}
	
	public void doPostProcessing(Texture colorTexture, Texture brightTexture) {
		start();
		for (PostProcessingEffect<?> effect: effects) {
			effect.render(colorTexture, brightTexture);
			colorTexture = effect.getOutputColorTexture();
			brightTexture = effect.getOutputBrightTexture();
		}
		combineFilter.render(colorTexture, brightTexture);
		end();
	}
	
	@Override
	public void finalize() {
		effects.forEach(p -> p.finalize());
		combineFilter.finalize();
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
