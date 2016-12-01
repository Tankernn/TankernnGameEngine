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
import org.lwjgl.opengl.GL33;

import eu.tankernn.gameEngine.loader.models.AABB;
import eu.tankernn.gameEngine.loader.models.TexturedModel;
import eu.tankernn.gameEngine.loader.obj.ModelData;
import eu.tankernn.gameEngine.loader.obj.ObjLoader;
import eu.tankernn.gameEngine.loader.textures.ModelTexture;
import eu.tankernn.gameEngine.loader.textures.Texture;
import eu.tankernn.gameEngine.renderEngine.RawModel;
import eu.tankernn.gameEngine.util.InternalFile;

/**
 * General purpose loader
 * 
 * @author Frans
 *
 */
public class Loader {
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<RawModel> rawModels = new ArrayList<RawModel>();
	private List<Texture> textures = new ArrayList<Texture>();
	private Map<Integer, TexturedModel> models = new HashMap<Integer, TexturedModel>();

	public RawModel loadToVAO(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		RawModel model = RawModel.create();
		model.storeData(indices, vertices.length / 3, vertices, textureCoords, normals);
		rawModels.add(model);
		return model;
	}

	public RawModel loadToVAO(float[] vertices, float[] textureCoords, float[] normals, float[] tangents,
			int[] indices) {
		RawModel model = RawModel.create();
		model.storeData(indices, vertices.length / 3, vertices, textureCoords, normals, tangents);
		rawModels.add(model);
		return model;
	}

	public int createEmptyVBO(int floatCount) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	public void addInstacedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength,
			int offset) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4, offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	public void updateVBO(int vboID, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return vaoID;
	}

	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / 2);
	}

	public RawModel loadToVAO(ModelData data) {
		return (RawModel) loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getTangents(),
				data.getIndices());
	}

	/**
	 * Loads a texture to the GPU.
	 * 
	 * @param filename
	 *            The path, relative to the root of the jar file, of the file to
	 *            load.
	 * @return The texture ID
	 * @throws FileNotFoundException
	 */
	public Texture loadTexture(String filename) throws FileNotFoundException {
		Texture texture = Texture.newTexture(new InternalFile(filename)).create();
		textures.add(texture);
		return texture;
	}

	/**
	 * Creates a new cube map from the images specified. File 0: Right face File
	 * 1: Left face File 2: Top face File 3: Bottom face File 4: Back face File
	 * 5: Front face
	 * 
	 * @param textureFiles
	 *            Filenames of images that make up the cube map
	 * @return The ID of the new cube map
	 */

	public Texture loadCubeMap(InternalFile[] textureFiles) {
		Texture cubeMap = Texture.newCubeMap(textureFiles, 500);
		textures.add(cubeMap);
		return cubeMap;
	}

	public void cleanUp() {
		for (int vao : vaos)
			GL30.glDeleteVertexArrays(vao);
		for (int vbo : vbos)
			GL15.glDeleteBuffers(vbo);
		for (Texture tex : textures)
			tex.delete();
		for (RawModel model : rawModels)
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

	public RawModel loadOBJ(InternalFile objFile) {
		ModelData data = ObjLoader.loadOBJ(objFile);
		return this.loadToVAO(data).withBoundingBox(data);
	}

	public void readModelSpecification(InternalFile file) throws IOException {
		Map<InternalFile, RawModel> cachedRawModels = new HashMap<InternalFile, RawModel>();
		Map<InternalFile, Texture> cachedTextures = new HashMap<InternalFile, Texture>();
		JSONObject spec;

		JSONArray jsonArr = new JSONArray(file.readFile());

		for (int j = 0; j < jsonArr.length(); j++) {
			spec = jsonArr.getJSONObject(j);

			int id;
			RawModel model;
			ModelTexture modelTexture;

			id = spec.getInt("id");

			InternalFile objFile = new InternalFile(optFilename(spec, "model", ".obj"));

			String[] textureFiles = {
					optFilename(spec, "texture", ".png"), 
					optFilename(spec, "specular", "S.png"),
					optFilename(spec, "normal", "N.png")
			};

			if (cachedRawModels.containsKey(objFile))
				model = cachedRawModels.get(objFile);
			else {
				model = loadOBJ(objFile);
				cachedRawModels.put(objFile, model);
			}

			Texture[] textures = Arrays.stream(textureFiles).map(fileName -> {
				try {
					InternalFile f = new InternalFile(fileName);
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

			models.put(id, new TexturedModel(model, modelTexture));
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
		return models.get(id);
	}

	public AABB getBoundingBox(int id) {
		return getModel(id).getBoundingBox();
	}
}
