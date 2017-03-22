package eu.tankernn.gameEngine.renderEngine.shadows;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformBoolean;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMat4Array;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;
import eu.tankernn.gameEngine.settings.Settings;

public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/renderEngine/shadows/shadowVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/renderEngine/shadows/shadowFragmentShader.glsl";
	
	protected UniformMatrix mvpMatrix = new UniformMatrix("mvpMatrix");
	protected UniformMat4Array jointTransforms = new UniformMat4Array("jointTransforms", Settings.MAX_JOINTS);
	protected UniformBoolean animated = new UniformBoolean("isAnimated");
	
	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "in_position", "in_textureCoords", "normal", "tangent", "in_jointIndices", "in_weights");
		super.storeAllUniformLocations(mvpMatrix, jointTransforms, animated);
	}
	
}
