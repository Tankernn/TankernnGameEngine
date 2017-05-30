package eu.tankernn.gameEngine.entities;

import eu.tankernn.gameEngine.World;
import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.particles.ParticleMaster;
import eu.tankernn.gameEngine.particles.ParticleSystem;

public class EntityFactory {
	private final Loader loader;
	private final ParticleMaster particleMaster;
	private final World world;

	public EntityFactory(Loader loader, ParticleMaster particleMaster, World world) {
		super();
		this.loader = loader;
		this.particleMaster = particleMaster;
		this.world = world;
	}

	public Entity3D getEntity(EntityState state) {
		TexturedModel model = loader.getModel(state.getModelId());
		ParticleSystem particleSystem = loader.getParticleSystem(state.getParticleSystemId());
		if (particleSystem != null)
			particleMaster.addSystem(particleSystem);
		AABB boundingBox = loader.getBoundingBox(model.getModel().id);
		return new Entity3D(state, model, particleSystem, boundingBox, world);
	}
}
