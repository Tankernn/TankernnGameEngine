package eu.tankernn.gameEngine.postProcessing;

import eu.tankernn.gameEngine.loader.textures.Texture;

public class ContrastChanger implements IPostProcessingEffect {
	private ImageRenderer renderer;
	private ContrastShader shader;
	
	public ContrastChanger() {
		shader = new ContrastShader();
		renderer = new ImageRenderer();
	}
	
	public void render(Texture colorTexture, Texture brightTexture) {
		shader.start();
		brightTexture.bindToUnit(0);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}

	@Override
	public Texture getOutputTexture() {
		return renderer.getOutputTexture();
	}
}
