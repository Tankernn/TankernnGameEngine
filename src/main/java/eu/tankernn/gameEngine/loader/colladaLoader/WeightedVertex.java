package eu.tankernn.gameEngine.loader.colladaLoader;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.obj.Vertex;

public class WeightedVertex extends Vertex {
	private VertexSkinData weightsData;
	protected WeightedVertex duplicateVertex = null;
	
	public WeightedVertex(int index,Vector3f position, VertexSkinData weightsData){
		super(index, position);
		this.weightsData = weightsData;
	}
	
	public VertexSkinData getWeightsData(){
		return weightsData;
	}

	public WeightedVertex getDuplicateVertex() {
		return duplicateVertex;
	}

	public void setDuplicateVertex(WeightedVertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}
