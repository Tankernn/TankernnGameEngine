package eu.tankernn.gameEngine.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.ai.Behavior;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;

public class PlayerBehavior extends Behavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = -361242008858358717L;
	
	private static final float RUN_SPEED = 20;
	protected static final float TURN_MAX = 160;
	private static final float JUMP_POWER = 30;

	protected float currentSpeed = 0;
	protected float currentTurnSpeed = 0;

	public void update(GameContext impl) {
		if (!impl.isClient())
			return;
		checkInputs(impl);
		entity.increaseRotation(new Vector3f(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0));
		entity.generateVelocity(currentSpeed, impl.getTickLengthSeconds());
	}

	private void jump(float terrainHeight) {
		if (entity.getPosition().y <= terrainHeight) {
			entity.getVelocity().y = JUMP_POWER;

			// source.play(jumpSoundBuffer);
		}
	}

	protected void checkInputs(GameContext impl) {
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
			jump(impl.getTerrainHeight(entity.getPosition().x, entity.getPosition().z));
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = 5 * RUN_SPEED;
		}
	}
}
