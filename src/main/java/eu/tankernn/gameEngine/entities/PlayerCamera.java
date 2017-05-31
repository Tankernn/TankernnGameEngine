package eu.tankernn.gameEngine.entities;

import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.terrains.TerrainPack;

public class PlayerCamera extends Camera {
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private float lockedPosition = 0;
	private boolean isLocked = false;
	
	private Entity3D player;
	private TerrainPack terrainPack;
	
	public PlayerCamera(Entity3D player, TerrainPack terrainPack) {
		super();
		this.player = player;
		this.terrainPack = terrainPack;
	}
	
	/**
	 * Handles player input regarding camera movement.
	 */
	@Override
	public void update() {
		if (player == null)
			return;
		Vector3f rot = player.getRotation();
		calculateZoom();
		if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
			if (!this.isLocked) { // Happens first frame mouse held down
				this.isLocked = true;
				this.lockedPosition = rot.y;
			}
			hideCursor();
			
			calculatePitch();
			calculateAngleAroundPlayer();
			
			if (Mouse.isButtonDown(1)) { // Right click
				float targetRot = this.angleAroundPlayer + this.lockedPosition;
				float delta = targetRot - rot.y;
				player.getState().increaseRotation(new Vector3f(0, delta, 0));
			}
		} else {
			if (this.isLocked) {
				this.isLocked = false;
				this.angleAroundPlayer -= (rot.y - lockedPosition);
			}
			showCursor();
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
			this.yaw = 180 - (rot.y + angleAroundPlayer);
		
		super.update();
	}
	
	private void adjustToCenter() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.angleAroundPlayer = this.angleAroundPlayer % 360;
			
			if (this.angleAroundPlayer < 0)
				this.angleAroundPlayer = 360 + this.angleAroundPlayer;
			
			if (this.angleAroundPlayer - 10 < 0) {
				this.angleAroundPlayer = 0;
				return;
			}
			
			if (this.angleAroundPlayer < 180)
				this.angleAroundPlayer -= 10;
			else
				this.angleAroundPlayer += 10;
		}
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
			theta = player.getRotation().y + angleAroundPlayer;
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
		distanceFromPlayer = Math.min(distanceFromPlayer, 500);
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
	
	private void hideCursor() {
		try {
			Mouse.setNativeCursor(new Cursor(1, 1, 0, 0, 1, IntBuffer.allocate(1), null)); //Hide the cursor
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	private void showCursor() {
		try {
			Mouse.setNativeCursor(null);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public void setPlayer(Entity3D player) {
		this.player = player;
	}
}
