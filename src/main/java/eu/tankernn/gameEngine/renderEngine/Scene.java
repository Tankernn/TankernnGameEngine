package eu.tankernn.gameEngine.renderEngine;

import java.util.List;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.skybox.Skybox;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class Scene {
	private List<Entity> entities;
	private List<Entity> normalEntities;
	private TerrainPack terrainPack;
	private List<Light> lights;
	private Camera camera;
	private Skybox sky;
	
	private Texture environmentMap;
	
	public Scene(List<Entity> entities, List<Entity> normalEntities, TerrainPack terrainPack, List<Light> lights, Camera camera, Skybox sky) {
		this.entities = entities;
		this.normalEntities = normalEntities;
		this.terrainPack = terrainPack;
		this.lights = lights;
		this.camera = camera;
		this.sky = sky;
		this.environmentMap = Texture.newEmptyCubeMap(128);
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
	
	public Skybox getSkybox() {
		return sky;
	}
	
	public Texture getEnvironmentMap() {
		return environmentMap;
	}
	
	public void delete() {
		environmentMap.delete();
	}
}
