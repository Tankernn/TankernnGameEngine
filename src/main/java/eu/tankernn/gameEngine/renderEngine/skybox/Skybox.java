package eu.tankernn.gameEngine.renderEngine.skybox;

import eu.tankernn.gameEngine.renderEngine.Vao;
import eu.tankernn.gameEngine.textures.Texture;

public class Skybox {
	
	private Vao cube;
	private Texture dayTexture, nightTexture;
	
	public Skybox(Texture dayTexture, Texture nightTexture, float size){
		cube = CubeGenerator.generateCube(size);
		this.dayTexture = dayTexture;
		this.nightTexture = nightTexture;
	}
	
	public Vao getCubeVao(){
		return cube;
	}
	
	public Texture getDayTexture(){
		return dayTexture;
	}
	
	public Texture getNightTexture(){
		return nightTexture;
	}
	
	public void delete(){
		cube.delete();
		dayTexture.delete();
	}

}
