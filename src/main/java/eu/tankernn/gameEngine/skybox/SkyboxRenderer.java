package eu.tankernn.gameEngine.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.renderEngine.Loader;

public class SkyboxRenderer {
	private static final int DAY_LENGTH = 24000;
	
	private CubeMap dayCube, nightCube;
	private SkyboxShader shader;
	private float time = 0;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionmatrix, String[] dayTextureFiles, String[] nightTextureFiles) {
		dayCube = new CubeMap(dayTextureFiles, "skybox/", loader);
		nightCube = new CubeMap(nightTextureFiles, "skybox/", loader);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionmatrix);
		shader.stop();
	}
	
	public void render(Camera camera, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColor(r, g, b);
		GL30.glBindVertexArray(dayCube.getCube().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, dayCube.getCube().getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures() {
		time += DisplayManager.getFrameTimeSeconds() * 1000;
		time %= DAY_LENGTH;
		
		int morning = (int) (DAY_LENGTH / 4.8);
		int noon = (int) (DAY_LENGTH / 3);
		int evening = (int) (DAY_LENGTH / 1.14);
		
		int texture1;
		int texture2;
		float blendFactor;
		if (time >= 0 && time < morning) {
			texture1 = nightCube.getTexture();
			texture2 = nightCube.getTexture();
			blendFactor = (time - 0) / (morning - 0);
		} else if (time >= morning && time < noon) {
			texture1 = nightCube.getTexture();
			texture2 = dayCube.getTexture();
			blendFactor = (time - morning) / (noon - morning);
		} else if (time >= noon && time < evening) {
			texture1 = dayCube.getTexture();
			texture2 = dayCube.getTexture();
			blendFactor = (time - noon) / (evening - noon);
		} else {
			texture1 = dayCube.getTexture();
			texture2 = nightCube.getTexture();
			blendFactor = (time - evening) / (DAY_LENGTH - evening);
		}
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
	
	public CubeMap getCubeMap() {
		return dayCube;
	}
}
