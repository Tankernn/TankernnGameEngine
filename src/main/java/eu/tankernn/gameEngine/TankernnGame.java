package eu.tankernn.gameEngine;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.renderEngine.Loader;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.renderEngine.skybox.Skybox;
import eu.tankernn.gameEngine.renderEngine.water.WaterMaster;

public class TankernnGame {
	protected Loader loader;
	protected MasterRenderer renderer;
	protected WaterMaster waterMaster;
	protected Camera camera;
	
	public TankernnGame(Skybox skybox, String dudvMap, String normalMap) {
		loader = new Loader();
		camera = new Camera();
		renderer = new MasterRenderer(loader, camera, skybox);
		waterMaster = new WaterMaster(loader, dudvMap, normalMap, camera);
	}
	
	public void update() {
		camera.update();
	}
	
	public void render() {
		DisplayManager.updateDisplay();
	}
	
	public void cleanUp() {
		waterMaster.cleanUp();
		loader.cleanUp();
		renderer.cleanUp();
	}
}
