package eu.tankernn.gameEngine.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import eu.tankernn.gameEngine.animation.animatedModel.AnimatedModel;
import eu.tankernn.gameEngine.animation.loaders.AnimatedModelLoader;
import eu.tankernn.gameEngine.animation.loaders.AnimationLoader;
import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.loader.obj.ModelData;
import eu.tankernn.gameEngine.loader.obj.ObjLoader;
import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.Vao;
import eu.tankernn.gameEngine.util.InternalFile;

/**
 * General purpose loader
 * 
 * @author Frans
 */
public class Loader {
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Vao> rawModels = new ArrayList<Vao>();
	private List<Texture> textures = new ArrayList<Texture>();
	private Map<Integer, TexturedModel> models = new HashMap<Integer, TexturedModel>();
	private List<AABB> boundingBoxes = new ArrayList<>();
	
	public Vao loadToVAO(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		return loadToVAO(vertices, textureCoords, normals, null, indices);
	}
	
	public Vao loadToVAO(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		Vao model = Vao.create();
		model.bind();
		model.createIndexBuffer(indices);
		model.createAttribute(0, vertices, 3);
		model.createAttribute(1, textureCoords, 2);
		model.createAttribute(2, normals, 3);
		if (tangents != null)
			model.createAttribute(3, tangents, 3);
		model.unbind();
		rawModels.add(model);
		return model;
	}
	
	public int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	
	public Vao loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new Vao(vaoID, positions.length / 2);
	}
	
	public Vao loadToVAO(ModelData data) {
		return (Vao) loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getTangents(), data.getIndices());
	}
	
	/**
	 * Loads a texture to the GPU.
	 * 
	 * @param filename The path, relative to the root of the jar file, of the
	 *        file to load.
	 * @return The texture ID
	 * @throws FileNotFoundException
	 */
	public Texture loadTexture(InternalFile file) {
		Texture texture = Texture.newTexture(file).create();
		textures.add(texture);
		return texture;
	}
	
	public Texture loadTexture(String filename) throws FileNotFoundException {
		return loadTexture(new InternalFile(filename));
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
		textures.add(cubeMap);
		return cubeMap;
	}
	
	private static final int[] CUBE_INDICES = {0, 1, 3, 1, 2, 3, 1, 5, 2, 2, 5, 6, 4, 7, 5, 5, 7, 6, 0, 3, 4, 4, 3, 7, 7, 3, 6, 6, 3, 2, 4, 5, 0, 0, 5, 1};
	
	public Vao generateCube(float size) {
		Vao vao = Vao.create();
		vao.bind();
		vao.createIndexBuffer(CUBE_INDICES);
		vao.createAttribute(0, getCubeVertexPositions(size), 3);
		vao.unbind();
		rawModels.add(vao);
		return vao;
	}
	
	private static float[] getCubeVertexPositions(float size) {
		return new float[] {-size, size, size, size, size, size, size, -size, size, -size, -size, size, -size, size, -size, size, size, -size, size, -size, -size, -size, -size, -size};
	}
	
	public void cleanUp() {
		for (int vao: vaos)
			GL30.glDeleteVertexArrays(vao);
		for (int vbo: vbos)
			GL15.glDeleteBuffers(vbo);
		for (Texture tex: textures)
			tex.delete();
		for (Vao model: rawModels)
			model.delete();
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public Vao loadOBJ(InternalFile objFile) {
		ModelData data = ObjLoader.loadOBJ(objFile);
		boundingBoxes.add(new AABB(data));
		return this.loadToVAO(data);
	}
	
	public AnimatedModel loadDAE(InternalFile colladaFile, ModelTexture texture) {
		return AnimatedModelLoader.loadEntity(colladaFile, texture);
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
						Texture t = loadTexture(f.getPath());
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
					AnimatedModel animatedModel = AnimatedModelLoader.loadEntity(modelFile, modelTexture);
					JSONObject animations = spec.getJSONObject("animations");
					for (Object key: animations.names().toList()) {
						String name = (String) key;
						//TODO Create a file format to specify animation frame ranges, then glue all animations together in Blender before exporting
						animatedModel.registerAnimation(name, AnimationLoader.loadAnimation(modelFile));
					}
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
	
	public int registerModel(int id, TexturedModel model) throws Exception {
		if (models.containsKey(id)) {
			throw new Exception("There is already a model registered for the key " + id + ".");
		} else {
			models.put(id, model);
			return id;
		}
	}
	
	public int registerModel(TexturedModel model) throws Exception {
		if (models.containsValue(model)) {
			Iterator<Entry<Integer, TexturedModel>> i = models.entrySet().iterator();
			
			while (i.hasNext()) {
				Entry<Integer, TexturedModel> e = i.next();
				if (e.getValue().equals(model)) {
					return e.getKey();
				}
			}
			// Should be impossible
			throw new IllegalStateException();
		} else
			return registerModel(models.size(), model);
	}
	
	public TexturedModel getModel(int id) {
		TexturedModel model = models.get(id);
		if (model instanceof AnimatedModel)
			return new AnimatedModel((AnimatedModel) model);
		else
			return model;
	}
	
	public AABB getBoundingBox(int id) {
		return boundingBoxes.get(id);
	}
}
