package eu.tankernn.gameEngine.renderEngine.water;

public class WaterTile {
	
	private float height;
	private float x, z;
	private float size;
	
	public WaterTile(float centerX, float centerZ, float height, float size) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.size = size;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getX() {
		return x;
	}
	
	public float getZ() {
		return z;
	}
	
	public float getSize() {
		return size;
	}
	
}
