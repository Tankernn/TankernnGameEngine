package eu.tankernn.gameEngine.renderEngine.skybox;

import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.Vao;

public class Skybox {

	private Vao cube;
	private Texture dayTexture, nightTexture;

	public Skybox(Loader loader, Texture dayTexture, Texture nightTexture, float size) {
		cube = loader.generateCube(size);
		this.dayTexture = dayTexture;
		this.nightTexture = nightTexture;
	}

	public Vao getCubeVao() {
		return cube;
	}

	public Texture getDayTexture() {
		return dayTexture;
	}

	public Texture getNightTexture() {
		return nightTexture;
	}

	public void delete() {
		cube.finalize();
		dayTexture.delete();
	}

}
