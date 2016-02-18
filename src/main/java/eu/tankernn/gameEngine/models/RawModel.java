package eu.tankernn.gameEngine.models;

public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	//private AABB boundingBox;
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		//this.boundingBox = boundingBox;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
//	public AABB getBoundingBox() {
//		return boundingBox;
//	}
}
