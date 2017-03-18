package eu.tankernn.gameEngine.util;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Maths {
	public static float distanceBetweenPoints(Vector3f pos1, Vector3f pos2) {
		float baseWidth = pos1.x - pos2.x;
		float baseDepth = pos1.z - pos2.z;
		
		float baseDiagonal = (float) Math.sqrt(Math.pow(baseWidth, 2) + Math.pow(baseDepth, 2));
		
		float cubeHeight = pos1.y - pos2.y;
		float cubeDiagonal = (float) Math.sqrt(Math.pow(baseDiagonal, 2) + Math.pow(cubeHeight, 2));
		return cubeDiagonal;
	}
	
	public static Vector4f slerp(Vector4f qa, Vector4f qb, double time) {
		// quaternion to return
		Vector4f qm = new Vector4f();
		// Calculate angle between them.
		double cosHalfTheta = qa.w * qb.w + qa.x * qb.x + qa.y * qb.y + qa.z * qb.z;
		// if qa=qb or qa=-qb then theta = 0 and we can return qa
		if (Math.abs(cosHalfTheta) >= 1.0){
			qm.w = qa.w;qm.x = qa.x;qm.y = qa.y;qm.z = qa.z;
			return qm;
		}
		// Calculate temporary values.
		double halfTheta = Math.acos(cosHalfTheta);
		double sinHalfTheta = Math.sqrt(1.0 - cosHalfTheta*cosHalfTheta);
		// if theta = 180 degrees then result is not fully defined
		// we could rotate around any axis normal to qa or qb
		if (Math.abs(sinHalfTheta) < 0.001){ // fabs is floating point absolute
			qm.w = (float) (qa.w * 0.5 + qb.w * 0.5);
			qm.x = (float) (qa.x * 0.5 + qb.x * 0.5);
			qm.y = (float) (qa.y * 0.5 + qb.y * 0.5);
			qm.z = (float) (qa.z * 0.5 + qb.z * 0.5);
			return qm;
		}
		double ratioA = Math.sin((1 - time) * halfTheta) / sinHalfTheta;
		double ratioB = Math.sin(time * halfTheta) / sinHalfTheta; 
		//calculate Quaternion.
		qm.w = (float) (qa.w * ratioA + qb.w * ratioB);
		qm.x = (float) (qa.x * ratioA + qb.x * ratioB);
		qm.y = (float) (qa.y * ratioA + qb.y * ratioB);
		qm.z = (float) (qa.z * ratioA + qb.z * ratioB);
		return qm;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
}
