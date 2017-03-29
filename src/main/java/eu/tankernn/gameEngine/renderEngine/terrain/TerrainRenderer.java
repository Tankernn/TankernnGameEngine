package eu.tankernn.gameEngine.renderEngine.terrain;

import static eu.tankernn.gameEngine.settings.Settings.BLUE;
import static eu.tankernn.gameEngine.settings.Settings.GREEN;
import static eu.tankernn.gameEngine.settings.Settings.RED;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.loader.textures.TerrainTexturePack;
import eu.tankernn.gameEngine.renderEngine.shadows.ShadowBox;
import eu.tankernn.gameEngine.renderEngine.shadows.ShadowMapMasterRenderer;
import eu.tankernn.gameEngine.terrains.Terrain;
import eu.tankernn.gameEngine.util.ICamera;
import eu.tankernn.gameEngine.util.Maths;

public class TerrainRenderer {
	private TerrainShader shader;

	public TerrainRenderer(Matrix4f projectionMatrix) {
		shader = new TerrainShader();
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(List<Terrain> terrains, Matrix4f toShadowSpace, ICamera camera, Vector4f clipPlane,
			List<Light> lights) {
		shader.start();
		shader.plane.loadVec4(clipPlane);
		shader.skyColor.loadVec3(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.viewMatrix.loadCamera(camera);
		shader.shadowMapSize.loadFloat(ShadowMapMasterRenderer.SHADOW_MAP_SIZE);

		shader.toShadowMapSpace.loadMatrix(toShadowSpace);
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);

			unbindTexturedModel(terrain);
		}
		shader.stop();
	}

	public void finalize() {
		shader.finalize();
	}

	private void prepareTerrain(Terrain terrain) {
		terrain.getModel().bind(0, 1, 2);
		bindTexture(terrain);
		shader.shineDamper.loadFloat(1); // No shine
		shader.reflectivity.loadFloat(0); // No shine
		shader.shadowDistance.loadFloat(ShadowBox.SHADOW_DISTANCE);
	}

	private void bindTexture(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.getTexturePack();
		texturePack.getBackgroundTexture().bindToUnit(0);
		texturePack.getrTexture().bindToUnit(1);
		texturePack.getgTexture().bindToUnit(2);
		texturePack.getbTexture().bindToUnit(3);
		terrain.getBlendMap().bindToUnit(4);
	}

	private void unbindTexturedModel(Terrain terrain) {
		terrain.getModel().unbind(0, 1, 2);
	}

	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths
				.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		shader.transformationMatrix.loadMatrix(transformationMatrix);
	}
}
