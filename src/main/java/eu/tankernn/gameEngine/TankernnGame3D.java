package eu.tankernn.gameEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.entities.Player;
import eu.tankernn.gameEngine.entities.projectiles.Projectile;
import eu.tankernn.gameEngine.environmentMap.EnvironmentMapRenderer;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.particles.ParticleMaster;
import eu.tankernn.gameEngine.postProcessing.PostProcessor;
import eu.tankernn.gameEngine.renderEngine.Fbo;
import eu.tankernn.gameEngine.renderEngine.MasterRenderer;
import eu.tankernn.gameEngine.renderEngine.MultisampleMultitargetFbo;
import eu.tankernn.gameEngine.renderEngine.Scene;
import eu.tankernn.gameEngine.renderEngine.skybox.Skybox;
import eu.tankernn.gameEngine.renderEngine.water.WaterMaster;
import eu.tankernn.gameEngine.terrains.TerrainPack;
import eu.tankernn.gameEngine.util.DistanceSorter;
import eu.tankernn.gameEngine.util.InternalFile;
import eu.tankernn.gameEngine.util.MousePicker;

public class TankernnGame3D extends TankernnGame {
	protected MasterRenderer renderer;
	protected WaterMaster waterMaster;
	protected ParticleMaster particleMaster;
	protected PostProcessor postProcessor;
	protected Camera camera;
	protected Skybox sky;
	protected MousePicker picker;

	protected List<Entity3D> entities = new ArrayList<>();
	protected List<Projectile> projectiles = new ArrayList<>();
	protected List<Light> lights = new ArrayList<>();
	private Light sun;
	protected TerrainPack terrainPack;
	protected Player player;
	
	private MultisampleMultitargetFbo multisampleFbo = new MultisampleMultitargetFbo(Display.getWidth(),
			Display.getHeight());
	private Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE),
			outputFbo2 = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
	
	public TankernnGame3D(String name, String[] dayTextures, String[] nightTextures, Light sun) {
		super(name);
		this.sun = sun;
		lights.add(sun);
		try {
			loader.readModelSpecification(new InternalFile("models.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.sky = new Skybox(loader, Texture.newCubeMap(InternalFile.fromFilenames("skybox", dayTextures, "png"), 400),
				Texture.newCubeMap(InternalFile.fromFilenames("skybox", nightTextures, "png"), 400), 400);
	}

	public void update() {
		super.update();
		entities.forEach(Entity3D::update);
		entities.removeIf(Entity3D::isDead);
		
		projectiles.forEach(Projectile::update);
		projectiles.removeIf(Projectile::isDead);
		projectiles.forEach((p) -> p.checkCollision(entities));
		
		player.move();
		picker.update(terrainPack, entities, guiMaster.getGuis());
		camera.update();
		terrainPack.update(player);
		particleMaster.update(camera);
		DistanceSorter.sort(lights, camera);
		
		audioMaster.setListenerPosition(player.getPosition());
	}

	public void render() {
		renderer.renderShadowMap(entities, sun);

		Scene scene = new Scene(entities, terrainPack, lights, camera, sky);

		EnvironmentMapRenderer.renderEnvironmentMap(scene.getEnvironmentMap(), scene, player.getPosition(), renderer);

		waterMaster.renderBuffers(renderer, scene);

		multisampleFbo.bindFrameBuffer();

		renderer.renderScene(scene, new Vector4f(0, 1, 0, Float.MAX_VALUE));
		waterMaster.renderWater(camera, lights);
		particleMaster.renderParticles(camera);

		multisampleFbo.unbindFrameBuffer();

		multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
		multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo2);

		postProcessor.doPostProcessing(outputFbo.getColourTexture(), outputFbo2.getColourTexture());
		super.render();
	}

	public void cleanUp() {
		super.cleanUp();
		terrainPack.finalize();
		particleMaster.finalize();
		postProcessor.finalize();
		waterMaster.finalize();
		multisampleFbo.finalize();
		outputFbo.finalize();
		outputFbo2.finalize();
		renderer.finalize();
	}
}
