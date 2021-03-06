package eu.tankernn.gameEngine;

import org.lwjgl.opengl.Display;

import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.util.NativesExporter;

public class GameLauncher {
	private static TankernnGame instance;

	public static void launch(TankernnGame game) {
		instance = game;

		while (!Display.isCloseRequested()) {
			instance.update();
			instance.fullRender();
		}

		instance.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static void init(String name, int width, int height) {
		NativesExporter.exportNatives();
		DisplayManager.createDisplay(name, width, height);
	}
	
	public static void init(String name) {
		init(name, 800, 600);
	}
	
}
