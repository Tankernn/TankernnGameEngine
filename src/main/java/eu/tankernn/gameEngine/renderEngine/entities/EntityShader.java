package eu.tankernn.gameEngine.renderEngine.entities;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.ILight;
import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformBoolean;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMat4Array;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformSampler;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec2;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec3;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec4;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformViewMatrix;
import eu.tankernn.gameEngine.settings.Settings;

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
	protected UniformSampler normalMap = new UniformSampler("normalMap");
	public UniformBoolean usesNormalMap = new UniformBoolean("usesNormalMap");
	protected UniformMat4Array jointTransforms = new UniformMat4Array("jointTransforms", Settings.MAX_JOINTS);
	protected UniformBoolean animated = new UniformBoolean("isAnimated");

	public EntityShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "textureCoords", "normal", "tangent", "in_jointIndices", "in_weights");
		super.getLightUniformLocations();
		super.storeAllUniformLocations(transformationMatrix, projectionMatrix, viewMatrix, shineDamper, reflectivity,
				refractivity, useFakeLighting, skyColor, numberOfRows, offset, plane, toShadowMapSpace, shadowMap,
				specularMap, usesSpecularMap, modelTexture, cameraPosition, enviroMap, normalMap, usesNormalMap,
				jointTransforms, animated);
	}

	public EntityShader(String vertexFile, String fragmentFile, String... string) {
		super(vertexFile, fragmentFile, string);
	}

	public void loadLights(List<ILight> lights, Matrix4f viewMatrix) {
		super.loadLights(lights);
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				lightPositionEyeSpace[i].loadVec3(getEyeSpacePosition(lights.get(i), viewMatrix));
			} else {
				lightPositionEyeSpace[i].loadVec3(new Vector3f(0, 0, 0));
			}
		}
	}

	protected void connectTextureUnits() {
		shadowMap.loadTexUnit(5);
		modelTexture.loadTexUnit(0);
		normalMap.loadTexUnit(1);
		specularMap.loadTexUnit(2);
		enviroMap.loadTexUnit(10);
	}

	private Vector3f getEyeSpacePosition(ILight light, Matrix4f viewMatrix) {
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x, position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos);
	}
}
