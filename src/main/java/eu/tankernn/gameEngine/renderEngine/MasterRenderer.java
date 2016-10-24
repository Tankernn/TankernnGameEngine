package eu.tankernn.gameEngine.renderEngine;

import static eu.tankernn.gameEngine.settings.Settings.BLUE;
import static eu.tankernn.gameEngine.settings.Settings.FAR_PLANE;
import static eu.tankernn.gameEngine.settings.Settings.FOV;
import static eu.tankernn.gameEngine.settings.Settings.GREEN;
import static eu.tankernn.gameEngine.settings.Settings.NEAR_PLANE;
import static eu.tankernn.gameEngine.settings.Settings.RED;
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
import eu.tankernn.gameEngine.skybox.CubeMap;
import eu.tankernn.gameEngine.skybox.SkyboxRenderer;
import eu.tankernn.gameEngine.terrains.Terrain;

/**
 * Handles most of the rendering in the game.
 * 
 * @author Frans
 */
public class MasterRenderer {
	private StaticShader staticShader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();
	
	private EntityRenderer entityRenderer;
	private TerrainRenderer terrainRenderer;
	private SkyboxRenderer skyboxRenderer;
	private NormalMappingRenderer normalMapRenderer;
	private ShadowMapMasterRenderer shadowMapRenderer;
	
	private Matrix4f projectionMatrix;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	/**
	 * Sets up most other renderers for rendering.
	 * 
	 * @param loader The main <code>Loader</code>, used by some other renderers
	 * @param camera The main <code>Camera</code>
	 */
	public MasterRenderer(Loader loader, Camera camera) {
		enableCulling();
		createProjectionMatrix();
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
		entityRenderer = new EntityRenderer(staticShader, projectionMatrix, skyboxRenderer.getCubeMap());
	}
	
	/**
	 * Enables culling of faces facing away from the camera.
	 */
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	/**
	 * Disables culling of faces facing away from the camera. Used when
	 * rendering flat objects.
	 */
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	/**
	 * Renders a scene.
	 * 
	 * @param scene The <code>Scene</code> to render.
	 * @param clipPlane The clip plane.
	 */
	public void renderScene(Scene scene, Vector4f clipPlane) {
		scene.getTerrainPack().prepareRenderTerrains(this);
		for (Entity e: scene.getEntities()) {
			processEntity(e);
		}
		for (Entity e: scene.getNormalEntities()) {
			processNormalMappedEntity(e);
		}
		render(scene.getLights(), scene.getCamera(), clipPlane);
	}
	
	/**
	 * Renders the current scene to the current buffer.
	 * 
	 * @param lights List of lights in the scene.
	 * @param camera The main camera.
	 * @param clipPlane The clip plane.
	 */
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		prepare();
		staticShader.start();
		staticShader.loadClipPlane(clipPlane);
		staticShader.loadSkyColor(RED, GREEN, BLUE);
		staticShader.loadLights(lights);
		staticShader.loadViewMatrix(camera);
		entityRenderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix(), camera);
		staticShader.stop();
		
		normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera);
		
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadShadowMapSize(ShadowMapMasterRenderer.SHADOW_MAP_SIZE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		
		entities.clear();
		terrains.clear();
		normalMapEntities.clear();
	}
	
	/**
	 * Adds an entity to the list of entities.
	 * 
	 * @param entity Entity to add to the list
	 */
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	/**
	 * Same as {@link #processEntity(Entity)}, but for normal-mapped entities.
	 * 
	 * @param entity Entity to add to the list
	 */
	public void processNormalMappedEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = normalMapEntities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalMapEntities.put(entityModel, newBatch);
		}
	}
	
	/**
	 * Adds specified terrain to the terrain list.
	 * 
	 * @param terrain Terrain object to add to list
	 */
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void renderShadowMap(List<Entity> entityList, Light sun) {
		for (Entity e: entityList) {
			processEntity(e);
		}
		shadowMapRenderer.render(entities, sun);
		entities.clear();
	}
	
	/**
	 * Gets the shadow map texture from the <code>shadowMapRenderer</code>.
	 * 
	 * @return
	 */
	public int getShadowMapTexture() {
		return shadowMapRenderer.getShadowMap();
	}
	
	/**
	 * Runs the cleanup method for the other renderers.
	 */
	public void cleanUp() {
		staticShader.cleanUp();
		terrainShader.cleanUp();
		normalMapRenderer.cleanUp();
		shadowMapRenderer.cleanUp();
	}
	
	/**
	 * Prepares the current buffer for rendering.
	 */
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
	/**
	 * Gets the current projection matrix.
	 * @return The current projection matrix
	 */
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
}
