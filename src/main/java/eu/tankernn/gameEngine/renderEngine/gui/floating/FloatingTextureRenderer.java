package eu.tankernn.gameEngine.renderEngine.gui.floating;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.renderEngine.Vao;
import eu.tankernn.gameEngine.util.ICamera;

public class FloatingTextureRenderer {
	
	private final Vao quad;
	private FloatingTextureShader shader;
	
	public FloatingTextureRenderer(Loader loader, Matrix4f projectionMatrix) {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new FloatingTextureShader();
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(List<FloatingTexture> textures, ICamera camera) {
		Matrix4f viewMatrix = camera.getViewMatrix();
		shader.start();
		quad.bind(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		for (FloatingTexture texture: textures) {
			texture.getTexture().bindToUnit(0);
			Matrix4f modelMatrix = new Matrix4f();
			modelMatrix.setIdentity();
			Matrix4f.translate(texture.getPosition(), modelMatrix, modelMatrix);
			//Sets rotation of model matrix to transpose of rotation of view matrix
			modelMatrix.m00 = viewMatrix.m00;
			modelMatrix.m01 = viewMatrix.m10;
			modelMatrix.m02 = viewMatrix.m20;
			modelMatrix.m10 = viewMatrix.m01;
			modelMatrix.m11 = viewMatrix.m11;
			modelMatrix.m12 = viewMatrix.m21;
			modelMatrix.m20 = viewMatrix.m02;
			modelMatrix.m21 = viewMatrix.m12;
			modelMatrix.m22 = viewMatrix.m22;
			Matrix4f.scale(texture.getScale(), modelMatrix, modelMatrix);
			shader.modelViewMatrix.loadMatrix(Matrix4f.mul(viewMatrix, modelMatrix, null));
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getIndexCount());
		}
		GL11.glDisable(GL11.GL_BLEND);
		quad.unbind(0);
		shader.stop();
	}
	
	public void finalize() {
		shader.finalize();
	}
}
