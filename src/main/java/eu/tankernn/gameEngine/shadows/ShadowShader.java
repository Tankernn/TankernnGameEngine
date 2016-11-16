package eu.tankernn.gameEngine.shadows;

import eu.tankernn.gameEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.shaders.UniformMatrix;

public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/shadows/shadowVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/shadows/shadowFragmentShader.glsl";
	
	protected UniformMatrix mvpMatrix = new UniformMatrix("mvpMatrix");
	
	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "in_position", "in_textureCoords");
		super.storeAllUniformLocations(mvpMatrix);
	}
	
}
