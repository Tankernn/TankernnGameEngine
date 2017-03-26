package eu.tankernn.gameEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.animation.model.AnimatedModel;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.terrains.Terrain;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class Player extends Entity3D {
	
	private static final float RUN_SPEED = 20;
	protected static final float TURN_MAX = 160;
	private static final float JUMP_POWER = 30;
	
	protected TerrainPack terrainPack;
	
	protected float currentSpeed = 0;
	protected float currentTurnSpeed = 0;
	
	private float height = 2.0f;
	
	public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale, AABB boundingBox, TerrainPack terrainPack) {
		super(model, position, rotation, scale, boundingBox, terrainPack);
		this.terrainPack = terrainPack;
	}
	
	public void move() {
		checkInputs();
		super.increaseRotation(new Vector3f(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0));
		super.increasePosition(currentSpeed);
	}
	
	private void jump() {
		if (!this.isInAir()) {
			this.velocity.y = JUMP_POWER;
		}
	}
	
	protected void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || (Mouse.isButtonDown(0) && Mouse.isButtonDown(1))) {
			if (this.getModel() instanceof AnimatedModel)
				((AnimatedModel) getModel()).doAnimation("run");
			this.currentSpeed = RUN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
			if (this.getModel() instanceof AnimatedModel)
				((AnimatedModel) getModel()).doAnimation("idle");
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
	
	public boolean isInAir() {
		return this.position.y > this.terrainPack.getTerrainHeightByWorldPos(this.position.x, this.position.z);
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
	
	public Vector3f get2dRotation() {
		float dx = (float) (Math.sin(Math.toRadians(super.getRotation().y)));
		float dz = (float) (Math.cos(Math.toRadians(super.getRotation().y)));
		return new Vector3f(dx, 0, dz);
	}
	
}
