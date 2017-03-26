package eu.tankernn.gameEngine.postProcessing.bloom;

import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.postProcessing.ImageRenderer;

public class CombineFilter {
	
	private ImageRenderer renderer;
	private CombineShader shader;
	
	public CombineFilter(){
		shader = new CombineShader();
		shader.start();
		shader.connectTextureUnits();
		shader.stop();
		renderer = new ImageRenderer(null);
	}
	
	public void render(Texture colorTexture, Texture bloomTexture){
		shader.start();
		colorTexture.bindToUnit(0);
		bloomTexture.bindToUnit(1);
		renderer.renderQuad();
		shader.stop();
	}
	
	@Override
	public void finalize(){
		renderer.finalize();
		shader.finalize();
	}

}
