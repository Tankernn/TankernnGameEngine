package eu.tankernn.gameEngine.renderEngine.entities;

import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformBoolean;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformSampler;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec2;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec3;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec4;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformViewMatrix;

public class EntityShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/renderEngine/entities/vertexShader.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/renderEngine/entities/fragmentShader.glsl";

	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	public UniformViewMatrix viewMatrix = new UniformViewMatrix("viewMatrix");

	public UniformFloat shineDamper = new UniformFloat("shineDamper");
	public UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformFloat refractivity = new UniformFloat("refractivity");
	protected UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
	public UniformVec3 skyColor = new UniformVec3("skyColor");
	public UniformFloat numberOfRows = new UniformFloat("numberOfRows");
	protected UniformVec2 offset = new UniformVec2("offset");
	public UniformVec4 plane = new UniformVec4("plane");
	protected UniformMatrix toShadowMapSpace = new UniformMatrix("toShadowMapSpace");
	protected UniformSampler shadowMap = new UniformSampler("shadowMap");
	protected UniformSampler specularMap = new UniformSampler("specularMap");
	public UniformBoolean usesSpecularMap = new UniformBoolean("usesSpecularMap");
	protected UniformSampler modelTexture = new UniformSampler("modelTexture");
	protected UniformVec3 cameraPosition = new UniformVec3("cameraPosition");
	protected UniformSampler enviroMap = new UniformSampler("enviroMap");

	public EntityShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "textureCoords", "normal");
		super.getLightUniformLocations();
		super.storeAllUniformLocations(transformationMatrix, projectionMatrix, viewMatrix, shineDamper, reflectivity,
				refractivity, useFakeLighting, skyColor, numberOfRows, offset, plane, toShadowMapSpace, shadowMap,
				specularMap, usesSpecularMap, modelTexture, cameraPosition, enviroMap);
	}

	public EntityShader(String vertexFile, String fragmentFile, String... string) {
		super(vertexFile, fragmentFile, string);
	}

	protected void connectTextureUnits() {
		shadowMap.loadTexUnit(5);
		modelTexture.loadTexUnit(0);
		specularMap.loadTexUnit(1);
		enviroMap.loadTexUnit(10);
	}
}
