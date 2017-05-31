package eu.tankernn.gameEngine.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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

	private int id;
	private int modelId = -1;
	private int systemId = -1;
	private final Vector3f position;
	private final Vector3f rotation;
	private final Vector3f velocity;
	private final Vector3f scale;
	protected boolean dead = false;

	private Set<Behavior> behaviors;

	public EntityState(int id, int modelId, int systemId, Vector3f position, Vector3f rotation, Vector3f velocity,
			Vector3f scale, Behavior... behaviors) {
		this.id = id;
		this.modelId = modelId;
		this.systemId = systemId;
		this.position = new Vector3f(position);
		this.rotation = new Vector3f(rotation);
		this.velocity = new Vector3f(velocity);
		this.scale = new Vector3f(scale);

		this.behaviors = new HashSet<>(Arrays.asList(behaviors));
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

	public Set<Behavior> getBehaviors() {
		return behaviors;
	}

	public void setBehaviors(Set<Behavior> behaviors) {
		this.behaviors = behaviors;
	}

	public String toString() {
		return String.format("Entity with id %d, modelId %d and position %s. List of behaviors: %s", id, modelId, position.toString(), behaviors.toString());
	}

	public void resetId() {
		this.id = ID_GEN.getAndIncrement();
	}
}
