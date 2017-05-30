package eu.tankernn.gameEngine.entities.projectiles;

import java.util.function.BiFunction;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.EntityState;

public class ProjectileState extends EntityState {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2926349294733353572L;
	
	private final float range;
	private final Vector3f startPosition;

	public ProjectileState(int id, int modelId, int systemId, Vector3f position, Vector3f rotation, Vector3f velocity,
			Vector3f scale, float range) {
		super(id, modelId, systemId, position, rotation, velocity, scale);
		this.range = range;
		this.startPosition = new Vector3f(position);
	}
	
	public ProjectileState(int modelId, int systemId, Vector3f position, Vector3f rotation, Vector3f velocity,
			Vector3f scale, float range) {
		this(ID_GEN.getAndIncrement(), modelId, systemId, position, rotation, velocity, scale, range);
	}

	public void update(BiFunction<Float, Float, Float> terrain) {
		getPosition().y = Math.max(getPosition().y, 5 + terrain.apply(getPosition().x, getPosition().z));

		Vector3f distance = Vector3f.sub(getPosition(), startPosition, null);
		if (distance.length() > range) {
			setDead(true);
		}
	}

	public float getRange() {
		return range;
	}

	public Vector3f getStartPosition() {
		return startPosition;
	}

}
