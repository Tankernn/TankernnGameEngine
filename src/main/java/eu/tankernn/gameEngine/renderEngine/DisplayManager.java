package eu.tankernn.gameEngine.renderEngine;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

/**
 * Handles the OpenGL display.
 * 
 * @author Frans
 */
public class DisplayManager {
	
	private static final int WIDTH = 1600;
	private static final int HEIGHT = 900;
	private static final int FPS_CAP = 60;
	public static final int MULTISAMPLING = 8;
	
	private static long lastFrameTime;
	private static float delta;
	private static boolean fullscreen = false;
	
	/**
	 * Creates a new display.
	 */
	public static void createDisplay(String title) {
		ContextAttribs attribs = new ContextAttribs(3, 3)
				.withForwardCompatible(true)
				.withProfileCore(true);
		
		try {
			setDisplayMode(WIDTH, HEIGHT, fullscreen);
			Display.setResizable(true);
			Display.create(new PixelFormat().withDepthBits(24), attribs);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
		
		Display.setTitle(title);
	}
	
	/**
	 * Set the display mode to be used
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public static void setDisplayMode(int width, int height, boolean fullscreen) {
		
		// return if requested DisplayMode is already set
		if ((Display.getDisplayMode().getWidth() == width) &&
				(Display.getDisplayMode().getHeight() == height) &&
				(Display.isFullscreen() == fullscreen)) {
			return;
		}
		
		try {
			DisplayMode targetDisplayMode = null;
			
			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;
				
				for (int i = 0; i < modes.length; i++) {
					DisplayMode current = modes[i];
					
					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}
						
						// if we've found a match for bpp and frequence against the 
						// original display mode then it's probably best to go for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
								(current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}
			
			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}
			
			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);
			Display.setResizable(!fullscreen);
			
		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}
	}
	
	/**
	 * Updates the display. Should be called every frame.
	 */
	public static void updateDisplay() {
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			fullscreen = !fullscreen;
			
			if (fullscreen) {
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int width = (int) screenSize.getWidth();
				int height = (int) screenSize.getHeight();
				setDisplayMode(width, height, fullscreen);
			} else {
				setDisplayMode(WIDTH, HEIGHT, fullscreen);
			}
		}
		
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
