package eu.tankernn.gameEngine.particles;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/particles/particleVShader.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/particles/particleFShader.glsl";

	protected UniformFloat numberOfRows = new UniformFloat("numberOfRows");
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");

	public ParticleShader() {
		//																 0,					1,						5,				6
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "modelViewMatrix", "texOffsets", "blendFactor");
		super.storeAllUniformLocations(numberOfRows, projectionMatrix);
	}

}
