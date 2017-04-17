package eu.tankernn.gameEngine.loader.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;

public class TextureAtlas {

	public final int textureId;
	private final int type;
	public final Vector2f dimensions;
	public final int rows;
	
	protected TextureAtlas(int textureId, int width, int height) {
		this(textureId, GL11.GL_TEXTURE_2D, width, height, 1);
	}
	
	protected TextureAtlas(int textureId, int type, int width, int height, int rows) {
		this.textureId = textureId;
		this.dimensions = new Vector2f(width, height);
		this.type = type;
		this.rows = rows;
	}
	
	public void bindToUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(type, textureId);
	}

	public void delete() {
		GL11.glDeleteTextures(textureId);
	}
}
