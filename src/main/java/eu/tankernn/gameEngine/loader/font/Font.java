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
	
}
