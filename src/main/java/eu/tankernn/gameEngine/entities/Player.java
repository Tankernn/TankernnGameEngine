package eu.tankernn.gameEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.settings.Physics;
import eu.tankernn.gameEngine.terrains.Terrain;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 20;
	protected static final float TURN_MAX = 160;
	private static final float JUMP_POWER = 30;
	
	private TerrainPack terrainPack;
	
	protected float currentSpeed = 0;
	protected float currentTurnSpeed = 0;
	protected float upwardsSpeed = 0;
	private boolean isInAir = false;
	
	private float height = 2.0f;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, TerrainPack terrainPack) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.terrainPack = terrainPack;
	}
	
	public void move(TerrainPack terrainPack) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += Physics.GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		Terrain currentTerrain = terrainPack.getTerrainByWorldPos(this.getPosition().x, this.getPosition().z);
		
		float terrainHeight = 0;
		if (currentTerrain != null) {
			terrainHeight = currentTerrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		}
		
		if (super.getPosition().getY() < terrainHeight) {
			upwardsSpeed = 0;
			this.isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void jump() {
		if (!this.isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			this.isInAir = true;
		}
	}
	
	protected void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || (Mouse.isButtonDown(0) && Mouse.isButtonDown(1))) {
			this.currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_MAX;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_MAX;
		} else {
			this.currentTurnSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = 5 * RUN_SPEED;
		}
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public Terrain getCurrentTerrain() {
		return terrainPack.getTerrainByWorldPos(this.getPosition().x, this.getPosition().z);
	}
	
}
