package eu.tankernn.gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.World;
import eu.tankernn.gameEngine.animation.model.AnimatedModel;
import eu.tankernn.gameEngine.audio.Source;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.particles.ParticleSystem;
import eu.tankernn.gameEngine.util.IPositionable;

public class Entity3D implements IPositionable {

	private EntityState state;
	private TexturedModel model;
	private ParticleSystem particleSystem;
	private AABB boundingBox;
	protected World world;

	protected Source source = new Source();

	public Entity3D(EntityState state, TexturedModel model, ParticleSystem particleSystem, AABB boundingBox,
			World world) {
		this.state = state;
		this.model = model;
		this.particleSystem = particleSystem;
		this.boundingBox = boundingBox;
		this.world = world;
	}

	public void update(GameContext ctx) {
		state.update(ctx);

		source.setPosition(getPosition());
		source.setVelocity(getVelocity());
		if (particleSystem != null)
			particleSystem.setPosition(getPosition());
		this.boundingBox.updatePosition(getPosition());
		if (model instanceof AnimatedModel) {
			float speed = state.getVelocity().length();
			if (speed == 0) {
				((AnimatedModel) getModel()).doAnimation("idle");
			} else {
				((AnimatedModel) getModel()).doAnimation("run");
			}
			((AnimatedModel) model).update();
		}
	}

	public boolean isInAir() {
		return getPosition().y > getTerrainHeight();
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return state.getPosition();
	}

	public void setPosition(Vector3f position) {
		state.getPosition().set(position);
	}

	public Vector3f getVelocity() {
		return state.getVelocity();
	}

	public Vector3f getRotation() {
		return state.getRotation();
	}

	public Vector3f getScale() {
		return state.getScale();
	}

	public void setScale(Vector3f scale) {
		state.getScale().set(scale);
	}

	public AABB getBoundingBox() {
		return boundingBox;
	}

	protected void kill() {
		if (particleSystem != null)
			particleSystem.remove();
		this.getState().setDead(true);
	}

	public boolean isDead() {
		return state.isDead();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Entity3D) {
			return this.getId() == ((Entity3D) obj).getId();
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return state.getId();
	}

	public int getId() {
		return state.getId();
	}

	public EntityState getState() {
		return state;
	}

	public void setState(EntityState state) {
		//this.state = state;
		this.getPosition().set(state.getPosition());
		this.getState().setModelId(state.getModelId());
		this.getState().setDead(state.isDead());
	}

	public float getHeight() {
		return getBoundingBox().getSize().y;
	}

	public float getTerrainHeight() {
		return world.getTerrainHeigh(getPosition().x, getPosition().z);
	}

}
