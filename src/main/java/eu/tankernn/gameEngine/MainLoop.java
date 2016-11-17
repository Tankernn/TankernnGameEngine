package eu.tankernn.gameEngine;

import java.io.FileNotFoundException;
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
import eu.tankernn.gameEngine.loader.models.RawModel;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.loader.obj.ModelData;
import eu.tankernn.gameEngine.loader.obj.OBJFileLoader;
import eu.tankernn.gameEngine.loader.obj.normalMapped.NormalMappedObjLoader;
import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.loader.textures.TerrainTexture;
import eu.tankernn.gameEngine.loader.textures.TerrainTexturePack;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.particles.ParticleMaster;
import eu.tankernn.gameEngine.particles.ParticleSystem;
import eu.tankernn.gameEngine.particles.ParticleTexture;
import eu.tankernn.gameEngine.postProcessing.Fbo;
import eu.tankernn.gameEngine.postProcessing.MultisampleMultitargetFbo;
import eu.tankernn.gameEngine.postProcessing.PostProcessing;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.renderEngine.Loader;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.renderEngine.Scene;
import eu.tankernn.gameEngine.renderEngine.font.TextMaster;
import eu.tankernn.gameEngine.renderEngine.gui.GuiRenderer;
import eu.tankernn.gameEngine.renderEngine.gui.GuiTexture;
import eu.tankernn.gameEngine.renderEngine.skybox.Skybox;
import eu.tankernn.gameEngine.renderEngine.water.WaterMaster;
import eu.tankernn.gameEngine.renderEngine.water.WaterTile;
import eu.tankernn.gameEngine.terrains.Terrain;
import eu.tankernn.gameEngine.terrains.TerrainPack;
import eu.tankernn.gameEngine.util.DistanceSorter;
import eu.tankernn.gameEngine.util.MousePicker;
import eu.tankernn.gameEngine.util.InternalFile;

public class MainLoop {

	private static final int SEED = 1235;
	
	// Skybox settings
	public static final String[] TEXTURE_FILES = {"alps_rt", "alps_lf", "alps_up", "alps_dn", "alps_bk", "alps_ft"};
	public static final String[] NIGHT_TEXTURE_FILES = {"midnight_rt", "midnight_lf", "midnight_up", "midnight_dn", "midnight_bk", "midnight_ft"};
	
	// Water settings
	public static final String DUDV_MAP = "waterDUDV";
	public static final String NORMAL_MAP = "waterNormalMap";

	private static final boolean DEBUG = true;

	public static void main(String[] args) throws FileNotFoundException {
		
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		TerrainPack terrainPack = new TerrainPack();

		DisplayManager.createDisplay("Tankernn Game Engine tester");
		Loader loader = new Loader();
		
		// Monkey
		ModelData monkeyData = OBJFileLoader.loadOBJ("character");
		RawModel monkeyModel = loader.loadToVAO(monkeyData);
		TexturedModel texturedMonkeyModel = new TexturedModel(monkeyModel,
				new ModelTexture(loader.loadTexture("erkky")));

		ModelTexture texture = texturedMonkeyModel.getTexture();
		texture.setReflectivity(3);
		texture.setShineDamper(10);

		Entity entity = new Entity(texturedMonkeyModel, new Vector3f(0, 0, 20), 0, 0, 0, 1);
		entities.add(entity);
		TexturedModel monkey = new TexturedModel(monkeyModel, new ModelTexture(loader.loadTexture("white")));
		Player player = new Player(monkey, new Vector3f(10, 0, 50), 0, 0, 0, 1, terrainPack);
		entities.add(player);
		Camera camera = new PlayerCamera(player, terrainPack);
		
		InternalFile[] dayTextures = new InternalFile[TEXTURE_FILES.length], nightTextures = new InternalFile[NIGHT_TEXTURE_FILES.length];
		
		for (int i = 0; i < TEXTURE_FILES.length; i++)
			dayTextures[i] = new InternalFile("skybox/" + TEXTURE_FILES[i] + ".png");
		for (int i = 0; i < NIGHT_TEXTURE_FILES.length; i++)
			nightTextures[i] = new InternalFile("skybox/" + NIGHT_TEXTURE_FILES[i] + ".png");
		
		Skybox skybox = new Skybox(Texture.newCubeMap(dayTextures, 500), Texture.newCubeMap(nightTextures, 500), 500);
		
		MasterRenderer renderer = new MasterRenderer(loader, camera, skybox);
		ParticleMaster.init(loader, camera.getProjectionMatrix());
		TextMaster.init(loader);

		FontType font = new FontType(loader.loadTexture("arial"), "arial.fnt");
		GUIText text = new GUIText("Sample text", 3, font, new Vector2f(0.5f, 0.5f), 0.5f, true);
		text.setColor(1, 1, 1);

		// Barrel
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture("barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		barrelModel.getTexture().setSpecularMap(loader.loadTexture("barrelS"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		Entity barrel = new Entity(barrelModel, new Vector3f(75, 10, 75), 0, 0, 0, 1f);
		normalMapEntities.add(barrel);

		Light sun = new Light(new Vector3f(100000, 150000, -70000), new Vector3f(1f, 1f, 1f));
		Light flashLight = new Light(new Vector3f(0, 10, -10), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f));
		List<Light> lights = new ArrayList<Light>();
		lights.add(sun);
		lights.add(flashLight);
		lights.add(new Light(new Vector3f(10, 100, 0), new Vector3f(0, 1, 0)));
		lights.add(new Light(new Vector3f(20, 100, 0), new Vector3f(0, 0, 1)));
		lights.add(new Light(new Vector3f(30, 100, 0), new Vector3f(1, 0, 0)));
		lights.add(new Light(new Vector3f(40, 100, 0), new Vector3f(1, 1, 0)));

		// ### Terrain textures ###

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		terrainPack.addTerrain(new Terrain(0, 1, loader, texturePack, blendMap, SEED));
		terrainPack.addTerrain(new Terrain(1, 1, loader, texturePack, blendMap, SEED));
		terrainPack.addTerrain(new Terrain(0, 0, loader, texturePack, blendMap, SEED));
		terrainPack.addTerrain(new Terrain(1, 0, loader, texturePack, blendMap, SEED));

		// ### Random grass generation ###

		ModelTexture textureAtlas = new ModelTexture(loader.loadTexture("lantern"));
		textureAtlas.setNumberOfRows(1);
		TexturedModel grassModel = new TexturedModel(loader.loadToVAO(OBJFileLoader.loadOBJ("lantern")), textureAtlas);
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setShineDamper(10);
		grassModel.getTexture().setReflectivity(0.5f);
		grassModel.getTexture().setSpecularMap(loader.loadTexture("lanternS"));

		Random rand = new Random();

		for (int i = 0; i < 1000; i++) {
			float x = rand.nextFloat() * 1000;
			float z = rand.nextFloat() * 1000;

			entities.add(new Entity(grassModel, rand.nextInt(4),
					new Vector3f(x, terrainPack.getTerrainHeightByWorldPos(x, z), z), 0, 0, 0, 1));
		}

		// #### Water rendering ####
		WaterMaster waterMaster = new WaterMaster(loader, DUDV_MAP, NORMAL_MAP, camera);
		WaterTile water = new WaterTile(75, 75, 0);
		waterMaster.addWaterTile(water);

		// #### Gui rendering ####
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		
		GuiTexture debug = new GuiTexture(0, new Vector2f(1, 1), new Vector2f(1, 1));
		
		guis.add(debug);

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particles/cosmic"), 4, true);
		ParticleSystem ps = new ParticleSystem(particleTexture, 50, 10, 0.3f, 4);

		MultisampleMultitargetFbo multisampleFbo = new MultisampleMultitargetFbo(Display.getWidth(),
				Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		Fbo outputFbo2 = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);

		PostProcessing.init(loader);

		MousePicker picker = new MousePicker(camera, camera.getProjectionMatrix(), terrainPack, entities, guis);
		
		while (!Display.isCloseRequested()) {
			barrel.increaseRotation(0, 1, 0);
			player.move(terrainPack);
			camera.update();
			picker.update();

			if (picker.getCurrentTerrainPoint() != null) {
				Vector3f currentPoint = picker.getCurrentTerrainPoint();
				flashLight.getPosition().x = currentPoint.x;
				flashLight.getPosition().z = currentPoint.z;
				flashLight.getPosition().y = terrainPack.getTerrainHeightByWorldPos(currentPoint.x, currentPoint.z)
						+ 1.0f;
			}

			if (picker.getCurrentGui() != null) {
				if (Mouse.isButtonDown(0)) {
					System.out.println("Clicked gui.");
					picker.getCurrentGui().getPosition().x += 0.1f;
				}
			}

			// Update debug info
			if (DEBUG) {
				Terrain currentTerrain = terrainPack.getTerrainByWorldPos(player.getPosition().x,
						player.getPosition().z);
				if (currentTerrain != null) {
					text.remove();
					Vector3f pos = player.getPosition();
					String textString = "X: " + Math.floor(pos.x) + " Y: " + Math.floor(pos.y) + " Z: "
							+ Math.floor(pos.z);
					text = new GUIText(textString, 1, font, new Vector2f(0.5f, 0f), 0.5f, false);
				}
			}

			// Sort list of lights
			DistanceSorter.sort(lights, camera);

			renderer.renderShadowMap(entities, sun);

			ps.generateParticles(player.getPosition());
			ParticleMaster.update(camera);

			Scene scene = new Scene(entities, normalMapEntities, terrainPack, lights, camera, skybox);
			
			EnvironmentMapRenderer.renderEnvironmentMap(scene.getEnvironmentMap(), scene, player.getPosition(), renderer);
			
			debug = new GuiTexture(scene.getEnvironmentMap().textureId, new Vector2f(1, 1), new Vector2f(1, 1));
			
			waterMaster.renderBuffers(renderer, scene);

			multisampleFbo.bindFrameBuffer();

			renderer.renderScene(scene, new Vector4f(0, 1, 0, Float.MAX_VALUE));
			waterMaster.renderWater(camera, lights);
			ParticleMaster.renderParticles(camera);

			multisampleFbo.unbindFrameBuffer();

			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo2);
			PostProcessing.doPostProcessing(outputFbo.getColourTexture(), outputFbo2.getColourTexture());

			guiRenderer.render(guis);
			TextMaster.render();

			DisplayManager.updateDisplay();
		}

		PostProcessing.cleanUp();
		outputFbo.cleanUp();
		outputFbo2.cleanUp();
		multisampleFbo.cleanUp();
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		waterMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
