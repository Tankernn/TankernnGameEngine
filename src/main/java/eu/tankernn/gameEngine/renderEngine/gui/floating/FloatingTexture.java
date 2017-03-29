package eu.tankernn.gameEngine.renderEngine.gui.floating;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.textures.Texture;

public class FloatingTexture {
	
	private Texture texture;
	protected Vector3f position;
	protected Vector2f scale;
	
	public FloatingTexture(Texture texture, Vector3f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	public Texture getTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getScale() {
		return new Vector3f(scale.x, scale.y, 1f);
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
	
}
