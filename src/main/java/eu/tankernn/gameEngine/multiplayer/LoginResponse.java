package eu.tankernn.gameEngine.multiplayer;

import java.io.Serializable;

import eu.tankernn.gameEngine.entities.EntityState;

public class LoginResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2261275286169003120L;
	
	public final boolean success;
	public final EntityState playerState;

	public LoginResponse(boolean success, EntityState playerState) {
		this.success = success;
		this.playerState = playerState;
	}

}
