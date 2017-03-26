package eu.tankernn.gameEngine.renderEngine.gui;

import java.util.ArrayList;
import java.util.List;

import eu.tankernn.gameEngine.loader.Loader;

public class GuiMaster {
	private GuiRenderer renderer;
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	
	public GuiMaster(Loader loader) {
		renderer = new GuiRenderer(loader);
	}
	
	public void render() {
		renderer.render(guis);
	}
	
	public void loadGui(GuiTexture gui) {
		guis.add(gui);
	}
	
	public void removeGui(GuiTexture gui) {
		guis.remove(gui);
	}
	
	public List<GuiTexture> getGuis() {
		return guis;
	}

	public void finalize() {
		renderer.finalize();
	}
}
