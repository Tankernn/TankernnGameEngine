package eu.tankernn.gameEngine.renderEngine.entities;

import eu.tankernn.gameEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.shaders.UniformBoolean;
import eu.tankernn.gameEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.shaders.UniformMatrix;
import eu.tankernn.gameEngine.shaders.UniformSampler;
import eu.tankernn.gameEngine.shaders.UniformVec2;
import eu.tankernn.gameEngine.shaders.UniformVec3;
import eu.tankernn.gameEngine.shaders.UniformVec4;
import eu.tankernn.gameEngine.shaders.UniformViewMatrix;

public class EntityShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/renderEngine/entities/vertexShader.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/renderEngine/entities/fragmentShader.glsl";

	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformViewMatrix viewMatrix = new UniformViewMatrix("viewMatrix");
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
	protected UniformVec3 skyColor = new UniformVec3("skyColor");
	protected UniformFloat numberOfRows = new UniformFloat("numberOfRows");
	protected UniformVec2 offset = new UniformVec2("offset");
	protected UniformVec4 plane = new UniformVec4("plane");
	protected UniformMatrix toShadowMapSpace = new UniformMatrix("toShadowMapSpace");
	protected UniformSampler shadowMap = new UniformSampler("shadowMap");
	protected UniformSampler specularMap = new UniformSampler("specularMap");
	protected UniformBoolean usesSpecularMap = new UniformBoolean("usesSpecularMap");
	protected UniformSampler modelTexture = new UniformSampler("modelTexture");
	protected UniformVec3 cameraPosition = new UniformVec3("cameraPosition");
	protected UniformSampler enviroMap = new UniformSampler("enviroMap");

	public EntityShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "textureCoords", "normal");
		super.getLightUniformLocations();
		super.storeAllUniformLocations(transformationMatrix, projectionMatrix, viewMatrix, shineDamper, reflectivity,
				useFakeLighting, skyColor, numberOfRows, offset, plane, toShadowMapSpace, shadowMap, specularMap,
				usesSpecularMap, modelTexture, cameraPosition, enviroMap);
	}

	public void connectTextureUnits() {
		shadowMap.loadTexUnit(5);
		modelTexture.loadTexUnit(0);
		specularMap.loadTexUnit(1);
		enviroMap.loadTexUnit(10);
	}
}
