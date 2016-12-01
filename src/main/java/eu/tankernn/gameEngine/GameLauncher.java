package eu.tankernn.gameEngine;

import org.lwjgl.opengl.Display;

import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.util.NativesExporter;

public class GameLauncher {
	private static TankernnGame instance;

	public static void launch(TankernnGame game) {
		instance = game;
		init();

		while (!Display.isCloseRequested()) {
			instance.update();
			instance.render();
		}

		instance.cleanUp();
		DisplayManager.closeDisplay();
	}

	private static void init() {
		NativesExporter.exportNatives();
		DisplayManager.createDisplay(instance.getName());
	}
}
