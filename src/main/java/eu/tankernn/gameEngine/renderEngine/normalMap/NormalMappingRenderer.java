package eu.tankernn.gameEngine.renderEngine.normalMap;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.renderEngine.RawModel;
import eu.tankernn.gameEngine.settings.Settings;
import eu.tankernn.gameEngine.util.ICamera;
import eu.tankernn.gameEngine.util.Maths;

public class NormalMappingRenderer {

	private NormalMappingShader shader;

	public NormalMappingRenderer(Matrix4f projectionMatrix) {
		this.shader = new NormalMappingShader();
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities, Vector4f clipPlane, List<Light> lights,
			ICamera camera) {
		shader.start();
		prepare(clipPlane, lights, camera);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel(model);
		}
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		rawModel.bind(0, 1, 2, 3);
		ModelTexture texture = model.getModelTexture();
		shader.numberOfRows.loadFloat(texture.getNumberOfRows());
		if (texture.hasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.shineDamper.loadFloat(texture.getShineDamper());
		shader.reflectivity.loadFloat(texture.getReflectivity());
		model.getModelTexture().getTexture().bindToUnit(0);
		model.getModelTexture().getNormalMap().bindToUnit(1);
		shader.usesSpecularMap.loadBoolean(texture.hasSpecularMap());
		if (texture.hasSpecularMap()) {
			texture.getSpecularMap().bindToUnit(2);
		}
	}

	private void unbindTexturedModel(TexturedModel model) {
		MasterRenderer.enableCulling();
		model.getRawModel().unbind(0, 1, 2, 3);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.transformationMatrix.loadMatrix(transformationMatrix);
		shader.offset.loadVec2(entity.getTextureXOffset(), entity.getTextureYOffset());
	}

	private void prepare(Vector4f clipPlane, List<Light> lights, ICamera camera) {
		shader.plane.loadVec4(clipPlane);
		// need to be public variables in MasterRenderer
		shader.skyColour.loadVec3(Settings.RED, Settings.GREEN, Settings.BLUE);
		Matrix4f viewMatrix = camera.getViewMatrix();

		shader.loadLights(lights, viewMatrix);
		shader.viewMatrix.loadMatrix(viewMatrix);
	}

}