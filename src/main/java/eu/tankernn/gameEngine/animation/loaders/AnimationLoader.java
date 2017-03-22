package eu.tankernn.gameEngine.animation.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.animation.animation.Animation;
import eu.tankernn.gameEngine.animation.animation.JointTransform;
import eu.tankernn.gameEngine.animation.animation.KeyFrame;
import eu.tankernn.gameEngine.animation.animation.Quaternion;
import eu.tankernn.gameEngine.loader.colladaLoader.AnimationData;
import eu.tankernn.gameEngine.loader.colladaLoader.ColladaLoader;
import eu.tankernn.gameEngine.loader.colladaLoader.JointTransformData;
import eu.tankernn.gameEngine.loader.colladaLoader.KeyFrameData;
import eu.tankernn.gameEngine.util.InternalFile;

/**
 * This class loads up an animation collada file, gets the information from it,
 * and then creates and returns an {@link Animation} from the extracted data.
 * 
 * @author Karl
 */
public class AnimationLoader {
	
	/**
	 * Loads up a collada animation file, and returns and animation created from
	 * the extracted animation data from the file.
	 * 
	 * @param colladaFile - the collada file containing data about the desired
	 *        animation.
	 * @return The animation made from the data in the file.
	 * @throws IOException 
	 */
	public static Map<String, Animation> loadAnimations(InternalFile colladaFile, InternalFile animationFile) throws IOException {
		Map<String, Animation> animations = new HashMap<>();
		AnimationData animationData = ColladaLoader.loadColladaAnimation(colladaFile);
		int dataIndex = 0;
		BufferedReader r = animationFile.getReader();
		String line;
		while (r.ready()) {
			line = r.readLine();
			String[] split = line.split(" ");
			KeyFrame[] frames = new KeyFrame[Integer.parseInt(split[1])];
			float animationStart = animationData.keyFrames[dataIndex].time;
			for (int i = 0; i < frames.length; i++) {
				frames[i] = createKeyFrame(animationData.keyFrames[dataIndex++], animationStart);
			}
			float animationEnd = animationData.keyFrames[dataIndex -1].time;
			animations.put(split[0], new Animation(animationEnd - animationStart, frames));
		}
		
		return animations;
	}
	
	/**
	 * Creates a keyframe from the data extracted from the collada file.
	 * 
	 * @param data - the data about the keyframe that was extracted from the
	 *        collada file.
	 * @return The keyframe.
	 */
	private static KeyFrame createKeyFrame(KeyFrameData data, float animationStart) {
		Map<String, JointTransform> map = new HashMap<String, JointTransform>();
		for (JointTransformData jointData: data.jointTransforms) {
			JointTransform jointTransform = createTransform(jointData);
			map.put(jointData.jointNameId, jointTransform);
		}
		return new KeyFrame(data.time - animationStart, map);
	}
	
	/**
	 * Creates a joint transform from the data extracted from the collada file.
	 * 
	 * @param data - the data from the collada file.
	 * @return The joint transform.
	 */
	private static JointTransform createTransform(JointTransformData data) {
		Matrix4f mat = data.jointLocalTransform;
		Vector3f translation = new Vector3f(mat.m30, mat.m31, mat.m32);
		Quaternion rotation = new Quaternion(mat);
		return new JointTransform(translation, rotation);
	}
	
}
