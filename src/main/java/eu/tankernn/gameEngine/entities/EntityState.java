package eu.tankernn.gameEngine.entities;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.settings.Physics;

public class EntityState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3294516796577777079L;

	private static final AtomicInteger ID_GEN = new AtomicInteger();

	private final int id;
	private int modelId;
	private final Vector3f position;
	private final Vector3f rotation;
	private final Vector3f velocity;
	private final Vector3f scale;

	public EntityState(int id, int modelId, Vector3f position, Vector3f rotation, Vector3f velocity, Vector3f scale) {
		this.id = id;
		this.modelId = modelId;
		this.position = position;
		this.rotation = rotation;
		this.velocity = velocity;
		this.scale = scale;
	}

	public EntityState(int modelId, Vector3f position, Vector3f rotation, Vector3f velocity, Vector3f scale) {
		this(ID_GEN.getAndIncrement(), modelId, position, rotation, velocity, scale);
	}

	public EntityState() {
		this(-1, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
	}

	/**
	 * Moves this entity in the direction of its rotation.
	 * 
	 * @param speed
	 *            The speed with which to move the entity
	 */
	public void generateVelocity(float speed) {
		getVelocity().x = (float) (speed * Math.sin(Math.toRadians(getRotation().y)));
		getVelocity().z = (float) (speed * Math.cos(Math.toRadians(getRotation().y)));
		getVelocity().y += Physics.GRAVITY * DisplayManager.getFrameTimeSeconds();
	}

	public void increaseRotation(Vector3f deltaRotation) {
		Vector3f.add(getRotation(), deltaRotation, getRotation());
	}

	public void update() {
		Vector3f.add(position, (Vector3f) new Vector3f(velocity).scale(DisplayManager.getFrameTimeSeconds()), position);
	}

	public void update(BiFunction<Float, Float, Float> terrainHeightByPos) {
		this.update();

		float terrainHeight = terrainHeightByPos.apply(getPosition().x, getPosition().z);

		if (position.y < terrainHeight) {
			velocity.y = 0;
			position.y = terrainHeight;
		}
	}

	public int getId() {
		return id;
	}

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public Vector3f getScale() {
		return scale;
	}

}
