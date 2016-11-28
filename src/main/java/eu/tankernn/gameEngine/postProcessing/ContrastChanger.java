package eu.tankernn.gameEngine.postProcessing;

import eu.tankernn.gameEngine.loader.textures.Texture;

public class ContrastChanger extends PostProcessingEffect<ContrastShader> {
	public ContrastChanger() {
		super(new ContrastShader());
		renderer = new ImageRenderer();
	}
	
	public void render(Texture colorTexture, Texture brightTexture) {
		shader.start();
		brightTexture.bindToUnit(0);
		renderer.renderQuad();
		shader.stop();
		
		outputBrightTexture = renderer.getOutputTexture();
		outputColorTexture = colorTexture;
	}
}
