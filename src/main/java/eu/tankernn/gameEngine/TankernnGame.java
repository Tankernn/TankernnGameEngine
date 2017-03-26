package eu.tankernn.gameEngine;

import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.renderEngine.DisplayManager;
import eu.tankernn.gameEngine.renderEngine.font.TextMaster;
import eu.tankernn.gameEngine.renderEngine.gui.GuiMaster;

public class TankernnGame {
	protected final String name;
	protected Loader loader;
	protected GuiMaster guiMaster;
	protected TextMaster textMaster;
	
	public TankernnGame(String name) {
		this.name = name;
		this.loader = new Loader();
		this.guiMaster = new GuiMaster(loader);
		this.textMaster = new TextMaster(loader);
	}
	
	public void update() {
		
	}
	
	public void render() {
		guiMaster.render();
		textMaster.render();
		DisplayManager.updateDisplay();
	}
	
	public void cleanUp() {
		textMaster.finalize();
		guiMaster.finalize();
		loader.finalize();
	}
	
	public String getName() {
		return name;
	}
}
