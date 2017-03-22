package eu.tankernn.gameEngine.entities;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.particles.ParticleSystem;

public class BasicProjectile extends Projectile {

	public BasicProjectile(TexturedModel model, Vector3f position, Vector3f velocity, ParticleSystem particleSystem) {
		super(model, position, velocity, new AABB(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)), particleSystem);
	}

	@Override
	public void onCollision(Entity3D entity) {
		System.out.println("Collision!");
	}

}
