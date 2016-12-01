package eu.tankernn.gameEngine;

import java.util.List;

import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.entities.Light;

public class Chunk {
	private final int seed;
	private List<Light> lights;
	private List<Entity> entities;
	
	public Chunk(int seed, List<Light> lights, List<Entity> entities) {
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

	public List<Entity> getEntities() {
		return entities;
	}
}
