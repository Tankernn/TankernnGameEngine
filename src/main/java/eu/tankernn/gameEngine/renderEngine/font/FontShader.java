package eu.tankernn.gameEngine.renderEngine.font;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec2;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec3;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/renderEngine/font/fontVertex.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/renderEngine/font/fontFragment.glsl";
	
	protected UniformVec3 color = new UniformVec3("color");
	protected UniformVec2 translation = new UniformVec2("translation");
	protected UniformFloat width = new UniformFloat("width");
	protected UniformFloat edge = new UniformFloat("edge");
	protected UniformFloat borderWidth = new UniformFloat("borderWidth");
	protected UniformFloat borderEdge = new UniformFloat("borderEdge");
	protected UniformVec3 outlineColor = new UniformVec3("outlineColor");
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "textureCoords");
		super.storeAllUniformLocations(color, translation, width, edge, borderWidth, borderEdge, outlineColor);
	}
	
}
