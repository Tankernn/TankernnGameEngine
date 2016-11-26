package eu.tankernn.gameEngine;

import java.util.List;

import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.entities.Light;

public class World {
	private final int seed;
	private List<Light> lights;
	private List<Entity> entities;
	
	public World(int seed, List<Light> lights, List<Entity> entities) {
		this.seed = seed;
		this.lights = lights;
		this.entities = entities;
	}
	
	
}
