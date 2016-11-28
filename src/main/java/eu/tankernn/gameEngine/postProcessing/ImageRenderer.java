package eu.tankernn.gameEngine.postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.Fbo;

public class ImageRenderer {

	private Fbo fbo;

	public ImageRenderer(int width, int height) {
		this(new Fbo(width, height, Fbo.NONE));
	}

	public ImageRenderer() {
		this(Display.getWidth(), Display.getHeight());
	}
	
	public ImageRenderer(Fbo fbo) {
		this.fbo = fbo;
	}

	public void renderQuad() {
		if (fbo != null) {
			fbo.bindFrameBuffer();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
	}

	public Texture getOutputTexture() {
		return fbo.getColourTexture();
	}

	public void cleanUp() {
		if (fbo != null) {
			fbo.cleanUp();
		}
	}

}
