package eu.tankernn.gameEngine.postProcessing.gaussianBlur;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.postProcessing.ImageRenderer;
import eu.tankernn.gameEngine.postProcessing.PostProcessingEffect;

public class HorizontalBlur extends PostProcessingEffect<HorizontalBlurShader> {
	
	public HorizontalBlur(int targetFboWidth, int targetFboHeight){
		super(new HorizontalBlurShader());
		shader.start();
		shader.targetWidth.loadFloat(targetFboWidth);
		shader.stop();
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
	}
	
	public void render(Texture colorTexture, Texture brightTexture){
		shader.start();
		colorTexture.bindToUnit(0);
		renderer.renderQuad();
		shader.stop();
		
		outputColorTexture = renderer.getOutputTexture();
		outputBrightTexture = brightTexture;
	}
}
