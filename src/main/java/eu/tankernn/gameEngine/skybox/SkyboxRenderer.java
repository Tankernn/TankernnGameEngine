package eu.tankernn.gameEngine.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.models.RawModel;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.renderEngine.Loader;

public class SkyboxRenderer {
	private static final float SIZE = 500f;
	private static final int DAY_LENGTH = 24000;
	
	private static final float[] VERTICES = {
			-SIZE, SIZE, -SIZE,
			-SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,
			
			-SIZE, -SIZE, SIZE,
			-SIZE, -SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,
			-SIZE, SIZE, -SIZE,
			-SIZE, SIZE, SIZE,
			-SIZE, -SIZE, SIZE,
			
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			
			-SIZE, -SIZE, SIZE,
			-SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			SIZE, -SIZE, SIZE,
			-SIZE, -SIZE, SIZE,
			
			-SIZE, SIZE, -SIZE,
			SIZE, SIZE, -SIZE,
			SIZE, SIZE, SIZE,
			SIZE, SIZE, SIZE,
			-SIZE, SIZE, SIZE,
			-SIZE, SIZE, -SIZE,
			
			-SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE, SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE, SIZE,
			SIZE, -SIZE, SIZE
	};
	
	private static String[] TEXTURE_FILES = {"alps_rt", "alps_lf", "alps_up", "alps_dn", "alps_bk", "alps_ft"};
	private static String[] NIGHT_TEXTURE_FILES = {"midnight_rt", "midnight_lf", "midnight_up", "midnight_dn", "midnight_bk", "midnight_ft"};
	
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	private float time = 0;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionmatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES, "skybox/");
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES, "skybox/");
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
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures(){ //TODO make day/night cycle better
		time += DisplayManager.getFrameTimeSeconds() * 1000;
		time %= DAY_LENGTH;
		
		int morning = (int) (DAY_LENGTH/4.8);
		int noon = (int) (DAY_LENGTH/3);
		int evening = (int) (DAY_LENGTH/1.14);
		
		int texture1;
		int texture2;
		float blendFactor;
		if(time >= 0 && time < morning){
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = (time - 0)/(morning - 0);
		}else if(time >= morning && time < noon){
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = (time - morning)/(noon - morning);
		}else if(time >= noon && time < evening){
			texture1 = texture;
			texture2 = texture;
			blendFactor = (time - noon)/(evening - noon);
		}else{
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = (time - evening)/(DAY_LENGTH - evening);
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
}
