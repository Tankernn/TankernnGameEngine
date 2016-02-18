package eu.tankernn.gameEngine.entities;

import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.terrains.TerrainPack;

public class Camera {
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private float lockedPosition = 0;
	private boolean isLocked = false;
	
	private Vector3f position = new Vector3f(0, 10, 0);
	private float pitch = 20;
	private float yaw;
	private float roll;
	
	private Player player;
	private TerrainPack terrainPack;
	
	public Camera(Player player, TerrainPack terrainPack) {
		this.player = player;
		this.terrainPack = terrainPack;
	}
	
	public void move() {
		calculateZoom();
		if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
			if (!this.isLocked) {
				this.isLocked = true;
				this.lockedPosition = player.getRotY();
			}
			try {
				Mouse.setNativeCursor(new Cursor(1, 1, 0, 0, 1, IntBuffer.allocate(1), null)); //Hide the cursor
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			
			calculatePitch();
			calculateAngleAroundPlayer();
			
			if (Mouse.isButtonDown(1)) {
				this.lockedPosition = 0;
				float targetRot = this.angleAroundPlayer;
				float delta = targetRot - player.getRotY();
				player.increaseRotation(0, delta, 0);
			}
		} else {
			if (this.isLocked) {
				this.isLocked = false;
				this.angleAroundPlayer -= (player.getRotY() - lockedPosition);
			}
			try {
				Mouse.setNativeCursor(null);
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
		
		if (!this.isLocked) {
			adjustToCenter();
		}
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		
		if (this.isLocked) {
			this.yaw = 180 - (lockedPosition + angleAroundPlayer);
		} else
			this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	private void adjustToCenter() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.angleAroundPlayer = this.angleAroundPlayer % 360;
			
			if (this.angleAroundPlayer < 0)
				this.angleAroundPlayer = 360 + this.angleAroundPlayer;
			
			if (this.angleAroundPlayer -10 < 0) {
				this.angleAroundPlayer = 0;
				return;
			}
			
			if (this.angleAroundPlayer < 180)
				this.angleAroundPlayer -= 10;
			else
				this.angleAroundPlayer += 10;
		}
	}

	public void pointToEntity(Entity entity) {
		Vector3f targetPos = entity.getPosition();
		Vector3f delta = new Vector3f();
		Vector3f.sub(position, targetPos, delta);
		
		this.yaw = (float) (Math.atan2(-delta.x, delta.z) * 180 / Math.PI);
		
		float distance = (float) Math.sqrt(Math.pow(delta.x, 2) + Math.pow(delta.z, 2));
		this.pitch = (float) (Math.atan2(delta.y, distance) * 180 / Math.PI);
		
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		float theta;
		if (this.isLocked)
			theta = lockedPosition + angleAroundPlayer;
		else
			theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + player.getHeight() + verticDistance;
		position.y = Math.max(position.y, terrainPack.getTerrainHeightByWorldPos(position.x, position.z) + 1);
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		distanceFromPlayer = Math.max(distanceFromPlayer, 10);
		distanceFromPlayer = Math.min(distanceFromPlayer, 100);
	}
	
	private void calculatePitch() {
		float pitchChange = Mouse.getDY() * 0.1f;
		pitch -= pitchChange;
		pitch = Math.min(pitch, 90);
	}
	
	private void calculateAngleAroundPlayer() {
		float angleChange = Mouse.getDX() * 0.3f;
		angleAroundPlayer -= angleChange;
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

	public void invertPitch() {
		this.pitch = -pitch;
	}
	
	public void invertRoll() {
		this.roll = -roll;
	}
	
}
