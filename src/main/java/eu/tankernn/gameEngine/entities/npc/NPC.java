package eu.tankernn.gameEngine.entities.npc;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class NPC extends Entity3D {
	
	private int health;
	private final int maxHealth;
	
	private List<Behavior> behaviors;
	
	public NPC(TexturedModel model, Vector3f position, int health, AABB boundingBox, TerrainPack terrainPack, Behavior... behaviors) {
		super(model, position, boundingBox, terrainPack);
		this.health = this.maxHealth = health;
		this.behaviors = Arrays.asList(behaviors);
		this.behaviors.forEach(b -> b.setEntity(this));
	}
	
	@Override
	public void update() {
		if (this.health <= 0)
			this.dead = true;
		
		behaviors.forEach(Behavior::update);
		
		super.update();
	}
	
	public void fullHeal() {
		this.health = this.maxHealth;
	}
	
}
