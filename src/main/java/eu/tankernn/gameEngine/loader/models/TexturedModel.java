package eu.tankernn.gameEngine.loader.models;

import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.renderEngine.RawModel;

public class TexturedModel {
	private RawModel rawModel;
	private ModelTexture texture;
	
	private int textureIndex = 0;
	
	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}
	
	public TexturedModel(RawModel rawModel, ModelTexture texture, int textureIndex) {
		this(rawModel, texture);
		this.textureIndex = textureIndex;
	}
	
	public float getTextureXOffset() {
		int column = textureIndex % texture.getNumberOfRows();
		return (float) column / (float) texture.getNumberOfRows();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex / texture.getNumberOfRows();
		return (float) row / (float) texture.getNumberOfRows();
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getModelTexture() {
		return texture;
	}
	
	
}
