package eu.tankernn.gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.particles.ParticleSystem;

public class BasicProjectile extends Projectile {
	
	private Entity3D creator;
	
	public BasicProjectile(Entity3D creator, Vector3f position, Vector3f velocity, float range, ParticleSystem particleSystem) {
		super(null, position, velocity, range, new AABB(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)), particleSystem);
		this.creator = creator;
	}
	
	@Override
	public void onCollision(Entity3D entity) {
		if (entity.equals(creator))
			return;
		System.out.println("Collision!");
		this.kill();
	}
	
}
