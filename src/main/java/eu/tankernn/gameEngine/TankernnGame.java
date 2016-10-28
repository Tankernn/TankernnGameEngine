package eu.tankernn.gameEngine;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.renderEngine.Loader;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;

public class TankernnGame {
	protected Loader loader;
	protected MasterRenderer renderer;
	protected Camera camera;
	
	public TankernnGame(String[] dayTextureFiles, String[] nightTextureFiles) {
		loader = new Loader();
		camera = new Camera();
		renderer = new MasterRenderer(loader, camera, dayTextureFiles, nightTextureFiles);
	}
	
	public void update() {
		camera.update();
	}
	
	public void render() {
		DisplayManager.updateDisplay();
	}
	
	public void cleanUp() {
		loader.cleanUp();
		renderer.cleanUp();
	}
}
