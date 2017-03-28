package eu.tankernn.gameEngine.audio;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;

public class AudioMaster {
	
	private static final String SOUND_PATH = "sound/";
	
	private Map<String, Integer> buffers = new HashMap<>();
	
	public AudioMaster() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public void setListenerData(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public int loadSound(String file) {
		if (buffers.containsKey(file))
			return buffers.get(file);
		
		int buffer = AL10.alGenBuffers();
		buffers.put(file, buffer);
		WaveData waveFile = WaveData.create(SOUND_PATH + file);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}
	
	public void finalize() {
		for (int buffer: buffers.values()) {
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
	}

	public void setListenerPosition(Vector3f position) {
		setListenerData(position.x, position.y, position.z);
	}
}
