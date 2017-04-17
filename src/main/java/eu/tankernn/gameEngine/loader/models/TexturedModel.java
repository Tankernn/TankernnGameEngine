package eu.tankernn.gameEngine.loader.models;

import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.renderEngine.Vao;

public class TexturedModel {
	private Vao rawModel;
	private ModelTexture texture;
	
	public TexturedModel(Vao rawModel, ModelTexture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}

	public Vao getModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
