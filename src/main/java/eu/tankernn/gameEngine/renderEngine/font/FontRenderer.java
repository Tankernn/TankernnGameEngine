package eu.tankernn.gameEngine.renderEngine.font;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import eu.tankernn.gameEngine.loader.font.FontType;
import eu.tankernn.gameEngine.loader.font.GUIText;

public class FontRenderer {
	
	private FontShader shader;
	
	public FontRenderer() {
		shader = new FontShader();
	}
	
	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();
		for (FontType font: texts.keySet()) {
			font.getTextureAtlas().bindToUnit(0);
			for (GUIText text: texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
	}
	
	private void renderText(GUIText text) {
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.color.loadVec3(text.getColor());
		shader.translation.loadVec2(text.getPosition());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void endRendering() {
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
}
