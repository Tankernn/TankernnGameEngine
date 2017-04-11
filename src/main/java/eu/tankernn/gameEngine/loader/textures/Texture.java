package eu.tankernn.gameEngine.loader.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.util.InternalFile;

public class Texture {

	public final int textureId;
	public final int size;
	public final Vector2f ratio;
	private final int type;

	protected Texture(int textureId, int width, int height) {
		this(textureId, GL11.GL_TEXTURE_2D, width, height);
	}

	protected Texture(int textureId, int type, int width, int height) {
		this.textureId = textureId;
		this.ratio = (Vector2f) new Vector2f(width, height).normalise();
		this.size = width * height;
		this.type = type;
	}

	public void bindToUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(type, textureId);
	}

	public void delete() {
		GL11.glDeleteTextures(textureId);
	}

	public static TextureBuilder newTexture(InternalFile textureFile) {
		return new TextureBuilder(textureFile);
	}

	public static Texture newCubeMap(InternalFile[] textureFiles, int size) {
		int cubeMapId = TextureUtils.loadCubeMap(textureFiles);
		return new Texture(cubeMapId, GL13.GL_TEXTURE_CUBE_MAP, size);
	}
	
	public static Texture newEmptyCubeMap(int size) {
		int cubeMapId = TextureUtils.createEmptyCubeMap(size);
		return new Texture(cubeMapId, GL13.GL_TEXTURE_CUBE_MAP, size);
	}

	public int getWidth() {
		return (int) (size * ratio.x);
	}
	
	public int getHeight() {
		return (int) (size * ratio.y);
	}

}
