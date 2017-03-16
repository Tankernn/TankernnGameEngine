package eu.tankernn.gameEngine.animation.loaders;

import eu.tankernn.gameEngine.animation.animatedModel.AnimatedModel;
import eu.tankernn.gameEngine.animation.animatedModel.Joint;
import eu.tankernn.gameEngine.loader.colladaLoader.AnimatedModelData;
import eu.tankernn.gameEngine.loader.colladaLoader.ColladaLoader;
import eu.tankernn.gameEngine.loader.colladaLoader.JointData;
import eu.tankernn.gameEngine.loader.colladaLoader.JointsData;
import eu.tankernn.gameEngine.loader.colladaLoader.MeshData;
import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.renderEngine.Vao;
import eu.tankernn.gameEngine.util.InternalFile;

public class AnimatedModelLoader {
	
	public static final int MAX_WEIGHTS = 3;

	/**
	 * Creates an AnimatedEntity from the data in an entity file. It loads up
	 * the collada model data, stores the extracted data in a VAO, sets up the
	 * joint heirarchy, and loads up the entity's texture.
	 * 
	 * @param entityFile
	 *            - the file containing the data for the entity.
	 * @return The animated entity (no animation applied though)
	 */
	public static AnimatedModel loadEntity(InternalFile modelFile, ModelTexture texture) {
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(modelFile, MAX_WEIGHTS);
		Vao model = createVao(entityData.getMeshData());
		JointsData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);
		return new AnimatedModel(model, texture, headJoint, skeletonData.jointCount);
	}

	/**
	 * Constructs the joint-hierarchy skeleton from the data extracted from the
	 * collada file.
	 * 
	 * @param data
	 *            - the joints data from the collada file for the head joint.
	 * @return The created joint, with all its descendants added.
	 */
	private static Joint createJoints(JointData data) {
		Joint joint = new Joint(data.index, data.nameId, data.bindLocalTransform);
		for (JointData child : data.children) {
			joint.addChild(createJoints(child));
		}
		return joint;
	}

	/**
	 * Stores the mesh data in a VAO.
	 * 
	 * @param data
	 *            - all the data about the mesh that needs to be stored in the
	 *            VAO.
	 * @return The VAO containing all the mesh data for the model.
	 */
	private static Vao createVao(MeshData data) {
		Vao vao = Vao.create();
		vao.bind();
		vao.createIndexBuffer(data.getIndices());
		vao.createAttribute(0, data.getVertices(), 3);
		vao.createAttribute(1, data.getTextureCoords(), 2);
		vao.createAttribute(2, data.getNormals(), 3);
		vao.createIntAttribute(4, data.getJointIds(), 3);
		vao.createAttribute(5, data.getVertexWeights(), 3);
		vao.unbind();
		return vao;
	}

}
