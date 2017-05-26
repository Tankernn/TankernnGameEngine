package eu.tankernn.gameEngine.entities.npc;

import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.renderEngine.DisplayManager;

public class RoamingBehavior extends Behavior {
	
	private Vector2f targetPosition;
	private RoamingArea area;
	private float standbyTime, speed;
	
	public RoamingBehavior(RoamingArea area, float speed) {
		this.area = area;
		this.speed = speed;
	}
	
	@Override
	public void update() {
		if (targetPosition == null) {
			entity.generateVelocity(0);
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
			entity.generateVelocity(speed);
		}
	}
	
}
