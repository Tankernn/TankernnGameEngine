package eu.tankernn.gameEngine.renderEngine;

import java.util.List;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class Scene {
	private List<Entity> entities;
	private List<Entity> normalEntities;
	private TerrainPack terrainPack;
	private List<Light> lights;
	private Camera camera;
	
	public Scene(List<Entity> entities, List<Entity> normalEntities, TerrainPack terrainPack, List<Light> lights, Camera camera) {
		this.entities = entities;
		this.normalEntities = normalEntities;
		this.terrainPack = terrainPack;
		this.lights = lights;
		this.camera = camera;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public List<Entity> getNormalEntities() {
		return normalEntities;
	}

	public TerrainPack getTerrainPack() {
		return terrainPack;
	}

	public List<Light> getLights() {
		return lights;
	}

	public Camera getCamera() {
		return camera;
	}
}
