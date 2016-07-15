package eu.tankernn.gameEngine.textures;

public class ModelTexture {
	private int textureID;
	private int normalMap;
	private int specularMap;

	private float shineDemper = 1;
	private float reflectivity = 0;

	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	private boolean hasSpecularMap = false;

	private int numberOfRows = 1;

	public ModelTexture(int id) {
		this.textureID = id;
	}

	public int getID() {
		return textureID;
	}

	public boolean hasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public int getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}

	public float getShineDamper() {
		return shineDemper;
	}

	public void setShineDamper(float shineDemper) {
		this.shineDemper = shineDemper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	public void setSpecularMap(int textureId) {
		this.specularMap = textureId;
		this.hasSpecularMap = true;
	}
	
	public boolean hasSpecularMap() {
		return this.hasSpecularMap;
	}
	
	public int getSpecularMap() {
		return this.specularMap;
	}
}
