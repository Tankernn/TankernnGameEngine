package eu.tankernn.gameEngine.loader.colladaLoader;

import eu.tankernn.gameEngine.loader.obj.ModelData;

public class MeshData extends ModelData {

	private static final int DIMENSIONS = 3;

	private int[] jointIds;
	private float[] vertexWeights;

	public MeshData(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices,
			int[] jointIds, float[] vertexWeights, float furthestPoint) {
		super(vertices, textureCoords, normals, tangents, indices, furthestPoint);
		this.jointIds = jointIds;
		this.vertexWeights = vertexWeights;
	}

	public int[] getJointIds() {
		return jointIds;
	}
	
	public float[] getVertexWeights(){
		return vertexWeights;
	}

	public int getVertexCount() {
		return getVertices().length / DIMENSIONS;
	}

}
