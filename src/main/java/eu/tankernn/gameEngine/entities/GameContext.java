package eu.tankernn.gameEngine.entities;

import java.util.Collection;

import eu.tankernn.gameEngine.loader.models.AABB;

public abstract class GameContext {
	private final boolean client;
	private final float tickLengthSeconds;
	private final Collection<EntityState> entities;

	public GameContext(boolean client, float tickLengthSeconds, Collection<EntityState> entities) {
		super();
		this.client = client;
		this.tickLengthSeconds = tickLengthSeconds;
		this.entities = entities;
	}

	public boolean isClient() {
		return client;
	}

	public float getTickLengthSeconds() {
		return tickLengthSeconds;
	}

	public Collection<EntityState> getEntities() {
		return entities;
	}

	public abstract float getHeight(int entityId);

	public abstract float getTerrainHeight(float x, float z);

	public abstract AABB getBoundingBox(int entityId);
}
