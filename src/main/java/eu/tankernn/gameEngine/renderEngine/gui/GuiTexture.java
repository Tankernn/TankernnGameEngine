package eu.tankernn.gameEngine.renderEngine.gui;

import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.loader.textures.Texture;

public class GuiTexture {
	
	private Texture texture;
	protected Vector2f position;
	protected float scale;
	
	public GuiTexture(Texture texture, Vector2f position, float scale) {
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

	public Vector2f getSize() {
		return (Vector2f) new Vector2f(texture.dimensions).scale(scale);
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
}
