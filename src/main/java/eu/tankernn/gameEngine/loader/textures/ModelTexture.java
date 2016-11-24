package eu.tankernn.gameEngine.loader.textures;

public class ModelTexture {
	private Texture colorTexture;
	private Texture normalMap;
	private Texture specularMap;

	private float shineDamper = 1;
	private float reflectivity = 0;
	private float refractivity = 0;

	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	private boolean hasSpecularMap = false;

	private int numberOfRows = 1;

	public ModelTexture(Texture texture) {
		this.colorTexture = texture;
	}

	public Texture getTexture() {
		return colorTexture;
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

	public Texture getNormalMap() {
		return normalMap;
	}

	public ModelTexture setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
		return this;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public ModelTexture setShineDamper(float shineDemper) {
		this.shineDamper = shineDemper;
		return this;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public ModelTexture setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
		return this;
	}
	
	public ModelTexture setSpecularMap(Texture texture) {
		this.specularMap = texture;
		this.hasSpecularMap = true;
		return this;
	}
	
	public boolean hasSpecularMap() {
		return this.hasSpecularMap;
	}
	
	public Texture getSpecularMap() {
		return this.specularMap;
	}

	public float getRefractivity() {
		return refractivity;
	}

	public ModelTexture setRefractivity(float refractivity) {
		this.refractivity = refractivity;
		return this;
	}
}
