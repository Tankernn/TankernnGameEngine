package eu.tankernn.gameEngine.postProcessing;

import eu.tankernn.gameEngine.loader.textures.Texture;

public interface IPostProcessingEffect {
	public void render(Texture colorTexture, Texture brightTexture);
	public void cleanUp();
	public Texture getOutputTexture();
}
