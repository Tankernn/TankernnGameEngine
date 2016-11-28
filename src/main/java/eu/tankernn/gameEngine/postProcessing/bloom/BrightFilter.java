package eu.tankernn.gameEngine.postProcessing.bloom;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.postProcessing.PostProcessingEffect;
import eu.tankernn.gameEngine.postProcessing.ImageRenderer;

public class BrightFilter extends PostProcessingEffect<BrightFilterShader> {

	public BrightFilter(int width, int height) {
		super(new BrightFilterShader());
		renderer = new ImageRenderer(width, height);
	}

	@Override
	public void render(Texture colorTexture, Texture brightTexture) {
		shader.start();
		colorTexture.bindToUnit(0);
		renderer.renderQuad();
		shader.stop();

		outputColorTexture = colorTexture;
		outputBrightTexture = renderer.getOutputTexture();
	}

}
