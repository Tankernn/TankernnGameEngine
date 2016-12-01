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
			instance.render();
		}

		instance.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static void init(String name) {
		NativesExporter.exportNatives();
		DisplayManager.createDisplay(name);
	}
}
