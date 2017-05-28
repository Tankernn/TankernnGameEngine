package eu.tankernn.gameEngine.multiplayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.entities.ILight;

public class WorldState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4777990467291918367L;
	
	private final int seed;
	private List<ILight> lights = new ArrayList<>();
	private List<EntityState> entities = new ArrayList<>();

	public WorldState(int seed) {
		this.seed = seed;
	}
	
	public WorldState(int seed, List<ILight> lights, List<EntityState> entities) {
		this(seed);
		this.lights = lights;
		this.entities = entities;
	}

	public int getSeed() {
		return seed;
	}

	public List<ILight> getLights() {
		return lights;
	}

	public List<EntityState> getEntities() {
		return entities;
	}

}
