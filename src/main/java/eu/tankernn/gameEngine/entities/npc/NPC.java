package eu.tankernn.gameEngine.entities.npc;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class NPC extends Entity3D {
	
	private int health;
	private final int maxHealth;
	
	// Roaming stuff, should probable be a component or something
	private Vector2f targetPosition;
	private RoamingArea area;
	private float standbyTime, speed;
	
	public NPC(TexturedModel model, Vector3f position, int health, AABB boundingBox, RoamingArea area, float speed, TerrainPack terrainPack) {
		super(model, position, boundingBox, terrainPack);
		this.area = area;
		this.speed = speed;
		this.health = this.maxHealth = health;
	}
	
	@Override
	public void update() {
		if (this.health <= 0)
			this.dead = true;
		
		// Roaming stuff, should probable be a component or something
		if (targetPosition == null) {
			if (standbyTime > 0f)
				standbyTime -= DisplayManager.getFrameTimeSeconds();
			else
				targetPosition = area.getPointInside();
		} else {
			Vector2f direction = (Vector2f) Vector2f.sub(targetPosition, new Vector2f(getPosition().x, getPosition().z), null);
			if (direction.length() < 1f) { // Reached target
				targetPosition = null;
				standbyTime = (float) (Math.random() * 5);
			}
			this.getRotation().y = (float) Math.toDegrees(Math.atan2(direction.x, direction.y));
			super.increasePosition(speed);
		}
		super.update();
	}
	
	public void fullHeal() {
		this.health = this.maxHealth;
	}
	
}
