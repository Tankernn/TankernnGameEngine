package eu.tankernn.gameEngine.animation;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

public class AnimationSection {
	private int bodyPart;
	private List<KeyFrame> keyFrames;
	
	public Matrix4f getRotationMatrix(float time) {
		for (KeyFrame keyFrame : keyFrames) {
			if (keyFrame.getTime() == time) {
				return keyFrame.getRotationMatrix();
			}
		}
		return new Matrix4f();
	}
}
