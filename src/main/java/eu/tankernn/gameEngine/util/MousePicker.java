package eu.tankernn.gameEngine.util;

import java.util.Collection;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.renderEngine.gui.GuiTexture;
import eu.tankernn.gameEngine.terrains.Terrain;
import eu.tankernn.gameEngine.terrains.TerrainPack;

public class MousePicker {
	private static final int RECURSION_COUNT = 200;
	private static final float RAY_RANGE = 600;
	
	private Vector3f currentRay = new Vector3f();
	
	private ICamera camera;
	
	private Vector3f currentTerrainPoint;
	private Entity3D currentEntity;
	
	private GuiTexture currentGui;
	
	public MousePicker(ICamera cam) {
		camera = cam;
	}
	
	public Entity3D getCurrentEntity() {
		return currentEntity;
	}
	
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}
	
	public Vector3f getCurrentRay() {
		return currentRay;
	}
	
	public GuiTexture getCurrentGui() {
		return currentGui;
	}
	
	public void update(TerrainPack terrains, Collection<Entity3D> collection, List<GuiTexture> guis) {
		currentRay = calculateMouseRay();
		currentGui = calculateGuiTexture(guis);
		if (intersectionInRange(terrains, 0, RAY_RANGE, currentRay)) {
			currentTerrainPoint = binarySearch(terrains, 0, 0, RAY_RANGE, currentRay);
		} else {
			currentTerrainPoint = null;
		}
		
		for (Entity3D e: collection) {
			if (entityInstersect(e)) {
				currentEntity = e;
				return;
			}
		}
		currentEntity = null;
	}
	
	private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(camera.getViewMatrix(), null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}
	
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(camera.getProjectionMatrix(), null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}
	
	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Display.getWidth() - 1f;
		float y = (2.0f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
	
	// GUI Intersect
	
	private GuiTexture calculateGuiTexture(List<GuiTexture> guis) {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f mouseCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		
		for (GuiTexture gui: guis) {
			float posX = gui.getPosition().x;
			float posY = gui.getPosition().y;
			float scaleX = gui.getSize().x;
			float scaleY = gui.getSize().y;
			
			if (mouseCoords.x > posX - scaleX && mouseCoords.x < posX + scaleX && mouseCoords.y > posY - scaleY && mouseCoords.y < posY + scaleY) {
				return gui;
			}
		}
		return null;
	}
	
	// #### Entity intersect ####
	
	public boolean entityInstersect(Entity3D entity) {
		AABB box = entity.getBoundingBox();
		Vector3f dirfrac = new Vector3f();
		
		dirfrac.x = 1.0f / currentRay.x;
		dirfrac.y = 1.0f / currentRay.y;
		dirfrac.z = 1.0f / currentRay.z;
		// lb is the corner of AABB with minimal coordinates - left bottom, rt
		// is maximal corner
		// camera.getPosition() is origin of ray
		
		float t1 = (box.getLb().x - camera.getPosition().x) * dirfrac.x;
		float t2 = (box.getRt().x - camera.getPosition().x) * dirfrac.x;
		float t3 = (box.getLb().y - camera.getPosition().y) * dirfrac.y;
		float t4 = (box.getRt().y - camera.getPosition().y) * dirfrac.y;
		float t5 = (box.getLb().z - camera.getPosition().z) * dirfrac.z;
		float t6 = (box.getRt().z - camera.getPosition().z) * dirfrac.z;
		
		float tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
		float tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));
		
		// if tmax < 0, ray (line) is intersecting AABB, but whole AABB is
		// behind us
		if (tmax < 0) {
			return false;
		}
		
		// if tmin > tmax, ray doesn't intersect AABB
		if (tmin > tmax) {
			return false;
		}
		
		return true;
	}
	
	// #### Terrain intersect ####
	
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return Vector3f.add(start, scaledRay, null);
	}
	
	private Vector3f binarySearch(TerrainPack terrains, int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = terrains.getTerrainByWorldPos(endPoint.getX(), endPoint.getZ());
			return terrain != null ? endPoint : null;
		}
		if (intersectionInRange(terrains, start, half, ray)) {
			return binarySearch(terrains, count + 1, start, half, ray);
		} else {
			return binarySearch(terrains, count + 1, half, finish, ray);
		}
	}
	
	private boolean intersectionInRange(TerrainPack terrains, float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		return !isUnderGround(terrains, startPoint) && isUnderGround(terrains, endPoint);
	}
	
	private boolean isUnderGround(TerrainPack terrains, Vector3f testPoint) {
		return testPoint.y < terrains.getTerrainHeightByWorldPos(testPoint.x, testPoint.z);
	}
}
