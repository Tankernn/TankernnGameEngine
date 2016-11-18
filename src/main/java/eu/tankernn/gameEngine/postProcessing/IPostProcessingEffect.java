package eu.tankernn.gameEngine.postProcessing;

import eu.tankernn.gameEngine.loader.textures.Texture;

public interface IPostProcessingEffect {
	public void render(Texture texture);
	public void cleanUp();
}
