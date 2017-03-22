package eu.tankernn.gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.particles.ParticleSystem;

public abstract class Projectile extends Entity3D {
	
	private Vector3f velocity;
	private ParticleSystem particleSystem;
	
	public Projectile(TexturedModel model, Vector3f position, Vector3f velocity, AABB boundingBox, ParticleSystem particleSystem) {
		super(model, position, new Vector3f(0, 0, 0), 1, boundingBox);
		this.particleSystem = particleSystem;
		this.velocity = velocity;
	}
	
	public void update() {
		this.increasePosition(velocity.x, velocity.y, velocity.z);
		particleSystem.setPosition(this.getPosition());
	}
	
	public abstract void onCollision(Entity3D entity);
}
