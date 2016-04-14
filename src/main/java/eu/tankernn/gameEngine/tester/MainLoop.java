package eu.tankernn.gameEngine.tester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.entities.Player;
import eu.tankernn.gameEngine.entities.PlayerCamera;
import eu.tankernn.gameEngine.font.meshCreator.FontType;
import eu.tankernn.gameEngine.font.meshCreator.GUIText;
import eu.tankernn.gameEngine.font.rendering.TextMaster;
import eu.tankernn.gameEngine.gui.GuiRenderer;
import eu.tankernn.gameEngine.gui.GuiTexture;
import eu.tankernn.gameEngine.models.RawModel;
import eu.tankernn.gameEngine.models.TexturedModel;
import eu.tankernn.gameEngine.normalMapping.objConverter.NormalMappedObjLoader;
import eu.tankernn.gameEngine.objLoader.ModelData;
import eu.tankernn.gameEngine.objLoader.OBJFileLoader;
import eu.tankernn.gameEngine.particles.ParticleMaster;
import eu.tankernn.gameEngine.particles.ParticleSystem;
import eu.tankernn.gameEngine.particles.ParticleTexture;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.renderEngine.Loader;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.renderEngine.Scene;
import eu.tankernn.gameEngine.terrains.Terrain;
import eu.tankernn.gameEngine.terrains.TerrainPack;
import eu.tankernn.gameEngine.textures.ModelTexture;
import eu.tankernn.gameEngine.textures.TerrainTexture;
import eu.tankernn.gameEngine.textures.TerrainTexturePack;
import eu.tankernn.gameEngine.util.MousePicker;
import eu.tankernn.gameEngine.util.Sorter;
import eu.tankernn.gameEngine.water.WaterMaster;
import eu.tankernn.gameEngine.water.WaterTile;

public class MainLoop {
	
	private static final int SEED = 1235;
	
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		TerrainPack terrainPack = new TerrainPack();
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		// Monkey
		ModelData monkeyData = OBJFileLoader.loadOBJ("character");
		RawModel monkeyModel = loader.loadToVAO(monkeyData);
		TexturedModel texturedMonkeyModel = new TexturedModel(monkeyModel, new ModelTexture(loader.loadTexture("erkky")));
		
		ModelTexture texture = texturedMonkeyModel.getTexture();
		texture.setReflectivity(3);
		texture.setShineDamper(10);
		
		Entity entity = new Entity(texturedMonkeyModel, new Vector3f(0, 0, 20), 0, 0, 0, 1);
		entities.add(entity);
		TexturedModel monkey = new TexturedModel(monkeyModel, new ModelTexture(loader.loadTexture("white")));
		Player player = new Player(monkey, new Vector3f(10, 0, 50), 0, 0, 0, 1, terrainPack);
		entities.add(player);
		Camera camera = new PlayerCamera(player, terrainPack);
		
		MasterRenderer renderer = new MasterRenderer(loader, camera);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		TextMaster.init(loader);
		
		FontType font = new FontType(loader.loadTexture("arial"), "arial.fnt");
		GUIText text = new GUIText("Sample text", 3, font, new Vector2f(0.5f, 0.5f), 0.5f, true);
		text.setColor(1, 1, 1);
		
		//Barrel
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader), new ModelTexture(loader.loadTexture("barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		Entity barrel = new Entity(barrelModel, new Vector3f(75, 10, 75), 0, 0, 0, 1f);
		normalMapEntities.add(barrel);
		
		Light sun = new Light(new Vector3f(100000, 150000, -70000), new Vector3f(1, 1, 1));
		Light flashLight = new Light(new Vector3f(0, 10, -10), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f));
		List<Light> lights = new ArrayList<Light>();
		lights.add(sun);
		lights.add(flashLight);
		lights.add(new Light(new Vector3f(10, 100,0), new Vector3f(0,1,0)));
		lights.add(new Light(new Vector3f(20, 100,0), new Vector3f(0,0,1)));
		lights.add(new Light(new Vector3f(30, 100,0), new Vector3f(1,0,0)));
		lights.add(new Light(new Vector3f(40, 100,0), new Vector3f(1,1,0)));
		
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
		
		ModelTexture textureAtlas = new ModelTexture(loader.loadTexture("fern"));
		textureAtlas.setNumberOfRows(2);
		TexturedModel grassModel = new TexturedModel(loader.loadToVAO(OBJFileLoader.loadOBJ("fern")), textureAtlas);
		//grassModel.getTexture().setHasTransparency(true);
		//grassModel.getTexture().setUseFakeLighting(true);
		Random rand = new Random();
		
		for (int i = 0; i < 1000; i++) {
			float x = rand.nextFloat() * 1000;
			float z = rand.nextFloat() * 1000;
			
			entities.add(new Entity(grassModel, rand.nextInt(4), new Vector3f(x, terrainPack.getTerrainHeightByWorldPos(x, z), z), 0, 0, 0, 1));
		}
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrainPack, entities);
		
		// #### Water rendering ####
		WaterMaster waterMaster = new WaterMaster(loader, renderer);
		WaterTile water = new WaterTile(75, 75, 0);
		waterMaster.addWaterTile(water);
		
		// #### Gui rendering ####
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture depth = new GuiTexture(waterMaster.getBuffers().getRefractionDepthTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		GuiTexture refraction = new GuiTexture(waterMaster.getBuffers().getRefractionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		guis.add(depth);
		guis.add(refraction);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particles/cosmic"), 4, true);
		ParticleSystem ps = new ParticleSystem(particleTexture, 50, 10, 0.3f, 4);
		
		while (!Display.isCloseRequested()) {
			barrel.increaseRotation(0, 1, 0);
			player.move(terrainPack);
			camera.update();
			picker.update();
			
			if (picker.getCurrentTerrainPoint() != null) {
				Vector3f currentPoint = picker.getCurrentTerrainPoint();
				flashLight.getPosition().x = currentPoint.x;
				flashLight.getPosition().z = currentPoint.z;
				flashLight.getPosition().y = terrainPack.getTerrainHeightByWorldPos(currentPoint.x, currentPoint.z) + 1.0f;
			}
			
			// Update debug info
			
			Terrain currentTerrain = terrainPack.getTerrainByWorldPos(player.getPosition().x, player.getPosition().z);
			if (currentTerrain != null) {
				text.remove();
				Vector3f pos = player.getPosition();
				String textString = "X: " + Math.floor(pos.x) + " Y: " + Math.floor(pos.y) + " Z: " + Math.floor(pos.z);
				text = new GUIText(textString, 1, font, new Vector2f(0.5f, 0f), 0.5f, false);
			}
			
			//Sort list of lights
			lights = new Sorter<Light>(lights, camera).sortByDistance();
			
			renderer.renderShadowMap(entities, sun);
			
			ps.generateParticles(player.getPosition());
			ParticleMaster.update(camera);
			
			Scene scene = new Scene(entities, normalMapEntities, terrainPack, lights, camera);
			
			waterMaster.renderBuffers(renderer, scene);
			renderer.renderScene(scene, new Vector4f(0, 1, 0, Float.MAX_VALUE));
			waterMaster.renderWater(camera, lights);
			ParticleMaster.renderParticles(camera);
			guiRenderer.render(guis);
			TextMaster.render();
			
			DisplayManager.updateDisplay();
		}
		
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		waterMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
