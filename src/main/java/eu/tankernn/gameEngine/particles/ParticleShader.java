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
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "modelViewMatrix");
		super.bindAttribute("texOffsets", 5);
		super.bindAttribute("blendFactor", 6);
		super.storeAllUniformLocations(numberOfRows, projectionMatrix);
	}

}
