package eu.tankernn.gameEngine.entities.projectiles;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.particles.ParticleSystem;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public abstract class Projectile extends Entity3D {
	
	protected Vector3f velocity;
	private ParticleSystem particleSystem;
	private final float range;
	private final Vector3f startPosition;
	private TerrainPack terrain;
	
	public Projectile(TerrainPack terrain, TexturedModel model, Vector3f position, Vector3f velocity, float range, AABB boundingBox, ParticleSystem particleSystem) {
		super(model, position, new Vector3f(0, 0, 0), 1, boundingBox);
		this.particleSystem = particleSystem;
		this.velocity = velocity;
		this.range = range;
		this.startPosition = new Vector3f(position);
		this.terrain = terrain;
	}
	
	public void update() {
		this.increasePosition((Vector3f) new Vector3f(velocity).scale(DisplayManager.getFrameTimeSeconds()));
		particleSystem.setPosition(this.getPosition());
		
		if (this.terrain != null) {
			this.position.y = Math.max(this.position.y, 5 + terrain.getTerrainHeightByWorldPos(position.x, position.z));
		}
		
		Vector3f distance = Vector3f.sub(position, startPosition, null);
		if (distance.length() > range) {
			kill();
		}
		super.update();
	}
	
	public void checkCollision(List<Entity3D> entities) {
		entities.stream().filter((e) -> !e.equals(Projectile.this)).filter((e) -> AABB.collides(e.getBoundingBox(), Projectile.this.getBoundingBox())).forEach(this::onCollision);
	}

	protected void kill() {
		particleSystem.remove();
		this.dead = true;
	}
	
	public abstract void onCollision(Entity3D entity);
}
