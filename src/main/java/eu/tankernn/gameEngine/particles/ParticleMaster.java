package eu.tankernn.gameEngine.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.util.DistanceSorter;

public class ParticleMaster {
	private Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	private List<ParticleSystem> systems = new ArrayList<ParticleSystem>();
	private ParticleRenderer renderer;

	public ParticleMaster(Loader loader, Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(loader, projectionMatrix);
	}

	public void update(Camera camera) {
		for (ParticleSystem sys : systems) {
			for (Particle particle : sys.generateParticles()) {
				addParticle(particle);
			}
		}

		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			Entry<ParticleTexture, List<Particle>> entry = mapIterator.next();
			List<Particle> list = entry.getValue();
			Iterator<Particle> iterator = list.iterator();
			while (iterator.hasNext()) {
				Particle p = iterator.next();
				boolean stillAlive = p.update(camera);
				if (!stillAlive) {
					iterator.remove();
					if (list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
			if (!entry.getKey().usesAdditiveBlending())
				DistanceSorter.sort(list, camera);
		}
		
		systems.removeIf(ParticleSystem::isDead);
	}

	public void renderParticles(Camera camera) {
		renderer.render(particles, camera);
	}

	public void cleanUp() {
		renderer.cleanUp();
	}

	public void addParticle(Particle particle) {
		List<Particle> list = particles.get(particle.getTexture());
		if (list == null) {
			list = new ArrayList<Particle>();
			particles.put(particle.getTexture(), list);
		}
		list.add(particle);
	}

	public void addSystem(ParticleSystem system) {
		this.systems.add(system);
	}
}
