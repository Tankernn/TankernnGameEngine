package eu.tankernn.gameEngine.particles;

import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.util.IPositionable;

public interface IParticle extends IPositionable {
	ParticleTexture getTexture();
	float getScale();
	float getRotation();
	float getBlend();
	Vector2f getTexOffset1();
	Vector2f getTexOffset2();
	boolean update(Camera camera);
}
