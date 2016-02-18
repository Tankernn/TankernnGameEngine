package eu.tankernn.gameEngine.renderEngine;

import static eu.tankernn.gameEngine.renderEngine.Settings.BLUE;
import static eu.tankernn.gameEngine.renderEngine.Settings.FAR_PLANE;
import static eu.tankernn.gameEngine.renderEngine.Settings.FOV;
import static eu.tankernn.gameEngine.renderEngine.Settings.GREEN;
import static eu.tankernn.gameEngine.renderEngine.Settings.NEAR_PLANE;
import static eu.tankernn.gameEngine.renderEngine.Settings.RED;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.models.TexturedModel;
import eu.tankernn.gameEngine.normalMapping.renderer.NormalMappingRenderer;
import eu.tankernn.gameEngine.shaders.StaticShader;
import eu.tankernn.gameEngine.shaders.TerrainShader;
import eu.tankernn.gameEngine.shadows.ShadowMapMasterRenderer;
import eu.tankernn.gameEngine.skybox.SkyboxRenderer;
import eu.tankernn.gameEngine.terrains.Terrain;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class MasterRenderer {
	private StaticShader staticShader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();
	
	private EntityRenderer entityRenderer;
	private TerrainRenderer terrainRenderer;
	private SkyboxRenderer skyboxRenderer;
	private NormalMappingRenderer normalMapRenderer;
	private ShadowMapMasterRenderer shadowMapRenderer;
	
	private Matrix4f projectionMatrix;
	
	private Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel,List<Entity>> normalMapEntities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	
	
	public MasterRenderer(Loader loader, Camera camera) {
		enableCulling();
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void renderScene(List<Entity> entities, List<Entity> normalEntities, TerrainPack terrainPack, List<Light> lights, Camera camera, Vector4f clipPlane) {
		terrainPack.prepareRenderTerrains(this);
		for (Entity e : entities) {
			processEntity(e);
		}
		for (Entity e : normalEntities) {
			processNormalMappedEntity(e);
		}
		render(lights, camera, clipPlane);
	}
	
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		prepare();
		staticShader.start();
		staticShader.loadClipPlane(clipPlane);
		staticShader.loadSkyColor(RED, GREEN, BLUE);
		staticShader.loadLights(lights);
		staticShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		staticShader.stop();
		
		normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera);
		
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		
		entities.clear();
		terrains.clear();
		normalMapEntities.clear();
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch!=null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processNormalMappedEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = normalMapEntities.get(entityModel);
		if (batch!=null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalMapEntities.put(entityModel, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void renderShadowMap(List<Entity> entityList, Light sun) {
		for (Entity e : entityList) {
			processEntity(e);
		}
		shadowMapRenderer.render(entities, sun);
		entities.clear();
	}
	
	public int getShadowMapTexture() {
		return shadowMapRenderer.getShadowMap();
	}
	
	public void cleanUp() {
		staticShader.cleanUp();
		terrainShader.cleanUp();
		normalMapRenderer.cleanUp();
		shadowMapRenderer.cleanUp();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * FAR_PLANE * NEAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
}
