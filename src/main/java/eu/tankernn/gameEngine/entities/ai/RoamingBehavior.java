package eu.tankernn.gameEngine.entities.ai;

import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.entities.GameContext;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;

public class RoamingBehavior extends Behavior {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6198784236364774890L;
	
	private Vector2f targetPosition;
	private RoamingArea area;
	private float standbyTime, speed;
	
	public RoamingBehavior(RoamingArea area, float speed) {
		this.area = area;
		this.speed = speed;
	}
	
	@Override
	public void update(GameContext impl) {
		if (targetPosition == null) {
			entity.generateVelocity(0, impl.getTickLengthSeconds());
			if (standbyTime > 0f)
				standbyTime -= DisplayManager.getFrameTimeSeconds();
			else
				targetPosition = area.getPointInside();
		} else {
			Vector2f direction = (Vector2f) Vector2f.sub(targetPosition, new Vector2f(entity.getPosition().x, entity.getPosition().z), null);
			if (direction.length() < 1f) { // Reached target
				targetPosition = null;
				standbyTime = (float) (Math.random() * 10);
			}
			entity.getRotation().y = (float) Math.toDegrees(Math.atan2(direction.x, direction.y));
			entity.generateVelocity(speed, impl.getTickLengthSeconds());
		}
	}
	
}
