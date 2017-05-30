package eu.tankernn.gameEngine.entities.ai;

import java.io.Serializable;

import eu.tankernn.gameEngine.entities.GameContext;
import eu.tankernn.gameEngine.entities.EntityState;

public abstract class Behavior implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3510420608770918702L;
	
	protected EntityState entity;
	
	public abstract void update(GameContext impl);
	
	public void setEntity(EntityState npcState) {
		this.entity = npcState;
	}
}
