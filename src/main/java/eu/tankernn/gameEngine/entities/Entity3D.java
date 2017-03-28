package eu.tankernn.gameEngine.entities;

import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.animation.model.AnimatedModel;
import eu.tankernn.gameEngine.audio.Source;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.settings.Physics;
import eu.tankernn.gameEngine.terrains.TerrainPack;
import eu.tankernn.gameEngine.util.IPositionable;

public class Entity3D implements IPositionable {
	private static final AtomicInteger ID_GEN = new AtomicInteger();
	
	private TexturedModel model;
	protected Vector3f position, velocity = new Vector3f(0, 0, 0);
	private Vector3f rotation;
	private float scale;
	private AABB boundingBox;
	protected boolean dead;
	protected TerrainPack terrain;
	protected Source source = new Source();
	private final int id;
	
	public Entity3D(TexturedModel model, Vector3f position, AABB boundingBox, TerrainPack terrain) {
		this(model, position, new Vector3f(0, 0, 0), 1, boundingBox, terrain);
	}
	
	public Entity3D(TexturedModel model, Vector3f position, Vector3f rotation, float scale, AABB boundingBox, TerrainPack terrain) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.boundingBox = boundingBox;
		this.boundingBox.updatePosition(position);
		this.terrain = terrain;
		this.id = ID_GEN.incrementAndGet();
	}
	
	/**
	 * Moves this entity in the direction of its rotation.
	 * 
	 * @param speed The speed with which to move the entity
	 */
	public void generateVelocity(float speed) {
		velocity.x = (float) (speed * Math.sin(Math.toRadians(rotation.y)));
		velocity.z = (float) (speed * Math.cos(Math.toRadians(rotation.y)));
		velocity.y += Physics.GRAVITY * DisplayManager.getFrameTimeSeconds();
	}
	
	public void increaseRotation(Vector3f deltaRotation) {
		Vector3f.add(this.rotation, deltaRotation, this.rotation);
	}
	
	public void update() {
		Vector3f.add(position, (Vector3f) new Vector3f(velocity).scale(DisplayManager.getFrameTimeSeconds()), position);
		
		float terrainHeight = terrain.getTerrainHeightByWorldPos(position.x, position.z);
		
		if (position.y < terrainHeight) {
			velocity.y = 0;
			position.y = terrainHeight;
		}
		
		source.setPosition(this.position);
		source.setVelocity(this.velocity);
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
	
	public boolean isDead() {
		return dead;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Entity3D) {
			return this.id == ((Entity3D) obj).id;
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
}
