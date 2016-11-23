package eu.tankernn.gameEngine.postProcessing.bloom;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.postProcessing.IPostProcessingEffect;
import eu.tankernn.gameEngine.postProcessing.ImageRenderer;

public class BrightFilter implements IPostProcessingEffect {

	private ImageRenderer renderer;
	private BrightFilterShader shader;
	
	public BrightFilter(int width, int height){
		shader = new BrightFilterShader();
		renderer = new ImageRenderer(width, height);
	}
	
	@Override
	public void render(Texture colorTexture, Texture brightTexture) {
		shader.start();
		colorTexture.bindToUnit(0);
		renderer.renderQuad();
		shader.stop();
	}
	
	public Texture getOutputTexture(){
		return renderer.getOutputTexture();
	}
	
	public void cleanUp(){
		renderer.cleanUp();
		shader.cleanUp();
	}
	
}
