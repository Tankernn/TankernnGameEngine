package eu.tankernn.gameEngine.renderEngine.entities;

import static eu.tankernn.gameEngine.settings.Settings.BLUE;
import static eu.tankernn.gameEngine.settings.Settings.GREEN;
import static eu.tankernn.gameEngine.settings.Settings.RED;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.animation.model.AnimatedModel;
import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.entities.ILight;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.util.ICamera;
import eu.tankernn.gameEngine.util.Maths;
import eu.tankernn.gameEngine.util.OpenGlUtils;

/**
 * Renderer for entities.
 * 
 * @author Frans
 */
public class EntityRenderer<S extends EntityShader> {
	protected S shader;
	
	/**
	 * Starts shader and loads initial values.
	 * 
	 * @param shader The shader to use when rendering entities
	 * @param projectionMatrix The projection matrix to use when rendering
	 *        entities
	 */
	@SuppressWarnings("unchecked")
	public EntityRenderer(Matrix4f projectionMatrix) {
		this((S) new EntityShader(), projectionMatrix);
	}
	
	protected EntityRenderer(S shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
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
	public void render(Map<TexturedModel, List<Entity3D>> entities, Matrix4f toShadowSpace, ICamera cam, Vector4f clipPlane, List<ILight> lights, Texture environmentMap) {
		OpenGlUtils.antialias(true);
		OpenGlUtils.disableBlending();
		OpenGlUtils.enableDepthTesting(true);
		shader.start();
		shader.plane.loadVec4(clipPlane);
		shader.skyColor.loadVec3(RED, GREEN, BLUE);
		shader.loadLights(lights, cam.getViewMatrix());
		shader.viewMatrix.loadCamera(cam);
		
		shader.toShadowMapSpace.loadMatrix(toShadowSpace);
		shader.cameraPosition.loadVec3(cam.getPosition());
		for (TexturedModel model: entities.keySet()) {
			prepareTexturedModel(model, environmentMap);
			List<Entity3D> batch = entities.get(model);
			for (Entity3D entity: batch) {
				prepareInstance(entity, model);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel(model);
			
		}
		shader.stop();
	}
	
	public void finalize() {
		shader.finalize();
	}
	
	private void prepareTexturedModel(TexturedModel model, Texture environmentMap) {
		if (model instanceof AnimatedModel) {
			model.getModel().bind(0, 1, 2, 4, 5);
			shader.animated.loadBoolean(true);
			shader.jointTransforms.loadMatrixArray(((AnimatedModel) model).getJointTransforms());
		} else {
			model.getModel().bind(0, 1, 2, 3);
			shader.animated.loadBoolean(false);
		}
		ModelTexture texture = model.getTexture();
		shader.numberOfRows.loadFloat(texture.getNumberOfRows());
		if (texture.hasTransparency())
			MasterRenderer.disableCulling();
		shader.useFakeLighting.loadBoolean(texture.isUseFakeLighting());
		shader.shineDamper.loadFloat(texture.getShineDamper());
		shader.reflectivity.loadFloat(texture.getReflectivity());
		shader.refractivity.loadFloat(texture.getRefractivity());
		model.getTexture().getTexture().bindToUnit(0);
		shader.usesSpecularMap.loadBoolean(texture.hasSpecularMap());
		if (texture.hasSpecularMap()) {
			texture.getSpecularMap().bindToUnit(2);
		}
		shader.usesNormalMap.loadBoolean(texture.hasNormalMap());
		if (texture.hasNormalMap())
			texture.getNormalMap().bindToUnit(1);
		bindEnvironmentMap(environmentMap);
	}
	
	private void bindEnvironmentMap(Texture environmentMap) {
		environmentMap.bindToUnit(10);
	}
	
	private void unbindTexturedModel(TexturedModel model) {
		MasterRenderer.enableCulling();
		if (model instanceof AnimatedModel)
			model.getModel().unbind(0, 1, 2, 4, 5);
		else
			model.getModel().unbind(0, 1, 2, 3);
	}
	
	protected void prepareInstance(Entity3D entity, TexturedModel model) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		shader.transformationMatrix.loadMatrix(transformationMatrix);
		shader.offset.loadVec2(model.getTexture().getTexture().getXOffset(), model.getTexture().getTexture().getYOffset());
	}
}
