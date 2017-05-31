package eu.tankernn.gameEngine.entities.ai;

import eu.tankernn.gameEngine.entities.EntityState;

public class DieOnCollisionBehavior extends CollisionBehavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8921319921640580619L;

	@Override
	public void onCollision(EntityState entity) {
		this.entity.setDead(true);
	}

}
