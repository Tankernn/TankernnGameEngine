package eu.tankernn.gameEngine.terrains;

import java.util.ArrayList;
import java.util.List;

import eu.tankernn.gameEngine.renderEngine.MasterRenderer;

public class TerrainPack {
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public boolean addTerrain(Terrain terrain) {
		if (terrain != null) {
			terrains.add(terrain);
			return true;
		} else {
			return false;
		}
	}
	
	public List<Terrain> getList() {
		return terrains;
	}
	
	public Terrain getTerrainByWorldPos(float x, float z) {
		for (Terrain terrain: terrains) {
			if (x >= terrain.getX() && x <= terrain.getX() + terrain.getSize()) {
				if (z >= terrain.getZ() && z <= terrain.getZ() + terrain.getSize()) {
					return terrain;
				}
			}
		}
		return null;
	}
	
	public float getTerrainHeightByWorldPos(float x, float z) {
		Terrain terrain = getTerrainByWorldPos(x, z);
		if (terrain != null)
			return terrain.getHeightOfTerrain(x, z);
		else
			return 0;
	}
	
	public void prepareRenderTerrains(MasterRenderer renderer) {
		for (Terrain terrain: terrains) {
			renderer.processTerrain(terrain);
		}
	}
}
