package eu.tankernn.gameEngine.postProcessing.gaussianBlur;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;

public class VerticalBlurShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/postProcessing/gaussianBlur/verticalBlurVertex.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/postProcessing/gaussianBlur/blurFragment.glsl";
	
	protected UniformFloat targetHeight = new UniformFloat("targetHeight");
	
	protected VerticalBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		super.storeAllUniformLocations(targetHeight);
	}
}
