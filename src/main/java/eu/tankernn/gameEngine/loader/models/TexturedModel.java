package eu.tankernn.gameEngine.loader.models;

import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.renderEngine.Vao;

public class TexturedModel {
	private Vao rawModel;
	private ModelTexture texture;
	
	private int textureIndex = 0;
	
	public TexturedModel(Vao rawModel, ModelTexture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}
	
	public TexturedModel(Vao rawModel, ModelTexture texture, int textureIndex) {
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

	public Vao getModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
