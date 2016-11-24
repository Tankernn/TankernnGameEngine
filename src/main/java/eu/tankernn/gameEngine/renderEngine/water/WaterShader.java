package eu.tankernn.gameEngine.renderEngine.water;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformSampler;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec3;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformViewMatrix;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "/eu/tankernn/gameEngine/renderEngine/water/waterVertex.glsl";
	private final static String FRAGMENT_FILE = "/eu/tankernn/gameEngine/renderEngine/water/waterFragment.glsl";

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
	protected UniformFloat nearPlane = new UniformFloat("nearPlane");
	protected UniformFloat farPlane = new UniformFloat("farPlane");
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformFloat waveStrength = new UniformFloat("waveStrength");

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		super.getLightUniformLocations();
		super.storeAllUniformLocations(modelMatrix, viewMatrix, projectionMatrix, reflectionTexture, refractionTexture,
				dudvMap, moveFactor, cameraPosition, normalMap, depthMap, nearPlane, farPlane, shineDamper, reflectivity, waveStrength);
	}

	public void connectTextureUnits() {
		reflectionTexture.loadTexUnit(0);
		refractionTexture.loadTexUnit(1);
		dudvMap.loadTexUnit(2);
		normalMap.loadTexUnit(3);
		depthMap.loadTexUnit(4);
	}

}
