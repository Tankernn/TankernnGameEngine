package eu.tankernn.gameEngine.entities;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.util.IPositionable;

public interface ILight extends IPositionable, Serializable {
	Vector3f getColor();
	Vector3f getAttenuation();
}
