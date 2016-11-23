package eu.tankernn.gameEngine.postProcessing.gaussianBlur;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.postProcessing.IPostProcessingEffect;
import eu.tankernn.gameEngine.postProcessing.ImageRenderer;

public class VerticalBlur implements IPostProcessingEffect {
	
	private ImageRenderer renderer;
	private VerticalBlurShader shader;
	
	public VerticalBlur(int targetFboWidth, int targetFboHeight){
		shader = new VerticalBlurShader();
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
		shader.start();
		shader.targetHeight.loadFloat(targetFboHeight);
		shader.stop();
	}
	
	public void render(Texture colorTexture, Texture texture){
		shader.start();
		texture.bindToUnit(0);
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
