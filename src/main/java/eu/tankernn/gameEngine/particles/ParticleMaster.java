package eu.tankernn.gameEngine.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.font.Font;
import eu.tankernn.gameEngine.loader.font.GUIText;
import eu.tankernn.gameEngine.renderEngine.Fbo;
import eu.tankernn.gameEngine.renderEngine.font.FontRenderer;
import eu.tankernn.gameEngine.util.DistanceSorter;

public class ParticleMaster {
	private Loader loader;

	private Map<ParticleTexture, List<Particle>> particles = new HashMap<>();
	private List<ParticleSystem> systems = new ArrayList<>();
	private ParticleRenderer renderer;
	private FontRenderer fontRenderer = new FontRenderer();

	public ParticleMaster(Loader loader, Matrix4f projectionMatrix) {
		this.loader = loader;
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

	@Override
	public void finalize() {
		renderer.finalize();
	}

	public void addTextParticle(String text, Font font, Vector3f position) {
		GUIText guiText = new GUIText(text, font, new Vector2f(0, 0), 1.0f, false);
		guiText.update(loader);
		Fbo fbo = new Fbo((int) (100 * font.size), (int) (100 * font.size), 0);
		fbo.bindFrameBuffer();
		fontRenderer.render(guiText);
		fbo.unbindFrameBuffer();
		addParticle(new Particle(new ParticleTexture(fbo.getColourTexture(), 1, true), position, new Vector3f(0, 0, 0),
				0.1f, 4, 0, font.size));
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
