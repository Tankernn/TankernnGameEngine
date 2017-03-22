package eu.tankernn.gameEngine.renderEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public class Vbo {
	
	private final int vboId;
	private final int type;
	private final int usage;
	
	private Vbo(int vboId, int type, int usage){
		this.vboId = vboId;
		this.type = type;
		this.usage = usage;
	}
	
	private Vbo(int vboId, int type, int usage, int size) {
		this(vboId, type, usage);
		bind();
		GL15.glBufferData(type, size * 4, usage);
		unbind();
	}

	public static Vbo create(int type, int usage, int size) {
		int id = GL15.glGenBuffers();
		return new Vbo(id, type, usage, size);
	}
	
	public static Vbo create(int type, int usage){
		int id = GL15.glGenBuffers();
		return new Vbo(id, type, usage);
	}
	
	public static Vbo create(int type) {
		return create(type, GL15.GL_STATIC_DRAW);
	}
	
	public void bind(){
		GL15.glBindBuffer(type, vboId);
	}
	
	public void unbind(){
		GL15.glBindBuffer(type, vboId);
	}
	
	public void storeData(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		storeData(buffer);
	}
	
	public void storeData(FloatBuffer data){
		GL15.glBufferData(type, data, usage);
	}
	
	public void storeData(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		storeData(buffer);
	}
	
	public void storeData(IntBuffer data){
		GL15.glBufferData(type, data, usage);
	}

	public void updateData(float[] data) {
		updateData(data, BufferUtils.createFloatBuffer(data.length));
	}
	public void updateData(float[] data, FloatBuffer buffer) {
		bind();
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		GL15.glBindBuffer(type, vboId);
		GL15.glBufferData(type, buffer.capacity() * 4, usage);
		GL15.glBufferSubData(type, 0, buffer);
		GL15.glBindBuffer(type, 0);
		unbind();
	}
	
	public void delete(){
		GL15.glDeleteBuffers(vboId);
	}
}
