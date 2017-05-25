package eu.tankernn.gameEngine.renderEngine.lensFlare;

import java.util.Arrays;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import eu.tankernn.gameEngine.renderEngine.gui.GuiRenderer;
import eu.tankernn.gameEngine.renderEngine.gui.GuiTexture;
import eu.tankernn.gameEngine.util.ICamera;

public class FlareManager {

	private static final Vector2f CENTER_SCREEN = new Vector2f(0f, 0f);// center
																		// changed

	private final GuiTexture[] flareTextures;
	private final float spacing;

	private final GuiRenderer renderer;

	public FlareManager(GuiRenderer renderer, float spacing, GuiTexture... textures) {
		this.spacing = spacing;
		this.flareTextures = textures;
		this.renderer = renderer;
	}

	public void render(ICamera camera, Vector3f sunWorldPos) {
		Vector2f sunCoords = convertToScreenSpace(sunWorldPos, camera.getViewMatrix(), camera.getProjectionMatrix());
		if (sunCoords == null) {
			return;
		}
		Vector2f sunToCenter = Vector2f.sub(CENTER_SCREEN, sunCoords, null);
		float brightness = 1 - (sunToCenter.length() / 1.4f);// number doubled
		if (brightness > 0) {
			calcFlarePositions(sunToCenter, sunCoords);
			for (GuiTexture t : flareTextures)
				t.setOpacity(brightness);
			renderer.render(Arrays.asList(flareTextures));
		}
	}

	private void calcFlarePositions(Vector2f sunToCenter, Vector2f sunCoords) {
		for (int i = 0; i < flareTextures.length; i++) {
			Vector2f direction = new Vector2f(sunToCenter);
			direction.scale(i * spacing);
			Vector2f flarePos = Vector2f.add(sunCoords, direction, null);
			flareTextures[i].setPosition(flarePos);
		}
	}

	private Vector2f convertToScreenSpace(Vector3f worldPos, Matrix4f viewMat, Matrix4f projectionMat) {
		Vector4f coords = new Vector4f(worldPos.x, worldPos.y, worldPos.z, 1f);
		Matrix4f.transform(viewMat, coords, coords);
		Matrix4f.transform(projectionMat, coords, coords);
		if (coords.w <= 0) {
			return null;
		}
		// no need for conversion below
		return new Vector2f(coords.x / coords.w, coords.y / coords.w);
	}

	public void finalize() {
		renderer.finalize();
	}

}
