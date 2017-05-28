package eu.tankernn.gameEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.entities.ILight;
import eu.tankernn.gameEngine.entities.projectiles.Projectile;
import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.multiplayer.WorldState;
import eu.tankernn.gameEngine.renderEngine.gui.floating.FloatingTexture;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class World {
	private Map<Integer, Entity3D> entities = new HashMap<>();
	private List<Projectile> projectiles = new ArrayList<>();
	private List<ILight> lights = new ArrayList<>();
	private List<FloatingTexture> floatTextures = new ArrayList<>();
	private TerrainPack terrainPack;

	private final Loader loader;

	public World(Loader loader) {
		this.loader = loader;
	}

	public void update() {
		entities.values().forEach(Entity3D::update);
		entities.values().removeIf(Entity3D::isDead);

		projectiles.forEach(Projectile::update);
		projectiles.removeIf(Projectile::isDead);
		projectiles.forEach((p) -> p.checkCollision(entities.values()));
	}

	public Map<Integer, Entity3D> getEntities() {
		return entities;
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
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
			entities.put(s.getId(), new Entity3D(s, loader, terrainPack));
	}

}
