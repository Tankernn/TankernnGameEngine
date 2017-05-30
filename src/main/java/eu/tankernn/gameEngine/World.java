package eu.tankernn.gameEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.entities.EntityFactory;
import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.entities.GameContext;
import eu.tankernn.gameEngine.entities.ILight;
import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.multiplayer.WorldState;
import eu.tankernn.gameEngine.particles.ParticleMaster;
import eu.tankernn.gameEngine.renderEngine.gui.floating.FloatingTexture;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class World {
	private Map<Integer, Entity3D> entities = new HashMap<>();
	private List<ILight> lights = new ArrayList<>();
	private List<FloatingTexture> floatTextures = new ArrayList<>();
	private TerrainPack terrainPack;

	private final EntityFactory entityFactory;

	public World(Loader loader, ParticleMaster particleMaster, TerrainPack terrainPack) {
		this.entityFactory = new EntityFactory(loader, particleMaster, this);
		this.terrainPack = terrainPack;
	}

	public void update(GameContext ctx) {
		entities.values().forEach(e -> e.update(ctx));
		entities.values().removeIf(Entity3D::isDead);
	}

	public Map<Integer, Entity3D> getEntities() {
		return entities;
	}

	public List<ILight> getLights() {
		return lights;
	}

	public List<FloatingTexture> getFloatTextures() {
		return floatTextures;
	}

	public TerrainPack getTerrainPack() {
		return terrainPack;
	}

	public void finalize() {
		getTerrainPack().finalize();
	}

	public void setTerrainPack(TerrainPack terrainPack) {
		this.terrainPack = terrainPack;
	}

	public void setState(WorldState state) {
		for (EntityState s : state.getEntities()) {
			updateEntityState(s);
		}
		this.lights = state.getLights();
	}

	public void updateEntityState(EntityState s) {
		if (entities.containsKey(s.getId()))
			entities.get(s.getId()).setState(s);
		else
			spawnEntity(s);
	}

	public Entity3D spawnEntity(EntityState state) {
		if (entities.containsKey(state.getId())) {
			throw new RuntimeException("Entity with id " + state.getId() + " has already been spawned.");
		} else {
			Entity3D e = entityFactory.getEntity(state);
			entities.put(state.getId(), e);
			return e;
		}
	}

	public float getTerrainHeigh(float x, float z) {
		return terrainPack.getTerrainHeightByWorldPos(x, z);
	}

}
