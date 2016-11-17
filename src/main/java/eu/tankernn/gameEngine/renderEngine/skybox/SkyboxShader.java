package eu.tankernn.gameEngine.renderEngine.skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.renderEngine.shaders.ShaderProgram;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformFloat;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformMatrix;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformSampler;
import eu.tankernn.gameEngine.renderEngine.shaders.UniformVec3;
import eu.tankernn.gameEngine.util.ICamera;

public class SkyboxShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/eu/tankernn/gameEngine/skybox/skyboxVertexShader.glsl";
	private static final String FRAGMENT_FILE = "/eu/tankernn/gameEngine/skybox/skyboxFragmentShader.glsl";
	
	private static final float ROTATE_SPEED = 1f;
	
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	protected UniformVec3 fogColor = new UniformVec3("fogColor");
	protected UniformSampler cubeMap = new UniformSampler("cubeMap");
	protected UniformSampler cubeMap2 = new UniformSampler("cubeMap2");
	protected UniformFloat blendFactor = new UniformFloat("blendFactor");
	
	private float rotation = 0;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position");
		super.storeAllUniformLocations(projectionMatrix, viewMatrix, fogColor, cubeMap, cubeMap2, blendFactor);
	}
	
	public void loadViewMatrix(ICamera camera) {
		Matrix4f matrix = camera.getViewMatrix();
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
		viewMatrix.loadMatrix(matrix);
	}
	
	public void connectTextureUnits() {
		cubeMap.loadTexUnit(0);
		cubeMap2.loadTexUnit(1);
	}
	
}