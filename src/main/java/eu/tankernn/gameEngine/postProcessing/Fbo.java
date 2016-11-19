package eu.tankernn.gameEngine.postProcessing;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.loader.textures.TextureUtils;

public class Fbo {

	public static final int NONE = 0, DEPTH_TEXTURE = 1, DEPTH_RENDER_BUFFER = 2;

	protected final int width;
	protected final int height;

	protected int frameBuffer;

	private Texture colourTexture;
	private Texture depthTexture;

	protected int depthBuffer;

	/**
	 * Creates an FBO of a specified width and height, with the desired type of
	 * depth buffer attachment.
	 * 
	 * @param width
	 *            - the width of the FBO.
	 * @param height
	 *            - the height of the FBO.
	 * @param depthBufferType
	 *            - an int indicating the type of depth buffer attachment that
	 *            this FBO should use.
	 */
	public Fbo(int width, int height, int depthBufferType) {
		this.width = width;
		this.height = height;
		initialiseFrameBuffer(depthBufferType);
	}

	/**
	 * Deletes the frame buffer and its attachments when the game closes.
	 */
	public void cleanUp() {
		GL30.glDeleteFramebuffers(frameBuffer);
		if (colourTexture != null)
			colourTexture.delete();
		if (depthTexture != null)
			depthTexture.delete();
		GL30.glDeleteRenderbuffers(depthBuffer);
	}

	/**
	 * Binds the frame buffer, setting it as the current render target. Anything
	 * rendered after this will be rendered to this FBO, and not to the screen.
	 */
	public void bindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Unbinds the frame buffer, setting the default frame buffer as the current
	 * render target. Anything rendered after this will be rendered to the
	 * screen, and not this FBO.
	 */
	public void unbindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	/**
	 * Binds the current FBO to be read from (not used in tutorial 43).
	 */
	public void bindToRead() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
		GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}

	/**
	 * @return The texture containing the color buffer of the FBO.
	 */
	public Texture getColourTexture() {
		return colourTexture;
	}

	/**
	 * @return The texture containing the FBOs depth buffer.
	 */
	public Texture getDepthTexture() {
		return depthTexture;
	}

	/**
	 * Creates the FBO along with a colour buffer texture attachment, and
	 * possibly a depth buffer.
	 * 
	 * @param type
	 *            - the type of depth buffer attachment to be attached to the
	 *            FBO.
	 */
	protected void initialiseFrameBuffer(int type) {
		createFrameBuffer();
		colourTexture = TextureUtils.createTextureAttachment(width, height);
		if (type == DEPTH_RENDER_BUFFER) {
			createDepthBufferAttachment();
		} else if (type == DEPTH_TEXTURE) {
			depthTexture = TextureUtils.createDepthTextureAttachment(width, height);
		}
		unbindFrameBuffer();
	}
	
	protected void determineDrawBuffers() {
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(2);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT0);
		drawBuffers.flip();
		GL20.glDrawBuffers(drawBuffers);
	}

	/**
	 * Creates a new frame buffer object and sets the buffer to which drawing
	 * will occur - colour attachment 0. This is the attachment where the colour
	 * buffer texture is.
	 */
	protected void createFrameBuffer() {
		frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		determineDrawBuffers();
	}

	/**
	 * Adds a depth buffer to the FBO in the form of a render buffer. This can't
	 * be used for sampling in the shaders.
	 */
	protected void createDepthBufferAttachment() {
		depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
				depthBuffer);
	}

}
