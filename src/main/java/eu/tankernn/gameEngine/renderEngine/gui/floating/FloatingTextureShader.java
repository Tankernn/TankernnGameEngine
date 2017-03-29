package eu.tankernn.gameEngine.renderEngine.gui.floating;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;

public class FloatingTextureShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/renderEngine/gui/floating/floatingVertex.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/renderEngine/gui/floating/floatingFragment.glsl";
	
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformMatrix modelViewMatrix = new UniformMatrix("modelViewMatrix");
	
	public FloatingTextureShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		super.storeAllUniformLocations(projectionMatrix, modelViewMatrix);
	}
	
}
