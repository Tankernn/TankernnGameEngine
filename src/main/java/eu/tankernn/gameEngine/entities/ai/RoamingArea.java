package eu.tankernn.gameEngine.entities.ai;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

public class RoamingArea {
	private Vector2f a, b;
	private Random rand = new Random();

	public RoamingArea(Vector2f a, Vector2f b) {
		this.a = a;
		this.b = b;
	}
	
	public Vector2f getPointInside() {
		return new Vector2f(a.x + rand.nextFloat() * (b.x - a.x), a.y + rand.nextFloat() * (b.y - a.y));
	}
}
