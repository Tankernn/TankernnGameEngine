package eu.tankernn.gameEngine.renderEngine.shadows;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.renderEngine.Vao;
import eu.tankernn.gameEngine.util.Maths;

public class ShadowMapEntityRenderer {
	
	private Matrix4f projectionViewMatrix;
	private ShadowShader shader;
	
	/**
	 * @param shader - the simple shader program being used for the shadow
	 *        render pass.
	 * @param projectionViewMatrix - the orthographic projection matrix
	 *        multiplied by the light's "view" matrix.
	 */
	protected ShadowMapEntityRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}
	
	/**
	 * Renders entieis to the shadow map. Each model is first bound and then all
	 * of the entities using that model are rendered to the shadow map.
	 * 
	 * @param entities - the entities to be rendered to the shadow map.
	 */
	protected void render(Map<TexturedModel, List<Entity3D>> entities) {
		for (TexturedModel model: entities.keySet()) {
			Vao rawModel = model.getModel();
			bindModel(rawModel);
			model.getTexture().getTexture().bindToUnit(0);
			if (model.getTexture().hasTransparency()) {
				MasterRenderer.disableCulling();
			}
			for (Entity3D entity: entities.get(model)) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			if (model.getTexture().hasTransparency()) {
				MasterRenderer.enableCulling();
			}
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Binds a raw model before rendering. Only the attribute 0 and 1 are
	 * enabled here because that is where the positions and texture coordinates
	 * are stored in the VAO. The texture coordinates are used to determine
	 * whether the entity is transparent or not.
	 * 
	 * @param rawModel - the model to be bound.
	 */
	private void bindModel(Vao rawModel) {
		rawModel.bind(0, 1);
	}
	
	/**
	 * Prepares an entity to be rendered. The model matrix is created in the
	 * usual way and then multiplied with the projection and view matrix (often
	 * in the past we've done this in the vertex shader) to create the
	 * mvp-matrix. This is then loaded to the vertex shader as a uniform.
	 * 
	 * @param entity - the entity to be prepared for rendering.
	 */
	private void prepareInstance(Entity3D entity) {
		Vector3f rot = entity.getRotation();
		Matrix4f modelMatrix = Maths.createTransformationMatrix(entity.getPosition(), rot.x, rot.y, rot.z, entity.getScale());
		Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
		shader.mvpMatrix.loadMatrix(mvpMatrix);
	}
	
}
