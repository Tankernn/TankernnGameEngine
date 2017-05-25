package eu.tankernn.gameEngine.particles;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.Camera;
import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.renderEngine.Vao;
import eu.tankernn.gameEngine.renderEngine.Vbo;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	private static final int MAX_INSTANCES = 10000;
	private static final int INSTANCE_DATA_LENGTH = 21;
	
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
	
	private Vao quad;
	private ParticleShader shader;
	
	private Vbo vbo;
	private int pointer = 0;
	
	protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
		this.vbo = Vbo.create(GL15.GL_ARRAY_BUFFER, GL15.GL_STREAM_DRAW, INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		quad = loader.loadToVAO(VERTICES, 2);
		for (int i = 0; i < 5; i++)
			quad.addInstacedAttribute(vbo, i + 1, 4, INSTANCE_DATA_LENGTH, i * 4);
		quad.addInstacedAttribute(vbo, 6, 1, INSTANCE_DATA_LENGTH, 20);
		shader = new ParticleShader();
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
		shader.stop();
	}
	
	protected void render(Map<ParticleTexture, List<IParticle>> particles, Camera camera) {
		Matrix4f viewMatrix = camera.getViewMatrix();
		prepare();
		for (ParticleTexture texture: particles.keySet()) {
			bindTexture(texture);
			List<IParticle> particleList = particles.get(texture);
			pointer = 0;
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
			for (IParticle p: particleList) {
				updateModelViewMatrix(p.getPosition(), p.getRotation(), p.getScale(), viewMatrix, vboData);
				updateTexCoordInfo(p, vboData);
			}
			vbo.updateData(vboData, buffer);
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getIndexCount(), particleList.size());
		}
		finishRendering();
	}
	
	@Override
	protected void finalize() {
		shader.finalize();
	}
	
	private void updateTexCoordInfo(IParticle p, float[] data) {
		data[pointer++] = p.getTexOffset1().x;
		data[pointer++] = p.getTexOffset1().y;
		data[pointer++] = p.getTexOffset2().x;
		data[pointer++] = p.getTexOffset2().y;
		data[pointer++] = p.getBlend();
	}
	
	private void bindTexture(ParticleTexture texture) {
		int blendType = texture.usesAdditiveBlending() ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA;
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, blendType);
		texture.getTexture().bindToUnit(0);
		shader.numberOfRows.loadFloat(texture.getNumberOfRows());
	}
	
	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix, float[] vboData) {
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(position, modelMatrix, modelMatrix);
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
		
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		Matrix4f.rotate((float) Math.toRadians(180), new Vector3f(1, 0, 0), modelMatrix, modelMatrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
		Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
		storeMatrixData(modelViewMatrix, vboData);
	}
	
	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[pointer++] = matrix.m00;
		vboData[pointer++] = matrix.m01;
		vboData[pointer++] = matrix.m02;
		vboData[pointer++] = matrix.m03;
		vboData[pointer++] = matrix.m10;
		vboData[pointer++] = matrix.m11;
		vboData[pointer++] = matrix.m12;
		vboData[pointer++] = matrix.m13;
		vboData[pointer++] = matrix.m20;
		vboData[pointer++] = matrix.m21;
		vboData[pointer++] = matrix.m22;
		vboData[pointer++] = matrix.m23;
		vboData[pointer++] = matrix.m30;
		vboData[pointer++] = matrix.m31;
		vboData[pointer++] = matrix.m32;
		vboData[pointer++] = matrix.m33;
	}
	
	private void prepare() {
		shader.start();
		quad.bind(0, 1, 2, 3, 4, 5, 6);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
	}
	
	private void finishRendering() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		quad.unbind(0, 1, 2, 3, 4, 5, 6);
		shader.stop();
	}
	
}
