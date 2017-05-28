package eu.tankernn.gameEngine.particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.ILight;

public class Sun implements IParticle, ILight {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4224790312871439692L;

	private static final float SUN_DIS = 50;// fairly arbitrary - but make sure
											// it doesn't go behind skybox

	private final ParticleTexture texture;

	private Vector3f lightDirection = new Vector3f(0, -1, 0);
	private Vector3f position, color;
	private float scale;

	public Sun(ParticleTexture texture, float scale, Vector3f color) {
		this.texture = texture;
		this.scale = scale;
		this.color = color;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void setDirection(float x, float y, float z) {
		lightDirection.set(x, y, z);
		lightDirection.normalise();
	}

	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector3f getLightDirection() {
		return lightDirection;
	}

	public float getScale() {
		return scale;
	}

	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Calculates a position for the sun, based on the light direction. The
	 * distance of the sun from the camera is fairly arbitrary, although care
	 * should be taken to ensure it doesn't get rendered outside the skybox.
	 * 
	 * @param camPos
	 *            - The camera's position.
	 * @return The 3D world position of the sun.
	 */
	private Vector3f calculateWorldPosition(Vector3f camPos) {
		Vector3f sunPos = new Vector3f(lightDirection);
		sunPos.negate();
		sunPos.scale(SUN_DIS);
		return Vector3f.add(camPos, sunPos, null);
	}

	@Override
	public float getRotation() {
		return 0;
	}

	@Override
	public float getBlend() {
		return 0;
	}

	@Override
	public Vector2f getTexOffset1() {
		return new Vector2f(0, 0);
	}

	@Override
	public Vector2f getTexOffset2() {
		return new Vector2f(0, 0);
	}

	@Override
	public boolean update(Camera camera) {
		this.position = calculateWorldPosition(camera.getPosition());
		return true;
	}

	@Override
	public Vector3f getColor() {
		return color;
	}

	@Override
	public Vector3f getAttenuation() {
		return new Vector3f(1, 0, 0);
	}

}
