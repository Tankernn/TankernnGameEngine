package eu.tankernn.gameEngine.loader.colladaLoader;

import eu.tankernn.gameEngine.loader.xmlLoader.XmlNode;
import eu.tankernn.gameEngine.loader.xmlLoader.XmlParser;
import eu.tankernn.gameEngine.util.InternalFile;

public class ColladaLoader {

	public static AnimatedModelData loadColladaModel(InternalFile colladaFile, int maxWeights) {
		XmlNode node = XmlParser.loadXmlFile(colladaFile);

		SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), maxWeights);
		SkinningData skinningData = skinLoader.extractSkinData();

		JointsLoader jointsLoader = new JointsLoader(node.getChild("library_visual_scenes"), skinningData.jointOrder);
		JointsData jointsData = jointsLoader.extractBoneData();

		GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"), skinningData.verticesSkinData);
		MeshData meshData = g.extractModelData();

		return new AnimatedModelData(meshData, jointsData);
	}

	public static AnimationData loadColladaAnimation(InternalFile colladaFile) {
		XmlNode node = XmlParser.loadXmlFile(colladaFile);
		XmlNode animNode = node.getChild("library_animations");
		XmlNode jointsNode = node.getChild("library_visual_scenes");
		AnimationLoader loader = new AnimationLoader(animNode, jointsNode);
		AnimationData animData = loader.extractAnimation();
		return animData;
	}

}
