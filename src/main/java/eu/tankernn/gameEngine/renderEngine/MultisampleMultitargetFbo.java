package eu.tankernn.gameEngine.renderEngine;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import eu.tankernn.gameEngine.loader.textures.TextureUtils;
import eu.tankernn.gameEngine.settings.Settings;

public class MultisampleMultitargetFbo extends Fbo {
	
	private int colorBuffer;
	private int colorBuffer2;
	
	/**
	 * Creates a multisampled FBO of a specified width and height.
	 * 
	 * @param width - the width of the FBO.
	 * @param height - the height of the FBO.
	 */
	public MultisampleMultitargetFbo(int width, int height) {
		super(width, height, DEPTH_RENDER_BUFFER);
	}
	
	public void resolveToFbo(int readBuffer, Fbo outputFbo) {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, outputFbo.frameBuffer);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
		GL11.glReadBuffer(readBuffer);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, outputFbo.width, outputFbo.height, GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		this.unbindFrameBuffer();
	}
	
	public void resolveToScreen() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
		GL11.glDrawBuffer(GL11.GL_BACK);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, Display.getWidth(), Display.getHeight(), GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
		this.unbindFrameBuffer();
	}
	
	protected int createMultisampleColorAttachment(int attachment) {
		int colorBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, colorBuffer);
		GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, Settings.MULTISAMPLING, GL11.GL_RGBA8, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER,
				colorBuffer);
		return colorBuffer;
	}
	
	@Override
	protected void createDepthBufferAttachment() {
		depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, Settings.MULTISAMPLING, GL14.GL_DEPTH_COMPONENT24, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
				depthBuffer);
	}
	
	@Override
	protected void initialiseFrameBuffer(int type) {
		createFrameBuffer();
		colorBuffer = createMultisampleColorAttachment(GL30.GL_COLOR_ATTACHMENT0);
		colorBuffer2 = createMultisampleColorAttachment(GL30.GL_COLOR_ATTACHMENT1);
		if (type == DEPTH_RENDER_BUFFER) {
			createDepthBufferAttachment();
		} else if (type == DEPTH_TEXTURE) {
			TextureUtils.createDepthTextureAttachment(width, height);
		}
		unbindFrameBuffer();
	}
	
	@Override
	protected void determineDrawBuffers() {
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(2);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT0);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT1);
		drawBuffers.flip();
		GL20.glDrawBuffers(drawBuffers);
	};
	
	@Override
	public void cleanUp() {
		super.cleanUp();
		GL30.glDeleteRenderbuffers(colorBuffer);
		GL30.glDeleteRenderbuffers(colorBuffer2);
	}
}
