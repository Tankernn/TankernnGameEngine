package eu.tankernn.gameEngine.postProcessing.bloom;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformSampler;

public class CombineShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/postProcessing/simpleVertex.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/postProcessing/bloom/combineFragment.glsl";
	
	protected UniformSampler colourTexture = new UniformSampler("colourTexture");
	protected UniformSampler highlightTexture = new UniformSampler("highlightTexture");
	
	protected CombineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		connectTextureUnits();
	}
	
	protected void connectTextureUnits(){
		colourTexture.loadTexUnit(0);
		highlightTexture.loadTexUnit(1);
	}
	
}
