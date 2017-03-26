package eu.tankernn.gameEngine.terrains;

import static eu.tankernn.gameEngine.settings.Settings.TERRAIN_SIZE;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.textures.TerrainTexturePack;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.Vao;
import eu.tankernn.gameEngine.util.InternalFile;
import eu.tankernn.gameEngine.util.Maths;

public class Terrain {

	private float x, z;
	private int gridX, gridZ;
	private Vao model;
	private TerrainTexturePack texturePack;
	private Texture blendMap;

	private float[][] heights;

	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, Texture blendMap,
			InternalFile heightMap) {
		this.gridX = gridX;
		this.gridZ = gridZ;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * TERRAIN_SIZE;
		this.z = gridZ * TERRAIN_SIZE;
		TerrainModelData data = new TerrainModelData(gridX, gridZ, heightMap);
		this.model = data.getModel(loader);
		this.heights = data.getHeights();
	}

	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, Texture blendMap, int seed) {
		this.gridX = gridX;
		this.gridZ = gridZ;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * TERRAIN_SIZE;
		this.z = gridZ * TERRAIN_SIZE;
		TerrainModelData data = new TerrainModelData(gridX, gridZ, seed);
		this.model = data.getModel(loader);
		this.heights = data.getHeights();
	}

	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, Texture blendMap,
			TerrainModelData data) {
		this.gridX = gridX;
		this.gridZ = gridZ;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * TERRAIN_SIZE;
		this.z = gridZ * TERRAIN_SIZE;
		this.model = data.getModel(loader);
		this.heights = data.getHeights();
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	float getSize() {
		return TERRAIN_SIZE;
	}

	public Vao getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public Texture getBlendMap() {
		return blendMap;
	}

	public Pair<Integer, Integer> getGridPosition() {
		return Pair.of(this.gridX, this.gridZ);
	}

	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;

		float gridSquareSize = TERRAIN_SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}

		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

		float answer;
		if (xCoord <= (1 - zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}
		return answer;
	}
	
	public void delete() {
		model.finalize();
	}
}
