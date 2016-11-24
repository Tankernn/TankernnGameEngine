package eu.tankernn.gameEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.entities.Player;
import eu.tankernn.gameEngine.entities.PlayerCamera;
import eu.tankernn.gameEngine.environmentMap.EnvironmentMapRenderer;
import eu.tankernn.gameEngine.font.meshCreator.FontType;
import eu.tankernn.gameEngine.font.meshCreator.GUIText;
import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.textures.TerrainTexturePack;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.particles.ParticleMaster;
import eu.tankernn.gameEngine.particles.ParticleSystem;
import eu.tankernn.gameEngine.particles.ParticleTexture;
import eu.tankernn.gameEngine.postProcessing.Fbo;
import eu.tankernn.gameEngine.postProcessing.MultisampleMultitargetFbo;
import eu.tankernn.gameEngine.postProcessing.PostProcessing;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.renderEngine.Scene;
import eu.tankernn.gameEngine.renderEngine.font.TextMaster;
import eu.tankernn.gameEngine.renderEngine.gui.GuiRenderer;
import eu.tankernn.gameEngine.renderEngine.gui.GuiTexture;
import eu.tankernn.gameEngine.renderEngine.skybox.Skybox;
import eu.tankernn.gameEngine.renderEngine.water.WaterMaster;
import eu.tankernn.gameEngine.renderEngine.water.WaterTile;
import eu.tankernn.gameEngine.settings.Settings;
import eu.tankernn.gameEngine.terrains.Terrain;
import eu.tankernn.gameEngine.terrains.TerrainPack;
import eu.tankernn.gameEngine.util.DistanceSorter;
import eu.tankernn.gameEngine.util.InternalFile;
import eu.tankernn.gameEngine.util.MousePicker;

public class MainLoop {

	private static final int SEED = 1235;

	// Skybox settings
	public static final String[] TEXTURE_FILES = { "alps_ft", "alps_bk", "alps_up", "alps_dn", "alps_rt", "alps_lf", };
	public static final String[] NIGHT_TEXTURE_FILES = { "midnight_ft", "midnight_bk", "midnight_up", "midnight_dn",
			"midnight_rt", "midnight_lf" };

	// Water settings
	public static final String DUDV_MAP = "waterDUDV.png";
	public static final String NORMAL_MAP = "waterNormalMap.png";

	public static final boolean DEBUG = true;

	static List<Entity> entities = new ArrayList<Entity>();
	static List<Entity> normalMapEntities = new ArrayList<Entity>();
	static List<Light> lights = new ArrayList<Light>();

	public static void main(String[] args) throws IOException {
		DisplayManager.createDisplay("Tankernn Game Engine tester");
		
		Loader loader = new Loader(new InternalFile("models.txt"));

		// ### Terrain textures ###

		Texture backgroundTexture = loader.loadTexture("grassy.png");
		Texture rTexture = loader.loadTexture("dirt.png");
		Texture gTexture = loader.loadTexture("pinkFlowers.png");
		Texture bTexture = loader.loadTexture("path.png");

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		Texture blendMap = loader.loadTexture("blendMap.png");

		TerrainPack terrainPack = new TerrainPack(loader, texturePack, blendMap, SEED);

		// Player
		Entity entity = new Entity(0, new Vector3f(0, 0, 20), new Vector3f(0, 0, 0), 1,
				loader.getModel(0).getRawModel().getBoundingBox());
		entities.add(entity);
		Player player = new Player(0, new Vector3f(10, 0, 50), new Vector3f(0, 0, 0), 1,
				loader.getModel(0).getRawModel().getBoundingBox(), terrainPack);
		entities.add(player);
		Camera camera = new PlayerCamera(player, terrainPack);

		InternalFile[] dayTextures = InternalFile.fromFilenames("skybox", TEXTURE_FILES, "png"),
				nightTextures = InternalFile.fromFilenames("skybox", NIGHT_TEXTURE_FILES, "png");

		Skybox skybox = new Skybox(Texture.newCubeMap(dayTextures, 200), Texture.newCubeMap(nightTextures, 200), 200);

		MasterRenderer renderer = new MasterRenderer(loader, camera, skybox);
		ParticleMaster particleMaster = new ParticleMaster(loader, camera.getProjectionMatrix());
		TextMaster textMaster = new TextMaster(loader);

		FontType font = new FontType(loader.loadTexture("arial.png"), "arial.fnt");
		GUIText text = new GUIText("Sample text", 1, font, new Vector2f(0.5f, 0.0f), 0.5f, false).setColor(0, 1, 0);
		GUIText fpsText = new GUIText("FPS: ", 1, font, new Vector2f(0.0f, 0.0f), 0.5f, false).setColor(1, 1, 1);
		textMaster.loadText(fpsText);
		textMaster.loadText(text);

		// Barrel

		Entity barrel = new Entity(1, new Vector3f(75, 10, 75), new Vector3f(0, 0, 0), 1f,
				loader.getModel(1).getRawModel().getBoundingBox());
		normalMapEntities.add(barrel);

		Light sun = new Light(new Vector3f(100000, 150000, -70000), new Vector3f(1f, 1f, 1f));
		Light flashLight = new Light(new Vector3f(0, 10, -10), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f));

		lights.add(sun);
		lights.add(flashLight);

		// ### Random lantern generation ###

		Random rand = new Random(SEED);

		for (int i = 0; i < 100; i++) {
			float x = rand.nextFloat() * 1000;
			float z = rand.nextFloat() * 1000;

			entities.add(new Entity(2, new Vector3f(x, terrainPack.getTerrainHeightByWorldPos(x, z), z), new Vector3f(),
					1, loader.getModel(2).getRawModel().getBoundingBox()));
		}

		terrainPack.addWaitingForTerrainHeight(entities.toArray(new Entity[entities.size()]));

		// #### Water rendering ####
		WaterMaster waterMaster = new WaterMaster(loader, loader.loadTexture(DUDV_MAP), loader.loadTexture(NORMAL_MAP), camera);
		WaterTile water = new WaterTile(75, 75, 0);
		waterMaster.addWaterTile(water);

		// #### Gui rendering ####
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiRenderer guiRenderer = new GuiRenderer(loader);

		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particles/fire.png"), 4, false);
		ParticleSystem ps = new ParticleSystem(particleTexture, 50, 10, 0.3f, 1);
		particleMaster.addSystem(ps);

		MultisampleMultitargetFbo multisampleFbo = new MultisampleMultitargetFbo(Display.getWidth(),
				Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		Fbo outputFbo2 = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);

		PostProcessing.init(loader);

		MousePicker picker = new MousePicker(camera, camera.getProjectionMatrix(), terrainPack, entities, guis);

		while (!Display.isCloseRequested()) {
			barrel.increaseRotation(new Vector3f(0, 1, 0));
			player.move();
			terrainPack.update(player);
			camera.update();
			picker.update();

			if (picker.getCurrentTerrainPoint() != null) {
				Vector3f currentPoint = picker.getCurrentTerrainPoint();
				flashLight.getPosition().set(currentPoint);
				flashLight.getPosition().y = terrainPack.getTerrainHeightByWorldPos(currentPoint.x, currentPoint.z)
						+ 1.0f;
			}

			if (picker.getCurrentEntity() != null) {
				picker.getCurrentEntity().setScale(2);
			}

			if (picker.getCurrentGui() != null) {
				if (Mouse.isButtonDown(0)) {
					System.out.println("Clicked gui.");
				}
			}

			// Update debug info
			if (DEBUG) {
				Terrain currentTerrain = terrainPack.getTerrainByWorldPos(player.getPosition().x,
						player.getPosition().z);
				if (currentTerrain != null) {
					Vector3f pos = player.getPosition();
					String textString = "X: " + Math.floor(pos.x) + " Y: " + Math.floor(pos.y) + " Z: "
							+ Math.floor(pos.z) + " Current terrain: " + currentTerrain.getX() / Settings.TERRAIN_SIZE
							+ ":" + currentTerrain.getZ() / Settings.TERRAIN_SIZE;
					text.setText(textString);
					fpsText.setText(String.format("FPS: %.2f", getFps()));
				}
			}

			// Sort list of lights
			DistanceSorter.sort(lights, camera);

			renderer.renderShadowMap(entities, sun);

			ps.setPosition(player.getPosition());
			particleMaster.update(camera);

			Scene scene = new Scene(entities, normalMapEntities, terrainPack, lights, camera, skybox);

			EnvironmentMapRenderer.renderEnvironmentMap(scene.getEnvironmentMap(), scene, player.getPosition(),
					renderer);

			waterMaster.renderBuffers(renderer, scene);

			multisampleFbo.bindFrameBuffer();

			renderer.renderScene(scene, new Vector4f(0, 1, 0, Float.MAX_VALUE));
			waterMaster.renderWater(camera, lights);
			particleMaster.renderParticles(camera);

			multisampleFbo.unbindFrameBuffer();

			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo2);
			PostProcessing.doPostProcessing(outputFbo.getColourTexture(), outputFbo2.getColourTexture());

			guiRenderer.render(guis);
			textMaster.render();

			DisplayManager.updateDisplay();
		}

		PostProcessing.cleanUp();
		outputFbo.cleanUp();
		outputFbo2.cleanUp();
		multisampleFbo.cleanUp();
		particleMaster.cleanUp();
		textMaster.cleanUp();
		waterMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		terrainPack.cleanUp();
		DisplayManager.closeDisplay();
	}

	private static double getFps() {
		return 1 / DisplayManager.getFrameTimeSeconds();
	}
}
