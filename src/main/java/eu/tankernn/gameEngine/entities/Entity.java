package eu.tankernn.gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.util.IPositionable;

public class Entity implements IPositionable {
	private static final Vector3f SIZE = new Vector3f(2, 4, 2);
	
	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	private AABB boundingBox;
	
	private int textureIndex = 0;
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.boundingBox = new AABB(position, SIZE);
	}
	
	public Entity(TexturedModel model, int index, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.textureIndex = index;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.boundingBox = new AABB(position, SIZE);
	}
	
	public float getTextureXOffset() {
		int column = textureIndex % model.getModelTexture().getNumberOfRows();
		return (float) column / (float) model.getModelTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex / model.getModelTexture().getNumberOfRows();
		return (float) row / (float) model.getModelTexture().getNumberOfRows();
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
		updateBoundingBox();
	}
	
	public void increaseRotation(Vector3f deltaRotation) {
		Vector3f.add(this.rotation, deltaRotation, this.rotation);
	}
	
	private void updateBoundingBox() {
		this.boundingBox = new AABB(this.position, SIZE); //TODO Fix model size
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
