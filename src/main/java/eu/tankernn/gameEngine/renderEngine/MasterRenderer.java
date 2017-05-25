package eu.tankernn.gameEngine.renderEngine;

import static eu.tankernn.gameEngine.settings.Settings.BLUE;
import static eu.tankernn.gameEngine.settings.Settings.GREEN;
import static eu.tankernn.gameEngine.settings.Settings.RED;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.entities.ILight;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.entities.EntityRenderer;
import eu.tankernn.gameEngine.renderEngine.entities.EntityShader;
import eu.tankernn.gameEngine.renderEngine.shadows.ShadowMapMasterRenderer;
import eu.tankernn.gameEngine.renderEngine.skybox.Skybox;
import eu.tankernn.gameEngine.renderEngine.skybox.SkyboxRenderer;
import eu.tankernn.gameEngine.renderEngine.terrain.TerrainRenderer;
import eu.tankernn.gameEngine.terrains.Terrain;
import eu.tankernn.gameEngine.util.ICamera;

/**
 * Handles most of the rendering in the game.
 * 
 * @author Frans
 */
public class MasterRenderer {
	private static final Vector4f NO_CLIP = new Vector4f(0, 0, 0, 1);
	
	private EntityRenderer<EntityShader> entityRenderer;
	private TerrainRenderer terrainRenderer;
	private SkyboxRenderer skyboxRenderer;
	private ShadowMapMasterRenderer shadowMapRenderer;
	
	private Map<TexturedModel, List<Entity3D>> entities = new HashMap<TexturedModel, List<Entity3D>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	/**
	 * Sets up most other renderers for rendering.
	 * 
	 * @param loader
	 *            The main <code>Loader</code>, used by some other renderers
	 * @param camera
	 *            The main <code>Camera</code>
	 */
	public MasterRenderer(Loader loader, Camera camera, Skybox skybox) {
		enableCulling();
		terrainRenderer = new TerrainRenderer(camera.getProjectionMatrix());
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
		skyboxRenderer = new SkyboxRenderer(loader, camera.getProjectionMatrix(), skybox);
		entityRenderer = new EntityRenderer<EntityShader>(camera.getProjectionMatrix());
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
	 * @param scene
	 *            The <code>Scene</code> to render.
	 * @param clipPlane
	 *            The clip plane.
	 */
	public void renderScene(Scene scene, Vector4f clipPlane) {
		prepareScene(scene);
		render(scene.getLights(), scene.getCamera(), clipPlane, scene.getEnvironmentMap());
	}
	
	public void renderLowQualityScene(Scene scene, ICamera camera) {
		prepareScene(scene);
		prepareBuffer();
		entityRenderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix(), camera, NO_CLIP, scene.getLights(), scene.getEnvironmentMap());
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix(), camera, NO_CLIP, scene.getLights());
	}

	/**
	 * Renders the current scene to the current buffer.
	 * 
	 * @param lights
	 *            List of lights in the scene.
	 * @param camera
	 *            The main camera.
	 * @param clipPlane
	 *            The clip plane.
	 */
	public void render(List<ILight> lights, ICamera camera, Vector4f clipPlane, Texture environmentMap) {
		prepareBuffer();
		entityRenderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix(), camera, clipPlane, lights, environmentMap);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix(), camera, clipPlane, lights);
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
	}
	
	private void prepareScene(Scene scene) {
		entities.clear();
		terrains.clear();
		for (Terrain t : scene.getTerrainPack().getTerrains()) {
			processTerrain(t);
		}
		for (Entity3D e : scene.getEntities()) {
			processEntity(e);
		}
	}

	/**
	 * Adds an entity to the list of entities.
	 * 
	 * @param entity
	 *            Entity to add to the list
	 */
	public void processEntity(Entity3D entity) {
		TexturedModel entityModel = entity.getModel();
		if (entityModel == null)
			return;
		List<Entity3D> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity3D> newBatch = new ArrayList<Entity3D>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	/**
	 * Adds specified terrain to the terrain list.
	 * 
	 * @param terrain
	 *            Terrain object to add to list
	 */
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void renderShadowMap(List<Entity3D> entityList, Light sun) {
		for (Entity3D e : entityList) {
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
	public Texture getShadowMapTexture() {
		return shadowMapRenderer.getShadowMap();
	}

	/**
	 * Runs the cleanup method for the other renderers.
	 */
	@Override
	public void finalize() {
		entityRenderer.finalize();
		terrainRenderer.finalize();
		shadowMapRenderer.finalize();
	}

	/**
	 * Prepares the current buffer for rendering.
	 */
	public void prepareBuffer() {
		GL11.glEnable(GL11.GL_DEPTH_TEST | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
																	// framebuffer
		getShadowMapTexture().bindToUnit(5);
	}
}
