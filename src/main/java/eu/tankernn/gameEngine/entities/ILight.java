package eu.tankernn.gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.util.IPositionable;

public interface ILight extends IPositionable {
	Vector3f getColor();
	Vector3f getAttenuation();
}
