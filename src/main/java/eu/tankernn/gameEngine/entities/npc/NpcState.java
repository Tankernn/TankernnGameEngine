package eu.tankernn.gameEngine.entities.npc;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.entities.ai.Behavior;

public class NpcState extends EntityState {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8182776911367238544L;
	
	private int health;
	private final int maxHealth;
	
	public NpcState(int modelId, int systemId, Vector3f position, Vector3f rotation, Vector3f velocity, Vector3f scale, Behavior... behaviors) {
		super(modelId, systemId, position, rotation, velocity, scale, behaviors);
		this.health = this.maxHealth = health;
	}
	
	@Override
	public boolean isDead() {
		return health <= 0;
	}
	
	public void fullHeal() {
		this.health = this.maxHealth;
	}
	
}
