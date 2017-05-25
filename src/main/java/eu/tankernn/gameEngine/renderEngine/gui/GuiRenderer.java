package eu.tankernn.gameEngine.renderEngine.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.renderEngine.Vao;
import eu.tankernn.gameEngine.util.Maths;

public class GuiRenderer {
	
	private final Vao quad;
	private GuiShader shader;
	
	public GuiRenderer(Loader loader) {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}
	
	public void render(List<GuiTexture> guis) {
		shader.start();
		quad.bind(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GuiTexture gui : guis) {
			if (gui.isAdditive()) {
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			} else {
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
			gui.getTexture().bindToUnit(0);
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getSize());
			shader.transformationMatrix.loadMatrix(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getIndexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		quad.unbind(0);
		shader.stop();
	}
	
	public void finalize() {
		shader.finalize();
	}
}
