package eu.tankernn.gameEngine.settings;

public class Settings {
	//Display settings
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;
	
	//Sky color
	public static final float RED = 0.7f;
	public static final float GREEN = 0.75f;
	public static final float BLUE = 0.8f;
	
	public static final float TERRAIN_SIZE = 800;
	
	public static final float ANISOTROPIC_FILTERING_AMOUNT = 4f;
	public static final int MULTISAMPLING = 2;
	
	/**
	 * Maximum number of joints in a skeleton.
	 */
	public static final int MAX_JOINTS = 50;
}
