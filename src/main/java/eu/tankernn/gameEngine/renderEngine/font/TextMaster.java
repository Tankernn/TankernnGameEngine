package eu.tankernn.gameEngine.renderEngine.font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.tankernn.gameEngine.font.meshCreator.FontType;
import eu.tankernn.gameEngine.font.meshCreator.GUIText;
import eu.tankernn.gameEngine.font.meshCreator.TextMeshData;
import eu.tankernn.gameEngine.loader.Loader;

public class TextMaster {

	private Loader loader;
	private Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
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
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
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

	public void cleanUp() {
		renderer.cleanUp();
	}
}
