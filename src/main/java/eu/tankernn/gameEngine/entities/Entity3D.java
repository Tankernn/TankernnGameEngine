package eu.tankernn.gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.animation.model.AnimatedModel;
import eu.tankernn.gameEngine.audio.Source;
import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.terrains.TerrainPack;
import eu.tankernn.gameEngine.util.IPositionable;

public class Entity3D implements IPositionable {

	private TexturedModel model;
	private EntityState state;
	private AABB boundingBox;
	protected boolean dead;
	protected TerrainPack terrain;
	protected Source source = new Source();

	public Entity3D(TexturedModel model, Vector3f position, AABB boundingBox, TerrainPack terrain) {
		this(model, position, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), boundingBox, terrain);
	}

	public Entity3D(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale, AABB boundingBox,
			TerrainPack terrain) {
		this.model = model;
		this.state = new EntityState(model == null ? -1 : model.getId(), position, rotation, new Vector3f(0, 0, 0), scale);
		this.boundingBox = boundingBox;
		this.boundingBox.updatePosition(position);
		this.terrain = terrain;
	}

	public Entity3D(EntityState state, Loader loader, TerrainPack terrain) {
		this.state = state;
		this.model = loader.getModel(state.getModelId());
		this.boundingBox = loader.getBoundingBox(model.getModel().id);
		this.boundingBox.updatePosition(state.getPosition());
		this.terrain = terrain;
	}

	public void update() {
		state.update(terrain::getTerrainHeightByWorldPos);

		source.setPosition(getPosition());
		source.setVelocity(getVelocity());
		this.boundingBox.updatePosition(getPosition());
		if (model instanceof AnimatedModel)
			((AnimatedModel) model).update();
	}

	public boolean isInAir() {
		return getPosition().y > terrain.getTerrainHeightByWorldPos(getPosition().x, getPosition().z);
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

	public boolean isDead() {
		return dead;
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
		this.state = state;
	}

}
