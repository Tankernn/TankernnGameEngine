package eu.tankernn.gameEngine.loader.font;

import org.lwjgl.util.vector.Vector3f;

public class Font {
	public final FontFamily family;
	public final float size, width, edge, outlineWidth, outlineEdge;
	public final Vector3f color, outlineColor;
	
	public Font(FontFamily family, float size, float width, float edge, float outlineWidth, float outlineEdge, Vector3f color, Vector3f outlineColor) {
		this.family = family;
		this.size = size;
		this.width = width;
		this.edge = edge;
		this.outlineWidth = outlineWidth;
		this.outlineEdge = outlineEdge;
		this.color = color;
		this.outlineColor = outlineColor;
	}
	
	public Font(FontFamily family, float size, Vector3f color, Vector3f outlineColor) {
		this(family, size, 0.5f, 0.1f, 0, 0, color, outlineColor);
	}
	
	public Font(FontFamily family, float size, Vector3f color) {
		this(family, size, color, new Vector3f(0, 0, 0));
	}
	
	public Font(FontFamily family, float size) {
		this(family, size, new Vector3f(1, 1, 1));
	}
	
	public Font(FontFamily family) {
		this(family, 1);
	}
	
}
