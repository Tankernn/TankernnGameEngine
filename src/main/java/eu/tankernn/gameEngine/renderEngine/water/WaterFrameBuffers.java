package eu.tankernn.gameEngine.renderEngine.water;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.loader.textures.TextureUtils;

public class WaterFrameBuffers {
	
	protected static final int REFLECTION_WIDTH = 1024 * 1;
	private static final int REFLECTION_HEIGHT = 1024 * 1;
	
	protected static final int REFRACTION_WIDTH = 1024 / 2;
	private static final int REFRACTION_HEIGHT = 1024 / 2;
	
	private int reflectionFrameBuffer;
	private Texture reflectionTexture;
	private int reflectionDepthBuffer;
	
	private int refractionFrameBuffer;
	private Texture refractionTexture;
	private Texture refractionDepthTexture;
	
	public WaterFrameBuffers() {//call when loading the game
		initialiseReflectionFrameBuffer();
		initialiseRefractionFrameBuffer();
	}
	
	public void cleanUp() {//call when closing the game
		GL30.glDeleteFramebuffers(reflectionFrameBuffer);
		reflectionTexture.delete();
		GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
		GL30.glDeleteFramebuffers(refractionFrameBuffer);
		refractionTexture.delete();
		refractionDepthTexture.delete();
	}
	
	public void bindReflectionFrameBuffer() {//call before rendering to this FBO
		bindFrameBuffer(reflectionFrameBuffer, REFLECTION_WIDTH, REFLECTION_HEIGHT);
	}
	
	public void bindRefractionFrameBuffer() {//call before rendering to this FBO
		bindFrameBuffer(refractionFrameBuffer, REFRACTION_WIDTH, REFRACTION_HEIGHT);
	}
	
	public void unbindCurrentFrameBuffer() {//call to switch to default frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public Texture getReflectionTexture() {//get the resulting texture
		return reflectionTexture;
	}
	
	public Texture getRefractionTexture() {//get the resulting texture
		return refractionTexture;
	}
	
	public Texture getRefractionDepthTexture() {//get the resulting depth texture
		return refractionDepthTexture;
	}
	
	private void initialiseReflectionFrameBuffer() {
		reflectionFrameBuffer = createFrameBuffer();
		reflectionTexture = TextureUtils.createTextureAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		reflectionDepthBuffer = TextureUtils.createDepthBufferAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}
	
	private void initialiseRefractionFrameBuffer() {
		refractionFrameBuffer = createFrameBuffer();
		refractionTexture = TextureUtils.createTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		refractionDepthTexture = TextureUtils.createDepthTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}
	
	private void bindFrameBuffer(int frameBuffer, int width, int height) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}
	
	private int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		//generate name for frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		//create the framebuffer
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		//indicate that we will always render to color attachment 0
		return frameBuffer;
	}
	
	
	
}