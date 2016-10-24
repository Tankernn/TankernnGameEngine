package eu.tankernn.gameEngine.renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.models.TexturedModel;
import eu.tankernn.gameEngine.shaders.StaticShader;
import eu.tankernn.gameEngine.skybox.CubeMap;
import eu.tankernn.gameEngine.textures.ModelTexture;
import eu.tankernn.gameEngine.util.Maths;
/**
 * Renderer for entities.
 * @author Frans
 *
 */
public class EntityRenderer {
	private StaticShader shader;
	private CubeMap environmentMap;
	
	/**
	 * Starts shader and loads initial values.
	 * @param shader The shader to use when rendering entities
	 * @param projectionMatrix The projection matrix to use when rendering entities
	 */
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix, CubeMap environmentMap) {
		this.shader = shader;
		this.environmentMap = environmentMap;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	/**
	 * Renders entities to the current frame buffer.
	 * 
	 * @param entities The entities to render.
	 * @param toShadowSpace Transformation matrix to shadow space. Used for
	 *        applying shadows.
	 */
	public void render(Map<TexturedModel, List<Entity>> entities, Matrix4f toShadowSpace, Camera cam) {
		shader.loadToShadowSpaceMatrix(toShadowSpace);
		shader.loadCameraPosition(cam.getPosition());
		for (TexturedModel model: entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity: batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		GL30.glBindVertexArray(model.getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if (texture.hasTransparency())
			MasterRenderer.disableCulling();
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		shader.loadUseSpecularMap(texture.hasSpecularMap());
		if (texture.hasSpecularMap()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getSpecularMap());
		}
		bindEnvironmentMap();
	}
	
	private void bindEnvironmentMap() {
		GL13.glActiveTexture(GL13.GL_TEXTURE10);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, environmentMap.getTexture());
	}
	
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
}
