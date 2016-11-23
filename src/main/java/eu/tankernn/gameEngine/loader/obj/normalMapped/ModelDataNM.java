package eu.tankernn.gameEngine.loader.obj.normalMapped;

import eu.tankernn.gameEngine.loader.obj.ModelData;

public class ModelDataNM extends ModelData {
	private float[] tangents;

	public ModelDataNM(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices,
			float furthestPoint) {
		super(vertices, textureCoords, normals, indices, furthestPoint);
		this.tangents = tangents;
	}

	public float[] getTangents(){
		return tangents;
	}

}
