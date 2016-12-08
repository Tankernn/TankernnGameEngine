package eu.tankernn.gameEngine.loader.font;

import eu.tankernn.gameEngine.loader.textures.Texture;

/**
 * Represents a font. It holds the font's texture atlas as well as having the
 * ability to create the quad vertices for any text using this font.
 * 
 * @author Karl
 *
 */
public class FontType {

	private Texture textureAtlas;
	private TextMeshCreator loader;

	/**
	 * Creates a new font and loads up the data about each character from the
	 * font file.
	 * 
	 * @param texture
	 *            - the ID of the font atlas texture.
	 * @param fontFile
	 *            - the font file containing information about each character in
	 *            the texture atlas.
	 */
	public FontType(Texture texture, String fontFile) {
		this.textureAtlas = texture;
		this.loader = new TextMeshCreator(fontFile);
	}

	/**
	 * @return The font texture atlas.
	 */
	public Texture getTextureAtlas() {
		return textureAtlas;
	}

	/**
	 * Takes in an unloaded text and calculates all of the vertices for the quads
	 * on which this text will be rendered. The vertex positions and texture
	 * coords and calculated based on the information from the font file.
	 * 
	 * @param text
	 *            - the unloaded text.
	 * @return Information about the vertices of all the quads.
	 */
	public TextMeshData loadText(GUIText text) {
		return loader.createTextMesh(text);
	}

}
