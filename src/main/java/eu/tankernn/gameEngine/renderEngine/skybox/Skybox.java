package eu.tankernn.gameEngine.renderEngine.skybox;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.RawModel;

public class Skybox {

	private RawModel cube;
	private Texture dayTexture, nightTexture;

	public Skybox(Texture dayTexture, Texture nightTexture, float size) {
		cube = CubeGenerator.generateCube(size);
		this.dayTexture = dayTexture;
		this.nightTexture = nightTexture;
	}

	public RawModel getCubeVao() {
		return cube;
	}

	public Texture getDayTexture() {
		return dayTexture;
	}

	public Texture getNightTexture() {
		return nightTexture;
	}

	public void delete() {
		cube.delete();
		dayTexture.delete();
	}

}
