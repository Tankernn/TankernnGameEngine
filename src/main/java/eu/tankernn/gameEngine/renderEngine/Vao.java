package eu.tankernn.gameEngine.renderEngine;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class Vao {
	
	protected static final int BYTES_PER_FLOAT = 4;
	private static final int BYTES_PER_INT = 4;
	public final int id;
	private List<Vbo> dataVbos = new ArrayList<Vbo>();
	private Vbo indexVbo;
	private int indexCount;
	
	public static Vao create() {
		int id = GL30.glGenVertexArrays();
		return new Vao(id);
	}
	
	public static Vao create(int indexCount) {
		int id = GL30.glGenVertexArrays();
		return new Vao(id, indexCount);
	}
	
	private Vao(int id) {
		this.id = id;
	}
	
	private Vao(int id, int indexCount) {
		this(id);
		this.indexCount = indexCount;
	}
	
	public int getIndexCount() {
		return indexCount;
	}
	
	public void bind(int... attributes) {
		bind();
		for (int i: attributes) {
			GL20.glEnableVertexAttribArray(i);
		}
	}
	
	public void unbind(int... attributes) {
		for (int i: attributes) {
			GL20.glDisableVertexAttribArray(i);
		}
		unbind();
	}
	
	public void createIndexBuffer(int[] indices) {
		this.indexVbo = Vbo.create(GL15.GL_ELEMENT_ARRAY_BUFFER);
		indexVbo.bind();
		indexVbo.storeData(indices);
		this.indexCount = indices.length;
	}
	
	public void createAttribute(int attribute, float[] data, int attrSize) {
		Vbo dataVbo = Vbo.create(GL15.GL_ARRAY_BUFFER);
		dataVbo.bind();
		dataVbo.storeData(data);
		GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, attrSize * BYTES_PER_FLOAT, 0);
		dataVbo.unbind();
		dataVbos.add(dataVbo);
	}
	
	public void createIntAttribute(int attribute, int[] data, int attrSize) {
		Vbo dataVbo = Vbo.create(GL15.GL_ARRAY_BUFFER);
		dataVbo.bind();
		dataVbo.storeData(data);
		GL30.glVertexAttribIPointer(attribute, attrSize, GL11.GL_INT, attrSize * BYTES_PER_INT, 0);
		dataVbo.unbind();
		dataVbos.add(dataVbo);
	}
	
	public void addInstacedAttribute(Vbo vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
		vbo.bind();
		this.bind();
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4, offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);
		vbo.unbind();
		this.unbind();
	}
	
	public void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		Vbo dataVbo = Vbo.create(GL15.GL_ARRAY_BUFFER, GL15.GL_STATIC_DRAW);
		dataVbo.bind();
		dataVbo.storeData(data);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		dataVbo.unbind();
		dataVbos.add(dataVbo);
	}
	
	public void delete() {
		GL30.glDeleteVertexArrays(id);
		for (Vbo vbo: dataVbos) {
			vbo.delete();
		}
		if (indexVbo != null)
			indexVbo.delete();
	}
	
	private void bind() {
		GL30.glBindVertexArray(id);
	}
	
	private void unbind() {
		GL30.glBindVertexArray(0);
	}
}
