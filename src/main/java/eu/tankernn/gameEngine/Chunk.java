package eu.tankernn.gameEngine;

import java.util.List;

import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.entities.Light;

public class Chunk {
	private final int seed;
	private List<Light> lights;
	private List<Entity3D> entities;
	
	public Chunk(int seed, List<Light> lights, List<Entity3D> entities) {
		this.seed = seed;
		this.lights = lights;
		this.entities = entities;
	}

	public int getSeed() {
		return seed;
	}

	public List<Light> getLights() {
		return lights;
	}

	public List<Entity3D> getEntities() {
		return entities;
	}
}
