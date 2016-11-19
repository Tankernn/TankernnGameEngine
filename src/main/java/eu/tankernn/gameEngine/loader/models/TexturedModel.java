package eu.tankernn.gameEngine.loader.models;

import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.renderEngine.RawModel;

public class TexturedModel {
	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		super();
		this.rawModel = rawModel;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getModelTexture() {
		return texture;
	}
	
	
}
