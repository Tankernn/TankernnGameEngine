package eu.tankernn.gameEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class Car extends Player {

	private static final float MAX_SPEED = 100.0f, ACCELERATION = 20.0f, DECELERATION = 10.0f, BRAKE = 40.0f,
			TURN_FORCE = 160.0f;

	public Car(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale, AABB boundingBox, TerrainPack terrainPack) {
		super(model, position, rotation, scale, boundingBox, terrainPack);
	}

	@Override
	public void move() {
		checkInputs();
		super.move();
	}

	@Override
	protected void checkInputs() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed += ACCELERATION * DisplayManager.getFrameTimeSeconds();
			this.currentSpeed = Math.min(currentSpeed, MAX_SPEED);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed -= BRAKE * DisplayManager.getFrameTimeSeconds();
			this.currentSpeed = Math.max(currentSpeed, 0);
		} else {
			this.currentSpeed -= DECELERATION * DisplayManager.getFrameTimeSeconds();
			this.currentSpeed = Math.max(currentSpeed, 0);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed += TURN_FORCE * DisplayManager.getFrameTimeSeconds();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed -= TURN_FORCE * DisplayManager.getFrameTimeSeconds();
		} else {
			if (this.currentTurnSpeed > 0) {
				this.currentTurnSpeed -= TURN_FORCE;
				if (currentTurnSpeed < 0)
					this.currentTurnSpeed = 0;
			} else if (this.currentTurnSpeed < 0) {
				this.currentTurnSpeed += TURN_FORCE;
				if (currentTurnSpeed > 0)
					this.currentTurnSpeed = 0;
			}
		}

		this.currentTurnSpeed = Math.min(currentTurnSpeed, TURN_MAX);
		this.currentTurnSpeed = Math.max(currentTurnSpeed, -TURN_MAX);
	}

}
