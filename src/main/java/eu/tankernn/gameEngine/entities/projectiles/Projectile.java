package eu.tankernn.gameEngine.entities.projectiles;

import java.util.Collection;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.particles.ParticleSystem;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public abstract class Projectile extends Entity3D {
	
	private ParticleSystem particleSystem;
	private final float range;
	private final Vector3f startPosition;
	
	public Projectile(TerrainPack terrain, TexturedModel model, Vector3f position, Vector3f velocity, float range, AABB boundingBox, ParticleSystem particleSystem) {
		super(model, position, boundingBox, terrain);
		this.particleSystem = particleSystem;
		this.getVelocity().set(velocity);
		this.range = range;
		this.startPosition = new Vector3f(position);
	}
	
	public void update() {
		super.update();
		particleSystem.setPosition(this.getPosition());
		
		if (this.terrain != null) {
			getPosition().y = Math.max(getPosition().y, 5 + terrain.getTerrainHeightByWorldPos(getPosition().x, getPosition().z));
		}
		
		Vector3f distance = Vector3f.sub(getPosition(), startPosition, null);
		if (distance.length() > range) {
			kill();
		}
	}
	
	public void checkCollision(Collection<Entity3D> collection) {
		collection.stream().filter((e) -> AABB.collides(e.getBoundingBox(), Projectile.this.getBoundingBox())).forEach(this::onCollision);
	}

	protected void kill() {
		particleSystem.remove();
		this.dead = true;
	}
	
	public abstract void onCollision(Entity3D entity);
}
