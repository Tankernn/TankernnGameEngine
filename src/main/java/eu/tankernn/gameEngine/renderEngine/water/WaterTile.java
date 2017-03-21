package eu.tankernn.gameEngine.renderEngine.water;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.util.IPositionable;

public class WaterTile implements IPositionable {
	
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

	@Override
	public Vector3f getPosition() {
		return new Vector3f(x, height, z);
	}
	
}
