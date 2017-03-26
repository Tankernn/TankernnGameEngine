package eu.tankernn.gameEngine.postProcessing;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;

public abstract class PostProcessingEffect<S extends ShaderProgram> {
	protected ImageRenderer renderer;
	protected S shader;
	protected Texture outputColorTexture, outputBrightTexture;
	
	public PostProcessingEffect(S shader) {
		this.shader = shader;
	}
	
	public abstract void render(Texture colorTexture, Texture brightTexture);
	
	@Override
	public void finalize() {
		renderer.finalize();
		shader.finalize();
	}
	
	public Texture getOutputColorTexture() {
		return outputColorTexture;
	}
	
	public Texture getOutputBrightTexture() {
		return outputBrightTexture;
	}
}
