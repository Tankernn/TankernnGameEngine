package eu.tankernn.gameEngine.entities.npc;

import eu.tankernn.gameEngine.entities.Entity3D;

public abstract class Behavior {
	protected Entity3D entity;
	
	public abstract void update();
	
	public void setEntity(Entity3D entity) {
		this.entity = entity;
	}
}
