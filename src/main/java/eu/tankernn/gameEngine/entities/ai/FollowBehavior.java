package eu.tankernn.gameEngine.entities.ai;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.entities.GameContext;

public class FollowBehavior extends Behavior {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1890567568320815924L;
	
	private Vector3f target;
	private float speed;
	private int targetId = -1;
	
	public FollowBehavior(Vector3f target, float speed) {
		this.target = target;
		this.speed = speed;
	}
	
	public FollowBehavior(int targetEntity, float speed) {
		this.targetId = targetEntity;
		this.speed = speed;
	}
	
	public FollowBehavior(EntityState targetEntity, float speed) {
		this(targetEntity.getId(), speed);
	}
	
	@Override
	public void update(GameContext impl) {
		if (targetId != -1)
			this.target = impl.getEntity(targetId).getPosition();
		// Store direction in velocity vector
		Vector3f.sub(target, entity.getPosition(), entity.getVelocity());
		if (entity.getVelocity().length() == 0)
			return;
		// Normalize and scale velocity vector
		entity.getVelocity().normalise().scale(speed);
	}
}
