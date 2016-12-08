package eu.tankernn.gameEngine;

import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;

public class TankernnGame {
	protected final String name;
	protected Loader loader;
	
	public TankernnGame(String name) {
		this.name = name;
		this.loader = new Loader();
	}
	
	public void update() {
	}
	
	public void render() {
		DisplayManager.updateDisplay();
	}
	
	public void cleanUp() {
		loader.cleanUp();
	}
	
	public String getName() {
		return name;
	}
}
