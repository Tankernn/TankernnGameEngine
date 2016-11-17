package eu.tankernn.gameEngine.renderEngine.normalMap;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformBoolean;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformSampler;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec2;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec3;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec4;

public class NormalMappingShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/normalMapping/renderer/normalMapVShader.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/normalMapping/renderer/normalMapFShader.glsl";

	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	private UniformVec3[] lightPositionEyeSpace;
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformVec3 skyColour = new UniformVec3("skyColour");
	protected UniformFloat numberOfRows = new UniformFloat("numberOfRows");
	protected UniformVec2 offset = new UniformVec2("offset");
	protected UniformVec4 plane = new UniformVec4("plane");
	protected UniformSampler modelTexture = new UniformSampler("modelTexture");
	protected UniformSampler normalMap = new UniformSampler("normalMap");
	protected UniformSampler specularMap = new UniformSampler("specularMap");
	protected UniformBoolean usesSpecularMap = new UniformBoolean("usesSpecularMap");

	public NormalMappingShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "textureCoordinates", "normal", "tangent");
		this.getLightUniformLocations();
		super.storeAllUniformLocations(transformationMatrix, projectionMatrix, viewMatrix, viewMatrix, shineDamper,
				reflectivity, skyColour, numberOfRows, offset, plane, modelTexture, normalMap, specularMap,
				usesSpecularMap);
		super.storeAllUniformLocations(lightPositionEyeSpace);
	}
	
	@Override
	protected void getLightUniformLocations() {
		lightPositionEyeSpace = new UniformVec3[MAX_LIGHTS];
		lightColor = new UniformVec3[MAX_LIGHTS];
		attenuation = new UniformVec3[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			lightPositionEyeSpace[i] = new UniformVec3("lightPositionEyeSpace[" + i + "]");
			lightColor[i] = new UniformVec3("lightColor[" + i + "]");
			attenuation[i] = new UniformVec3("attenuation[" + i + "]");
		}
	}
	
	public void loadLights(List<Light> lights, Matrix4f viewMatrix) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				lightPositionEyeSpace[i].loadVec3(getEyeSpacePosition(lights.get(i), viewMatrix));
				lightColor[i].loadVec3(lights.get(i).getColor());
				attenuation[i].loadVec3(lights.get(i).getAttenuation());
			} else {
				lightPositionEyeSpace[i].loadVec3(new Vector3f(0, 0, 0));
				lightColor[i].loadVec3(new Vector3f(0, 0, 0));
				attenuation[i].loadVec3(new Vector3f(1, 0, 0));
			}
		}
	}
	
	@Override
	public void loadLights(List<Light> lights) {
		throw new NullPointerException("Use loadLights(List<Light> lights, Matrix4f viewMatrix) instead.");
	}

	protected void connectTextureUnits() {
		modelTexture.loadTexUnit(0);
		normalMap.loadTexUnit(1);
		specularMap.loadTexUnit(2);
	}

	private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix) {
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x, position.y, position.z, 1f);
		Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
		return new Vector3f(eyeSpacePos);
	}

}
