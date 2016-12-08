package eu.tankernn.gameEngine.renderEngine.gui;

import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.loader.textures.Texture;

public class GuiTexture {
	
	private Texture texture;
	protected Vector2f position;
	protected Vector2f scale;
	
	public GuiTexture(Texture texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	public Texture getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
	
}
