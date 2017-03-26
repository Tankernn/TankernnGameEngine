package eu.tankernn.gameEngine.renderEngine.water;

import eu.tankernn.gameEngine.renderEngine.Fbo;

public class WaterFrameBuffers {

	protected static final int REFLECTION_WIDTH = 1024 * 1;
	private static final int REFLECTION_HEIGHT = 1024 * 1;

	protected static final int REFRACTION_WIDTH = 1024 / 2;
	private static final int REFRACTION_HEIGHT = 1024 / 2;

	private Fbo reflectionBuffer;
	private Fbo refractionBuffer;

	public WaterFrameBuffers() {
		reflectionBuffer = new Fbo(REFLECTION_WIDTH, REFLECTION_HEIGHT, Fbo.DEPTH_RENDER_BUFFER);
		refractionBuffer = new Fbo(REFRACTION_WIDTH, REFRACTION_HEIGHT, Fbo.DEPTH_TEXTURE);
	}
	
	@Override
	public void finalize() {
		reflectionBuffer.finalize();
		refractionBuffer.finalize();
	}

	public Fbo getReflectionFbo() {
		return reflectionBuffer;
	}

	public Fbo getRefractionFbo() {
		return refractionBuffer;
	}

}