package eu.tankernn.gameEngine.entities.ai;

import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.entities.GameContext;
import eu.tankernn.gameEngine.loader.models.AABB;

public abstract class CollisionBehavior extends Behavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7412981353484426775L;

	@Override
	public void update(GameContext impl) {
		impl.getEntities().stream().filter(e -> !e.equals(entity))
				.filter((e) -> AABB.collides(impl.getBoundingBox(e.getId()), impl.getBoundingBox(entity.getId())))
				.forEach(this::onCollision);
	}

	public abstract void onCollision(EntityState other);

}
