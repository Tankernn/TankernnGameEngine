package eu.tankernn.gameEngine.particles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.renderEngine.DisplayManager;

public class ParticleSystem {
	private final float pps, speed, gravityComplient, lifeLength;
	private Vector3f position;
	
	private ParticleTexture texture;
	private boolean dead;
	
	public ParticleSystem(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength) {
		this.texture = texture;
		this.pps = pps;
		this.speed = speed;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
	}
	
	public ParticleSystem(ParticleSystem system) {
		this(system.texture, system.pps, system.speed, system.gravityComplient, system.lifeLength);
	}
	
	public void setPosition(Vector3f systemCenter) {
		this.position = new Vector3f(systemCenter);
	}
	
	public List<Particle> generateParticles() {
		List<Particle> particles = new ArrayList<Particle>();
		float delta = DisplayManager.getFrameTimeSeconds();
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			particles.add(createParticle(position));
		}
		if (Math.random() < partialParticle) {
			particles.add(createParticle(position));
		}
		return particles;
	}
	
	private Particle createParticle(Vector3f center) {
		float dirX = (float) Math.random() * 2f - 1f;
		float dirZ = (float) Math.random() * 2f - 1f;
		Vector3f velocity = new Vector3f(dirX, 1, dirZ);
		velocity.normalise();
		velocity.scale(speed);
		return new Particle(texture, new Vector3f(center), velocity, gravityComplient, lifeLength, 0, 1);
	}

	public void remove() {
		this.dead = true;
	}
	
	public boolean isDead() {
		return dead;
	}
}
