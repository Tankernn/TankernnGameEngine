package eu.tankernn.gameEngine.renderEngine.font;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import eu.tankernn.gameEngine.loader.font.Font;
import eu.tankernn.gameEngine.loader.font.GUIText;

public class FontRenderer {
	
	private FontShader shader = new FontShader();
	
	public void render(Map<Font, List<GUIText>> texts) {
		prepare();
		for (Font font: texts.keySet()) {
			loadFont(font);
			for (GUIText text: texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}
	
	public void finalilze() {
		shader.finalize();
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
	}
	
	private void loadFont(Font font) {
		font.family.getTextureAtlas().bindToUnit(0);
		shader.color.loadVec3(font.color);
		shader.width.loadFloat(font.width);
		shader.edge.loadFloat(font.edge);
		shader.borderWidth.loadFloat(font.outlineWidth);
		shader.borderEdge.loadFloat(font.outlineEdge);
		shader.outlineColor.loadVec3(font.outlineColor);
	}
	
	private void renderText(GUIText text) {
		text.getMesh().bind(0, 1);
		shader.translation.loadVec2(text.getPosition());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		text.getMesh().unbind(0, 1);
	}
	
	private void endRendering() {
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void render(GUIText guiText) {
		prepare();
		loadFont(guiText.getFont());
		renderText(guiText);
		endRendering();
	}
	
}
