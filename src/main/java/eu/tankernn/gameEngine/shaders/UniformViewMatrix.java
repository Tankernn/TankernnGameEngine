package eu.tankernn.gameEngine.shaders;

import eu.tankernn.gameEngine.util.ICamera;

public class UniformViewMatrix extends UniformMatrix {

	public UniformViewMatrix(String name) {
		super(name);
	}
	
	public void loadCamera(ICamera camera) {
		super.loadMatrix(camera.getViewMatrix());
	}

}
