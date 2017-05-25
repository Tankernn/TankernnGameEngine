package eu.tankernn.gameEngine.renderEngine.water;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.ILight;
import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.renderEngine.Scene;
import eu.tankernn.gameEngine.util.DistanceSorter;
import eu.tankernn.gameEngine.util.ICamera;

public class WaterMaster {
	private WaterFrameBuffers buffers = new WaterFrameBuffers();
	private WaterShader waterShader = new WaterShader();
	private List<WaterTile> waterTiles = new ArrayList<WaterTile>();
	private WaterRenderer waterRenderer;
	
	public WaterMaster(Loader loader, Texture dudvTexture, Texture normalMap, ICamera camera) {
		waterRenderer = new WaterRenderer(loader, dudvTexture, normalMap, waterShader, camera.getProjectionMatrix(), buffers);
	}
	
	public void addWaterTile(WaterTile water) {
		waterTiles.add(water);
	}
	
	public void renderBuffers(MasterRenderer renderer, Scene scene) {
		DistanceSorter.sort(waterTiles, scene.getCamera());
		float waterHeight = waterTiles.get(0).getHeight();
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		
		// Reflection
		buffers.getReflectionFbo().bindFrameBuffer();
		float distance = 2 * (scene.getCamera().getPosition().y - waterHeight);
		scene.getCamera().getPosition().y -= distance;
		scene.getCamera().invertPitch();
		scene.getCamera().invertRoll();
		renderer.renderScene(scene, new Vector4f(0, 1, 0, -waterHeight));
		scene.getCamera().getPosition().y += distance;
		scene.getCamera().invertPitch();
		scene.getCamera().invertRoll();
		
		// Refraction
		buffers.getRefractionFbo().bindFrameBuffer();
		renderer.renderScene(scene, new Vector4f(0, -1, 0, waterHeight + 1f));
		
		// Screen
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		buffers.getReflectionFbo().unbindFrameBuffer();
	}
	
	public boolean isPointUnderWater(Vector3f point) {
		for (WaterTile tile : waterTiles) {
			if (point.y < tile.getHeight()) {
				if (tile.getX() - tile.getSize().x <= point.x && point.x <= tile.getX() + tile.getSize().x) {
					if (tile.getZ() - tile.getSize().z <= point.z && point.z <= tile.getZ() + tile.getSize().z) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void renderWater(Camera camera, List<ILight> lights) {
		waterRenderer.render(waterTiles, camera, lights);
	}
	
	public void finalize() {
		buffers.finalize();
		waterShader.finalize();
	}

	public WaterFrameBuffers getBuffers() {
		return buffers;
	}
}
