package eu.tankernn.gameEngine.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.tankernn.gameEngine.animation.model.AnimatedModel;
import eu.tankernn.gameEngine.animation.model.Joint;
import eu.tankernn.gameEngine.loader.animation.AnimationLoader;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.loader.models.collada.AnimatedModelData;
import eu.tankernn.gameEngine.loader.models.collada.ColladaLoader;
import eu.tankernn.gameEngine.loader.models.collada.JointsData;
import eu.tankernn.gameEngine.loader.models.collada.MeshData;
import eu.tankernn.gameEngine.loader.models.obj.ModelData;
import eu.tankernn.gameEngine.loader.models.obj.ObjLoader;
import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.loader.textures.TextureAtlas;
import eu.tankernn.gameEngine.renderEngine.Vao;
import eu.tankernn.gameEngine.util.InternalFile;

/**
 * General purpose loader
 * 
 * @author Frans
 */
public class Loader {
	public static final int MAX_WEIGHTS = 3;
	
	private List<Vao> vaos = new ArrayList<>();
	private List<TextureAtlas> textures = new ArrayList<>();
	private Map<Integer, TexturedModel> models = new HashMap<>();
	private Map<Integer, AABB> boundingBoxes = new HashMap<>();
	
	public Vao loadToVAO(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		return loadToVAO(vertices, textureCoords, normals, null, null, null, indices);
	}
	
	public Vao loadToVAO(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] jointIds, float[] weights, int[] indices) {
		Vao model = Vao.create();
		model.bind();
		model.createIndexBuffer(indices);
		model.createAttribute(0, vertices, 3);
		model.createAttribute(1, textureCoords, 2);
		model.createAttribute(2, normals, 3);
		if (tangents != null)
			model.createAttribute(3, tangents, 3);
		if (jointIds != null)
			model.createIntAttribute(4, jointIds, 3);
		if (weights != null)
			model.createAttribute(5, weights, 3);
		model.unbind();
		vaos.add(model);
		return model;
	}
	
	public Vao loadToVAO(float[] positions, float[] textureCoords) {
		Vao vao = Vao.create();
		vao.bind();
		vao.storeDataInAttributeList(0, 2, positions);
		vao.storeDataInAttributeList(1, 2, textureCoords);
		vao.unbind();
		vaos.add(vao);
		return vao;
	}
	
	public Vao loadToVAO(float[] positions, int dimensions) {
		Vao vao = Vao.create(positions.length / 2);
		vao.bind();
		vao.storeDataInAttributeList(0, dimensions, positions);
		vao.unbind();
		vaos.add(vao);
		return vao;
	}
	
	public Vao loadToVAO(ModelData data) {
		return loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getTangents(), null, null, data.getIndices());
	}
	
	public Vao loadToVAO(MeshData data) {
		return loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getTangents(), data.getJointIds(), data.getVertexWeights(), data.getIndices());
	}
	
	/**
	 * Loads a texture to the GPU.
	 * 
	 * @param filename The path, relative to the root of the jar file, of the
	 *        file to load.
	 * @return The texture ID
	 * @throws FileNotFoundException
	 */
	public TextureAtlas loadTextureAtlas(InternalFile file) {
		TextureAtlas texture = Texture.newTexture(file).create();
		textures.add(texture);
		return texture;
	}

	public Texture loadTexture(InternalFile file) {
		return new Texture(loadTextureAtlas(file));
	}
	
	/**
	 * Creates a new cube map from the images specified. File 0: Right face File
	 * 1: Left face File 2: Top face File 3: Bottom face File 4: Back face File
	 * 5: Front face
	 * 
	 * @param textureFiles Filenames of images that make up the cube map
	 * @return The ID of the new cube map
	 */
	
	public Texture loadCubeMap(InternalFile[] textureFiles) {
		Texture cubeMap = Texture.newCubeMap(textureFiles, 500);
		textures.add(cubeMap.atlas);
		return cubeMap;
	}
	
	private static final int[] CUBE_INDICES = {0, 1, 3, 1, 2, 3, 1, 5, 2, 2, 5, 6, 4, 7, 5, 5, 7, 6, 0, 3, 4, 4, 3, 7, 7, 3, 6, 6, 3, 2, 4, 5, 0, 0, 5, 1};
	
	public Vao generateCube(float size) {
		Vao vao = Vao.create();
		vao.bind();
		vao.createIndexBuffer(CUBE_INDICES);
		vao.createAttribute(0, getCubeVertexPositions(size), 3);
		vao.unbind();
		vaos.add(vao);
		return vao;
	}
	
	private static float[] getCubeVertexPositions(float size) {
		return new float[] {-size, size, size, size, size, size, size, -size, size, -size, -size, size, -size, size, -size, size, size, -size, size, -size, -size, -size, -size, -size};
	}
	
	@Override
	public void finalize() {
		for (TextureAtlas tex: textures)
			tex.delete();
		for (Vao model: vaos)
			model.finalize();
	}
	
	public Vao loadOBJ(InternalFile objFile) {
		ModelData data = ObjLoader.loadOBJ(objFile);
		Vao vao = this.loadToVAO(data);
		boundingBoxes.put(vao.id, new AABB(data));
		return vao;
	}
	
	/**
	 * Creates an AnimatedEntity from the data in an entity file. It loads up
	 * the collada model data, stores the extracted data in a VAO, sets up the
	 * joint hierarchy, and loads up the entity's texture.
	 * 
	 * @param entityFile - the file containing the data for the entity.
	 * @return The animated entity (no animation applied though)
	 */
	public AnimatedModel loadDAE(InternalFile modelFile, ModelTexture texture) {
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(modelFile, MAX_WEIGHTS);
		Vao model = loadToVAO(entityData.getMeshData());
		boundingBoxes.put(model.id, new AABB(entityData.getMeshData()));
		JointsData skeletonData = entityData.getJointsData();
		Joint headJoint = new Joint(skeletonData.headJoint);
		return new AnimatedModel(model, texture, headJoint, skeletonData.jointCount);
	}
	
	public void readModelSpecification(InternalFile file) throws IOException {
		Map<InternalFile, Vao> cachedRawModels = new HashMap<InternalFile, Vao>();
		Map<InternalFile, Texture> cachedTextures = new HashMap<InternalFile, Texture>();
		JSONObject spec;
		
		JSONArray jsonArr = new JSONArray(file.readFile());
		
		for (int j = 0; j < jsonArr.length(); j++) {
			spec = jsonArr.getJSONObject(j);
			
			int id;
			Vao model;
			ModelTexture modelTexture;
			
			id = spec.getInt("id");
			InternalFile modelFile;
			try {
				modelFile = new InternalFile("models/" + optFilename(spec, "model", ".obj"));
			} catch (FileNotFoundException e) {
				modelFile = new InternalFile("models/" + optFilename(spec, "model", ".dae"));
			}
			
			String[] textureFiles = {optFilename(spec, "texture", ".png"), optFilename(spec, "specular", "S.png"), optFilename(spec, "normal", "N.png")};
			
			Texture[] textures = Arrays.stream(textureFiles).map(fileName -> {
				try {
					InternalFile f = new InternalFile("textures/" + fileName);
					if (cachedTextures.containsKey(f)) {
						return cachedTextures.get(f);
					} else {
						Texture t = loadTexture(f);
						cachedTextures.put(f, t);
						return t;
					}
				} catch (FileNotFoundException ex) {
					if (!(ex.getMessage().contains("S.png") || ex.getMessage().contains("N.png")))
						ex.printStackTrace();
					return null;
				}
			}).toArray(size -> new Texture[size]);
			
			modelTexture = new ModelTexture(textures[0]);
			if (textures[1] != null)
				modelTexture.setNormalMap(textures[2]);
			if (textures[2] != null)
				modelTexture.setSpecularMap(textures[1]);
			
			modelTexture.setShineDamper(optFloat(spec, "shinedamper", 10.0f));
			modelTexture.setReflectivity(optFloat(spec, "reflectivity", 0f));
			modelTexture.setRefractivity(optFloat(spec, "refractivity", 0f));
			
			modelTexture.setHasTransparency(spec.optBoolean("transparency"));
			
			if (cachedRawModels.containsKey(modelFile)) {
				model = cachedRawModels.get(modelFile);
				models.put(id, new TexturedModel(model, modelTexture));
			} else {
				switch (modelFile.getExtension()) {
				case "obj":
					model = loadOBJ(modelFile);
					cachedRawModels.put(modelFile, model);
					models.put(id, new TexturedModel(model, modelTexture));
					break;
				case "dae":
					AnimatedModel animatedModel = loadDAE(modelFile, modelTexture);
					String animations = spec.getString("animations");
					animatedModel.registerAnimations(AnimationLoader.loadAnimations(modelFile, new InternalFile("models/" + animations)));
					models.put(id, animatedModel);
					break;
				default:
					throw new UnsupportedOperationException("Unsupported file format: " + modelFile.getExtension());
				}
			}
		}
	}
	
	private float optFloat(JSONObject spec, String key, float defaultValue) {
		return BigDecimal.valueOf(spec.optDouble(key, (double) defaultValue)).floatValue();
	}
	
	private String optFilename(JSONObject spec, String key, String extension) {
		return spec.has(key) ? spec.getString(key) : spec.get("name") + extension;
	}
	
	public TexturedModel getModel(int id) {
		TexturedModel model = models.get(id);
		if (model instanceof AnimatedModel)
			return new AnimatedModel((AnimatedModel) model);
		else
			return model;
	}
	
	public AABB getBoundingBox(int vaoId) {
		if (!boundingBoxes.containsKey(vaoId))
			throw new NullPointerException("Unable to find bounding box for vaoId " + vaoId);
		return boundingBoxes.get(vaoId);
	}
}
