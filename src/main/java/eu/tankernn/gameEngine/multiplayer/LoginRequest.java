package eu.tankernn.gameEngine.multiplayer;

import java.io.Serializable;

public class LoginRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2245699524120454081L;
	
	public final String username;

	public LoginRequest(String username) {
		this.username = username;
	}

}
