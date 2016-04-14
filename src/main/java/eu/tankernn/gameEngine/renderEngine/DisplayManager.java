package eu.tankernn.gameEngine.renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

import eu.tankernn.gameEngine.settings.Settings;

/**
 * Handles the OpenGL display.
 * 
 * @author Frans
 */
public class DisplayManager {
	
	private static final int WIDTH = 1600;
	private static final int HEIGHT = 900;
	private static final int FPS_CAP = 60;
	private static final int MULTISAMPLING = 8;
	
	private static long lastFrameTime;
	private static float delta;
	
	/**
	 * Creates a new display.
	 */
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 3)
				.withForwardCompatible(true)
				.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat().withSamples(MULTISAMPLING).withDepthBits(24), attribs);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
		
		Display.setTitle(Settings.GAME_NAME);
	}
	
	/**
	 * Updates the display. Should be called every frame.
	 */
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	/**
	 * Get the current tick length in seconds. Used to synchronize
	 * time-dependent actions.
	 * 
	 * @return Current tick length in seconds
	 */
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	/**
	 * Close the display. Call when game stops.
	 */
	public static void closeDisplay() {
		Display.destroy();
	}
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
}
