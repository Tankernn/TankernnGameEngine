package eu.tankernn.gameEngine.animation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Bone {
	private String name;
	private List<Bone> children;
	private Bone parent;
	private float length;
	private Vector3f position;
	private Vector4f rotation;
	
	public Bone(String name, float length, Vector3f position, Vector4f rotation) {
		this.name = name;
		this.children = new ArrayList<Bone>();
		this.length = length;
		this.position = position;
		this.rotation = rotation;
	}
	
	public Bone(String[] args) {
		this.children = new ArrayList<Bone>();
		this.position = new Vector3f(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]));
		this.rotation = new Vector4f(Float.parseFloat(args[4]), Float.parseFloat(args[5]), Float.parseFloat(args[6]), Float.parseFloat(args[7]));
		this.length = Float.parseFloat(args[8]);
		this.name = args[9];
	}
	
	public static Bone fromFile(String filename) throws IOException {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		Bone root = new Bone(reader.readLine().split(" "));
		
		while (reader.ready()) {
			String[] line = reader.readLine().split(" ");
			String depthBuffer = line[0];
			int depth = depthBuffer.length() - 1;
			if (depth < 0) {
				System.err.println("Wrong bone depth.");
			}
			
			
		}
		reader.close();
		return root;
	}
	
	public void addChild(Bone child) {
		child.setParent(this);
		this.children.add(child);
	}
	
	public Bone getParent() {
		return parent;
	}
	
	protected void setParent(Bone parent) {
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Bone> getChildren() {
		return children;
	}
	
	public float getLength() {
		return length;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Vector4f getRotation() {
		return rotation;
	}
}
