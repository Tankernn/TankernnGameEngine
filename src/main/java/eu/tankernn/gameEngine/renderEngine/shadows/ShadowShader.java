package eu.tankernn.gameEngine.renderEngine.shadows;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;

public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/renderEngine/shadows/shadowVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/renderEngine/shadows/shadowFragmentShader.glsl";
	
	protected UniformMatrix mvpMatrix = new UniformMatrix("mvpMatrix");
	
	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "in_position", "in_textureCoords");
		super.storeAllUniformLocations(mvpMatrix);
	}
	
}
