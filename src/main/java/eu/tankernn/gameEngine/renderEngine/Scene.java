package eu.tankernn.gameEngine.renderEngine;

import java.util.List;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.entities.ILight;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.skybox.Skybox;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class Scene {
	private List<Entity3D> entities;
	private TerrainPack terrainPack;
	private List<ILight> lights;
	private Camera camera;
	private Skybox sky;
	
	private Texture environmentMap;
	
	public Scene(List<Entity3D> entities, TerrainPack terrainPack, List<ILight> lights, Camera camera, Skybox sky) {
		this.entities = entities;
		this.terrainPack = terrainPack;
		this.lights = lights;
		this.camera = camera;
		this.sky = sky;
		this.environmentMap = Texture.newEmptyCubeMap(128);
	}

	public List<Entity3D> getEntities() {
		return entities;
	}

	public TerrainPack getTerrainPack() {
		return terrainPack;
	}

	public List<ILight> getLights() {
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
