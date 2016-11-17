package eu.tankernn.gameEngine.renderEngine.entities;

import static eu.tankernn.gameEngine.settings.Settings.BLUE;
import static eu.tankernn.gameEngine.settings.Settings.GREEN;
import static eu.tankernn.gameEngine.settings.Settings.RED;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.util.ICamera;
import eu.tankernn.gameEngine.util.Maths;

/**
 * Renderer for entities.
 * 
 * @author Frans
 *
 */
public class EntityRenderer {
	private EntityShader shader;

	/**
	 * Starts shader and loads initial values.
	 * 
	 * @param shader
	 *            The shader to use when rendering entities
	 * @param projectionMatrix
	 *            The projection matrix to use when rendering entities
	 */
	public EntityRenderer(Matrix4f projectionMatrix) {
		this.shader = new EntityShader();
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	/**
	 * Renders entities to the current frame buffer.
	 * 
	 * @param entities
	 *            The entities to render.
	 * @param toShadowSpace
	 *            Transformation matrix to shadow space. Used for applying
	 *            shadows.
	 */
	public void render(Map<TexturedModel, List<Entity>> entities, Matrix4f toShadowSpace, ICamera cam,
			Vector4f clipPlane, List<Light> lights, Texture environmentMap) {
		shader.start();
		shader.plane.loadVec4(clipPlane);
		shader.skyColor.loadVec3(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.viewMatrix.loadCamera(cam);

		shader.toShadowMapSpace.loadMatrix(toShadowSpace);
		shader.cameraPosition.loadVec3(cam.getPosition());
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model, environmentMap);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private void prepareTexturedModel(TexturedModel model, Texture environmentMap) {
		GL30.glBindVertexArray(model.getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.numberOfRows.loadFloat(texture.getNumberOfRows());
		if (texture.hasTransparency())
			MasterRenderer.disableCulling();
		shader.useFakeLighting.loadBoolean(texture.isUseFakeLighting());
		shader.shineDamper.loadFloat(texture.getShineDamper());
		shader.reflectivity.loadFloat(texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		shader.usesSpecularMap.loadBoolean(texture.hasSpecularMap());
		if (texture.hasSpecularMap()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getSpecularMap());
		}
		bindEnvironmentMap(environmentMap);
	}

	private void bindEnvironmentMap(Texture environmentMap) {
		environmentMap.bindToUnit(10);
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.transformationMatrix.loadMatrix(transformationMatrix);
		shader.offset.loadVec2(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
}
