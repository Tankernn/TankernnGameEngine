package eu.tankernn.gameEngine.entities;

import static eu.tankernn.gameEngine.settings.Settings.FAR_PLANE;
import static eu.tankernn.gameEngine.settings.Settings.FOV;
import static eu.tankernn.gameEngine.settings.Settings.NEAR_PLANE;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.util.ICamera;
import eu.tankernn.gameEngine.util.Maths;

/**
 * Camera for determining the view frustum when rendering.
 * 
 * @author Frans
 */
public class Camera implements Positionable, ICamera {
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix = new Matrix4f();
	
	protected Vector3f position = new Vector3f(0, 10, 0);
	protected float pitch = 20;
	protected float yaw;
	protected float roll;
	
	public Camera(Vector3f position) {
		this();
		this.position = position;
	}
	
	public Camera() {
		this.projectionMatrix = createProjectionMatrix();
	}
	
	/**
	 * Points the camera so that that <code>entity</code> is in the center.
	 * 
	 * @param entity The object to point towards
	 */
	public void pointToEntity(Positionable entity) {
		Vector3f targetPos = entity.getPosition();
		Vector3f delta = new Vector3f();
		Vector3f.sub(position, targetPos, delta);
		
		this.yaw = (float) (Math.atan2(-delta.x, delta.z) * 180 / Math.PI);
		
		float distance = (float) Math.sqrt(Math.pow(delta.x, 2) + Math.pow(delta.z, 2));
		this.pitch = (float) (Math.atan2(delta.y, distance) * 180 / Math.PI);
		
	}
	
	public void update() {
		updateViewMatrix();
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}
	
	/**
	 * Inverts the pitch of the camera, used for water rendering.
	 */
	public void invertPitch() {
		this.pitch = -pitch;
	}
	
	/**
	 * Inverts the roll of the camera, used for water rendering.
	 */
	public void invertRoll() {
		this.roll = -roll;
	}
	
	private void updateViewMatrix() {
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0), viewMatrix,
				viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f negativeCameraPos = new Vector3f(-position.x,-position.y,-position.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
	}
	
	private static Matrix4f createProjectionMatrix(){
		Matrix4f projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
	
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}

	@Override
	public Matrix4f getViewMatrix() {
		return Maths.createViewMatrix(this);
	}
	
	@Override
	public void reflect(float height){
		this.pitch = -pitch;
		this.position.y = position.y - 2 * (position.y - height);
		updateViewMatrix();
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public Matrix4f getProjectionViewMatrix() {
		return Matrix4f.mul(projectionMatrix, viewMatrix, null);
	}
	
}
