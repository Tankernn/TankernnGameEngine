package eu.tankernn.gameEngine.renderEngine.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.renderEngine.RawModel;
import eu.tankernn.gameEngine.util.ICamera;

public class SkyboxRenderer {
	private static final int DAY_LENGTH = 24000;
	
	private Skybox skybox;
	private SkyboxShader shader;
	private float time = 0;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionmatrix, Skybox skybox) {
		this.skybox = skybox;
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.projectionMatrix.loadMatrix(projectionmatrix);
		shader.stop();
	}
	
	public void render(ICamera camera, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.fogColor.loadVec3(r, g, b);
		skybox.getCubeVao().bind();
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		RawModel model = skybox.getCubeVao();
		model.bind(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		model.unbind(0);
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
		
		Texture texture1;
		Texture texture2;
		float blendFactor;
		if (time >= 0 && time < morning) {
			texture1 = skybox.getNightTexture();
			texture2 = skybox.getNightTexture();
			blendFactor = (time - 0) / (morning - 0);
		} else if (time >= morning && time < noon) {
			texture1 = skybox.getNightTexture();
			texture2 = skybox.getDayTexture();
			blendFactor = (time - morning) / (noon - morning);
		} else if (time >= noon && time < evening) {
			texture1 = skybox.getDayTexture();
			texture2 = skybox.getDayTexture();
			blendFactor = (time - noon) / (evening - noon);
		} else {
			texture1 = skybox.getDayTexture();
			texture2 = skybox.getNightTexture();
			blendFactor = (time - evening) / (DAY_LENGTH - evening);
		}
		
		texture1.bindToUnit(0);
		texture2.bindToUnit(1);
		shader.blendFactor.loadFloat(blendFactor);
	}
}
