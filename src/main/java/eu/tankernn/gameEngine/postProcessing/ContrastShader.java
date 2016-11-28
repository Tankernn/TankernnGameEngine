package eu.tankernn.gameEngine.postProcessing;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;

public class ContrastShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/postProcessing/simpleVertex.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/postProcessing/contrastFragment.glsl";
	
	public ContrastShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
	}
	
}
