package eu.tankernn.gameEngine.loader.models;

import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.renderEngine.Vao;

public class TexturedModel {
	private final int id;
	private Vao rawModel;
	private ModelTexture texture;
	
	public TexturedModel(int id, Vao rawModel, ModelTexture texture) {
		this.id = id;
		this.rawModel = rawModel;
		this.texture = texture;
	}

	public Vao getModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}

	public int getId() {
		return id;
	}
}
