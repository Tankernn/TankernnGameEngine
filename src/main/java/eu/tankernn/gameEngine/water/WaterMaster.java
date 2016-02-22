package eu.tankernn.gameEngine.water;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.renderEngine.Loader;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.renderEngine.Scene;

public class WaterMaster {
	private WaterFrameBuffers buffers = new WaterFrameBuffers();
	private WaterShader waterShader = new WaterShader();
	private List<WaterTile> waterTiles = new ArrayList<WaterTile>();
	private WaterRenderer waterRenderer;
	
	public WaterMaster(Loader loader, MasterRenderer renderer) {
		waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
	}
	
	public void addWaterTile(WaterTile water) {
		waterTiles.add(water);
	}
	
	public void renderBuffers(MasterRenderer renderer, Scene scene) {
		float waterHeight = waterTiles.get(0).getHeight(); //TODO Using only the first watertile is BAD
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		
		// Reflection
		buffers.bindReflectionFrameBuffer();
		float distance = 2 * (scene.getCamera().getPosition().y - waterHeight);
		scene.getCamera().getPosition().y -= distance;
		scene.getCamera().invertPitch();
		scene.getCamera().invertRoll();
		renderer.renderScene(scene, new Vector4f(0, 1, 0, -waterHeight));
		scene.getCamera().getPosition().y += distance;
		scene.getCamera().invertPitch();
		scene.getCamera().invertRoll();
		
		// Refraction
		buffers.bindRefractionFrameBuffer();
		renderer.renderScene(scene, new Vector4f(0, -1, 0, waterHeight));
		
		// Screen
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		buffers.unbindCurrentFrameBuffer();
	}
	
	public void renderWater(Camera camera, List<Light> lights) {
		waterRenderer.render(waterTiles, camera, lights);
	}
	
	public void cleanUp() {
		buffers.cleanUp();
		waterShader.cleanUp();
	}

	public WaterFrameBuffers getBuffers() {
		return buffers;
	}
}
