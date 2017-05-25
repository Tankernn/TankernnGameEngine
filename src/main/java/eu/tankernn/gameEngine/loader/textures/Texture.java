package eu.tankernn.gameEngine.loader.textures;

import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;

import eu.tankernn.gameEngine.util.InternalFile;

public class Texture {
	
	public final TextureAtlas atlas;
	public final int index;
	
	protected Texture(int textureId, int width, int height) {
		this(new TextureAtlas(textureId, width, height));
	}
	
	public Texture(TextureAtlas atlas) {
		this(atlas, 0);
	}
	
	public Texture(TextureAtlas atlas, int index) {
		this.atlas = atlas;
		this.index = index;
	}
	
	public void bindToUnit(int unit) {
		atlas.bindToUnit(unit);
	}

	public void delete() {
		atlas.delete();
	}

	public static TextureBuilder newTexture(InternalFile textureFile) {
		return new TextureBuilder(textureFile);
	}

	public static Texture newCubeMap(InternalFile[] textureFiles, int size) {
		int cubeMapId = TextureUtils.loadCubeMap(textureFiles);
		return new Texture(new TextureAtlas(cubeMapId, GL13.GL_TEXTURE_CUBE_MAP, size, size, 1));
	}
	
	public static Texture newEmptyCubeMap(int size) {
		int cubeMapId = TextureUtils.createEmptyCubeMap(size);
		return new Texture(new TextureAtlas(cubeMapId, GL13.GL_TEXTURE_CUBE_MAP, size, size, 1));
	}

	public int getWidth() {
		return (int) (atlas.dimensions.x / atlas.rows);
	}
	
	public int getHeight() {
		return (int) (atlas.dimensions.y / atlas.rows);
	}
	
	public int getSize() {
		return getWidth() * getHeight();
	}
	
	public Vector2f getRatio() {
		return (Vector2f) new Vector2f(getWidth(), getHeight()).normalise();
	}
	
	public float getXOffset() {
		int column = index % atlas.rows;
		return (float) column / (float) atlas.rows;
	}
	
	public float getYOffset() {
		int row = index / atlas.rows;
		return (float) row / (float) atlas.rows;
	}
}
