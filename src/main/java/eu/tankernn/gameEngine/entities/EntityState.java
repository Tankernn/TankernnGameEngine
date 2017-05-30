package eu.tankernn.gameEngine.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.ai.Behavior;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.settings.Physics;

public class EntityState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3294516796577777079L;

	protected static final AtomicInteger ID_GEN = new AtomicInteger();

	private final int id;
	private int modelId;
	private int systemId;
	private final Vector3f position;
	private final Vector3f rotation;
	private final Vector3f velocity;
	private final Vector3f scale;
	protected boolean dead = false;

	private List<Behavior> behaviors;

	public EntityState(int id, int modelId, int systemId, Vector3f position, Vector3f rotation, Vector3f velocity,
			Vector3f scale, Behavior... behaviors) {
		this.id = id;
		this.modelId = modelId;
		this.position = new Vector3f(position);
		this.rotation = rotation;
		this.velocity = velocity;
		this.scale = scale;

		this.behaviors = new ArrayList<Behavior>(Arrays.asList(behaviors));
		this.behaviors.forEach(b -> b.setEntity(this));
	}

	public EntityState(int modelId, int systemId, Vector3f position, Vector3f rotation, Vector3f velocity,
			Vector3f scale, Behavior... behaviors) {
		this(ID_GEN.getAndIncrement(), modelId, systemId, position, rotation, velocity, scale, behaviors);
	}

	public EntityState() {
		this(-1, -1, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
	}

	/**
	 * Moves this entity in the direction of its rotation.
	 * 
	 * @param speed
	 *            The speed with which to move the entity
	 */
	public void generateVelocity(float speed, float tickTimeSeconds) {
		getVelocity().x = (float) (speed * Math.sin(Math.toRadians(getRotation().y)));
		getVelocity().z = (float) (speed * Math.cos(Math.toRadians(getRotation().y)));
		getVelocity().y += Physics.GRAVITY * DisplayManager.getFrameTimeSeconds();
	}

	public void increaseRotation(Vector3f deltaRotation) {
		Vector3f.add(getRotation(), deltaRotation, getRotation());
	}

	public void update(GameContext ctx) {
		behaviors.forEach(b -> b.update(ctx));

		float terrainHeight = ctx.getTerrainHeight(position.x, position.z);

		if (position.y < terrainHeight) {
			velocity.y = 0;
			position.y = terrainHeight;
		}

		System.out.println(this);

		Vector3f.add(position, (Vector3f) new Vector3f(velocity).scale(ctx.getTickLengthSeconds()), position);
	}

	public void addBehavior(Behavior behavior) {
		behaviors.add(behavior);
		behavior.setEntity(this);
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

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public int getParticleSystemId() {
		return systemId;
	}

	public List<Behavior> getBehaviors() {
		return behaviors;
	}

	public void setBehaviors(List<Behavior> behaviors) {
		this.behaviors = behaviors;
	}

	public String toString() {
		return String.format("Entity with id %d, modelId %d and position %s. List of behaviors: %s", id, modelId, position.toString(), behaviors.toString());
	}
}
