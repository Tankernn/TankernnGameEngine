package eu.tankernn.gameEngine.renderEngine.font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.tankernn.gameEngine.loader.Loader;
import eu.tankernn.gameEngine.loader.font.Font;
import eu.tankernn.gameEngine.loader.font.GUIText;

public class TextMaster {

	private Loader loader;
	private Map<Font, List<GUIText>> texts = new HashMap<>();
	private FontRenderer renderer;

	public TextMaster(Loader load) {
		renderer = new FontRenderer();
		loader = load;
	}

	public void render() {
		for (List<GUIText> l : texts.values()) {
			for (GUIText t : l) {
				if (t.isDirty())
					updateText(t);
			}
		}
		renderer.render(texts);
	}

	public void updateText(GUIText text) {
		text.update(loader);
	}

	public void loadText(GUIText text) {
		updateText(text);
		List<GUIText> textBatch = texts.get(text.getFont());
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(text.getFont(), textBatch);
		}
		textBatch.add(text);
	}

	public void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

	public void finalize() {
		renderer.finalilze();
	}
}
