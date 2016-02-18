package eu.tankernn.gameEngine.animation;

import java.util.List;

public class Animation {
	private int animationID;
	private int length;
	private String name;
	private List<AnimationSection> sections;
	
	public int getAnimationID() {
		return animationID;
	}
	public int getLength() {
		return length;
	}
	public String getName() {
		return name;
	}
	
}
