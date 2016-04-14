package eu.tankernn.gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Camera for determining the view frustum when rendering.
 * 
 * @author Frans
 */
public class Camera implements Positionable {
	
	protected Vector3f position = new Vector3f(0, 10, 0);
	protected float pitch = 20;
	protected float yaw;
	protected float roll;
	
	public Camera(Vector3f position) {
		this.position = position;
	}
	
	public Camera() {
		
	}
	
	/**
	 * Points the camera so that that <code>entity</code> is in the center.
	 * 
	 * @param entity The <code>Entity</code> to point towards
	 */
	public void pointToEntity(Entity entity) {
		Vector3f targetPos = entity.getPosition();
		Vector3f delta = new Vector3f();
		Vector3f.sub(position, targetPos, delta);
		
		this.yaw = (float) (Math.atan2(-delta.x, delta.z) * 180 / Math.PI);
		
		float distance = (float) Math.sqrt(Math.pow(delta.x, 2) + Math.pow(delta.z, 2));
		this.pitch = (float) (Math.atan2(delta.y, distance) * 180 / Math.PI);
		
	}
	
	public void update() {
		
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
	
}
