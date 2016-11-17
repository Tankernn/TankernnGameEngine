package eu.tankernn.gameEngine.postProcessing.gaussianBlur;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;

public class HorizontalBlurShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/postProcessing/gaussianBlur/horizontalBlurVertex.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/postProcessing/gaussianBlur/blurFragment.glsl";

	protected UniformFloat targetWidth = new UniformFloat("targetWidth");

	protected HorizontalBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		super.storeAllUniformLocations(targetWidth);
	}

}
