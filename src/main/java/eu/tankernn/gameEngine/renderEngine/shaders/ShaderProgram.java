package eu.tankernn.gameEngine.renderEngine.shaders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.Light;

public class ShaderProgram {

	public static final int MAX_LIGHTS = 4;

	private int programID;

	protected UniformVec3[] lightPosition;
	protected UniformVec3[] lightPositionEyeSpace;
	protected UniformVec3[] lightColor;
	protected UniformVec3[] attenuation;

	public ShaderProgram(String vertexFile, String fragmentFile, String... inVariables) {
		int vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		int fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes(inVariables);
		GL20.glLinkProgram(programID);
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
	}

	protected void getLightUniformLocations() {
		lightPosition = new UniformVec3[MAX_LIGHTS];
		lightPositionEyeSpace = new UniformVec3[MAX_LIGHTS];
		lightColor = new UniformVec3[MAX_LIGHTS];
		attenuation = new UniformVec3[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			lightPositionEyeSpace[i] = new UniformVec3("lightPositionEyeSpace[" + i + "]");
			lightPosition[i] = new UniformVec3("lightPosition[" + i + "]");
			lightColor[i] = new UniformVec3("lightColor[" + i + "]");
			attenuation[i] = new UniformVec3("attenuation[" + i + "]");
		}
	}

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				lightPosition[i].loadVec3(lights.get(i).getPosition());
				lightColor[i].loadVec3(lights.get(i).getColor());
				attenuation[i].loadVec3(lights.get(i).getAttenuation());
			} else {
				lightPosition[i].loadVec3(new Vector3f(0, 0, 0));
				lightColor[i].loadVec3(new Vector3f(0, 0, 0));
				attenuation[i].loadVec3(new Vector3f(1, 0, 0));
			}
		}
	}

	protected void storeAllUniformLocations(Uniform... uniforms) {
		if (lightPosition != null)
			uniforms = ArrayUtils.addAll(uniforms, lightPosition);
		if (lightColor != null)
			uniforms = ArrayUtils.addAll(uniforms, lightColor);
		if (attenuation != null)
			uniforms = ArrayUtils.addAll(uniforms, attenuation);

		for (Uniform uniform : uniforms) {
			uniform.storeUniformLocation(programID);
		}
		GL20.glValidateProgram(programID);
	}

	public void start() {
		GL20.glUseProgram(programID);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		GL20.glDeleteProgram(programID);
	}

	private void bindAttributes(String[] inVariables) {
		for (int i = 0; i < inVariables.length; i++) {
			bindAttribute(inVariables[i], i);
		}
	}

	protected void bindAttribute(String variable, int attributeId) {
		GL20.glBindAttribLocation(programID, attributeId, variable);
	}

	private int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(ShaderProgram.class.getResourceAsStream(file)));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		} catch (Exception e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader " + file);
			System.exit(-1);
		}
		return shaderID;
	}

}
