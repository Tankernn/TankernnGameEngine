package eu.tankernn.gameEngine.entities.ai;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.GameContext;
import eu.tankernn.gameEngine.util.IPositionable;

public class FollowBehavior extends Behavior {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1890567568320815924L;
	
	private IPositionable target;
	private float speed;
	
	public FollowBehavior(IPositionable target, float speed) {
		this.target = target;
		this.speed = speed;
	}
	
	@Override
	public void update(GameContext impl) {
		// Store direction in velocity vector
		Vector3f.sub(target.getPosition(), entity.getPosition(), entity.getVelocity());
		// Normalize and scale velocity vector
		entity.getVelocity().normalise().scale(speed);
	}
}
