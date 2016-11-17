package eu.tankernn.gameEngine.renderEngine.shaders;

import org.lwjgl.opengl.GL20;

public abstract class Uniform {
	
	private static final int NOT_FOUND = -1;
	
	private String name;
	private int location;
	protected boolean used = false;
	
	protected Uniform(String name){
		this.name = name;
	}
	
	protected void storeUniformLocation(int programID){
		location = GL20.glGetUniformLocation(programID, name);
		if(location == NOT_FOUND){
			System.err.println("No uniform variable called " + name + " found for program " + programID + "!");
		}
	}
	
	protected int getLocation(){
		return location;
	}
	
	protected boolean isUsed() {
		return used;
	}
	
	protected String getName() {
		return name;
	}
}
