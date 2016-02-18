package eu.tankernn.gameEngine.animation;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

public class KeyFrame {
	private float time;
	private Vector4f rotation;
	
	public KeyFrame(float time, Vector4f rotation) {
		this.time = time;
		this.rotation = rotation;
	}
	
	public float getTime() {
		return time;
	}
	
	public Vector4f getRotation() {
		return rotation;
	}
	
	public Matrix4f getRotationMatrix() {
		Matrix4f rotationMatrix = new Matrix4f();
		rotationMatrix.m00 = (float) (1 - 2 * Math.pow(rotation.y, 2) - 2 * Math.pow(rotation.z, 2));
		rotationMatrix.m01 = 2 * rotation.x * rotation.y + 2 * rotation.w * rotation.z;
		rotationMatrix.m02 = 2 * rotation.x * rotation.z - 2 * rotation.w * rotation.y;
		rotationMatrix.m03 = 0;
		
		rotationMatrix.m10 = 2 * rotation.x * rotation.y - 2 * rotation.w * rotation.z;
		rotationMatrix.m11 = (float) (1 - 2 * Math.pow(rotation.x, 2) - 2 * Math.pow(rotation.z, 2));
		rotationMatrix.m12 = 2 * rotation.y * rotation.z - 2 * rotation.w * rotation.x;
		rotationMatrix.m13 = 0;
		
		rotationMatrix.m20 = 2 * rotation.x * rotation.z + 2 * rotation.w * rotation.y;
		rotationMatrix.m21 = 2 * rotation.y * rotation.z + 2 * rotation.w * rotation.x;
		rotationMatrix.m22 = (float) (1 - 2 * Math.pow(rotation.x, 2) - 2 * Math.pow(rotation.y, 2));
		rotationMatrix.m23 = 0;
		
		rotationMatrix.m30 = 0;
		rotationMatrix.m31 = 0;
		rotationMatrix.m32 = 0;
		rotationMatrix.m33 = 1;
		return rotationMatrix;
	}
	
}
