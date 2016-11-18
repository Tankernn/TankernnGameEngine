package eu.tankernn.gameEngine.particles;

import eu.tankernn.gameEngine.loader.textures.Texture;

public class ParticleTexture {
	private Texture texture;
	private int numberOfRows;
	private boolean additive;
	
	public ParticleTexture(Texture texture, int numberOfRows, boolean additive) {
		this.texture = texture;
		this.numberOfRows = numberOfRows;
		this.additive = additive;
	}

	public Texture getTexture() {
		return texture;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	protected boolean usesAdditiveBlending() {
		return additive;
	}
}
