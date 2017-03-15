package eu.tankernn.gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.animation.animatedModel.AnimatedModel;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.util.IPositionable;

public class Entity3D implements IPositionable {
	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	private AABB boundingBox;
	
	public Entity3D(TexturedModel model, Vector3f position, Vector3f rotation, float scale, AABB boundingBox) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.boundingBox = boundingBox;
		this.boundingBox.updatePosition(position);
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(Vector3f deltaRotation) {
		Vector3f.add(this.rotation, deltaRotation, this.rotation);
	}
	
	public void update() {
		this.boundingBox.updatePosition(this.position);
		if (model instanceof AnimatedModel)
			((AnimatedModel) model).update();
	}
	
	public TexturedModel getModel() {
		return model;
	}
	
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public AABB getBoundingBox() {
		return boundingBox;
	}
	
}
