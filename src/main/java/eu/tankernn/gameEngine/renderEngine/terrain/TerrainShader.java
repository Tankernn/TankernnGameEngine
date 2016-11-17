package eu.tankernn.gameEngine.renderEngine.terrain;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformSampler;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec3;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec4;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformViewMatrix;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/renderEngine/terrain/terrainVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/renderEngine/terrain/terrainFragmentShader.glsl";

	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformViewMatrix viewMatrix = new UniformViewMatrix("viewMatrix");
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformVec3 skyColor = new UniformVec3("skyColor");
	protected UniformSampler backgroundTexture = new UniformSampler("backgroundTexture");
	protected UniformSampler rTexture = new UniformSampler("rTexture");
	protected UniformSampler gTexture = new UniformSampler("gTexture");
	protected UniformSampler bTexture = new UniformSampler("bTexture");
	protected UniformSampler blendMap = new UniformSampler("blendMap");
	protected UniformVec4 plane = new UniformVec4("plane");
	protected UniformMatrix toShadowMapSpace = new UniformMatrix("toShadowMapSpace");
	protected UniformSampler shadowMap = new UniformSampler("shadowMap");
	protected UniformFloat shadowDistance = new UniformFloat("shadowDistance");
	protected UniformFloat shadowMapSize = new UniformFloat("shadowMapSize");

	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "textureCoords", "normal");
		super.getLightUniformLocations();
		super.storeAllUniformLocations(transformationMatrix, projectionMatrix, viewMatrix, shineDamper, reflectivity,
				skyColor, backgroundTexture, rTexture, gTexture, bTexture, blendMap, plane, toShadowMapSpace, shadowMap,
				shadowDistance, shadowMapSize);
	}

	public void connectTextureUnits() {
		backgroundTexture.loadTexUnit(0);
		rTexture.loadTexUnit(1);
		gTexture.loadTexUnit(2);
		bTexture.loadTexUnit(3);
		blendMap.loadTexUnit(4);
		shadowMap.loadTexUnit(5);
	}
}
