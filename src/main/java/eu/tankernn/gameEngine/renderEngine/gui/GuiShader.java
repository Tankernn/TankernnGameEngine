package eu.tankernn.gameEngine.renderEngine.gui;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;

public class GuiShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/renderEngine/gui/guiVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/renderEngine/gui/guiFragmentShader.glsl";
	
	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	protected UniformFloat opacity = new UniformFloat("opacity");
	
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		super.storeAllUniformLocations(transformationMatrix, opacity);
	}
	
}
