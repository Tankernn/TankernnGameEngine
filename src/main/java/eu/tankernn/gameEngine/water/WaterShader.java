package eu.tankernn.gameEngine.water;

import eu.tankernn.gameEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.shaders.UniformMatrix;
import eu.tankernn.gameEngine.shaders.UniformSampler;
import eu.tankernn.gameEngine.shaders.UniformVec3;
import eu.tankernn.gameEngine.shaders.UniformViewMatrix;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "/eu/tankernn/gameEngine/water/waterVertex.glsl";
	private final static String FRAGMENT_FILE = "/eu/tankernn/gameEngine/water/waterFragment.glsl";

	protected UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
	protected UniformViewMatrix viewMatrix = new UniformViewMatrix("viewMatrix");
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformSampler reflectionTexture = new UniformSampler("reflectionTexture");
	protected UniformSampler refractionTexture = new UniformSampler("refractionTexture");
	protected UniformSampler dudvMap = new UniformSampler("dudvMap");
	protected UniformFloat moveFactor = new UniformFloat("moveFactor");
	protected UniformVec3 cameraPosition = new UniformVec3("cameraPosition");
	protected UniformSampler normalMap = new UniformSampler("normalMap");
	protected UniformSampler depthMap = new UniformSampler("depthMap");

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		super.getLightUniformLocations();
		super.storeAllUniformLocations(modelMatrix, viewMatrix, projectionMatrix, reflectionTexture, refractionTexture,
				dudvMap, moveFactor, cameraPosition, normalMap, depthMap);
	}

	public void connectTextureUnits() {
		reflectionTexture.loadTexUnit(0);
		refractionTexture.loadTexUnit(1);
		dudvMap.loadTexUnit(2);
		normalMap.loadTexUnit(3);
		depthMap.loadTexUnit(4);
	}

}
