package eu.tankernn.gameEngine.postProcessing.gaussianBlur;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.postProcessing.ImageRenderer;
import eu.tankernn.gameEngine.postProcessing.PostProcessingEffect;

public class VerticalBlur extends PostProcessingEffect<VerticalBlurShader> {
	
	public VerticalBlur(int targetFboWidth, int targetFboHeight){
		super(new VerticalBlurShader());
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
		shader.start();
		shader.targetHeight.loadFloat(targetFboHeight);
		shader.stop();
	}
	
	public void render(Texture colorTexture, Texture brightTexture){
		shader.start();
		colorTexture.bindToUnit(0);
		renderer.renderQuad();
		shader.stop();
		
		outputBrightTexture = brightTexture;
		outputColorTexture = renderer.getOutputTexture();
	}
}
