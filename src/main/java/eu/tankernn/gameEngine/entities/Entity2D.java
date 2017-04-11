package eu.tankernn.gameEngine.entities;

import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.gui.GuiTexture;

public class Entity2D extends GuiTexture {
	protected Vector2f velocity = new Vector2f(0, 0);
	protected boolean alive = true;
	
	public Entity2D(Texture texture, Vector2f position, float scale) {
		super(texture, position, scale);
	}
	
	public void update() {
		this.position = Vector2f.add(position, velocity, null);
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}

	public boolean collides(Entity2D b) {
		if (Math.abs(position.x - b.getPosition().x) < getSize().x + b.getSize().x) {
			if (Math.abs(position.y - b.getPosition().y) < getSize().y + b.getSize().y) {
				return true;
			}
		}
		return false;
	}

	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getSize() {
		return super.getSize();
	}
	
	public boolean isAlive() {
		return alive;
	}
}
