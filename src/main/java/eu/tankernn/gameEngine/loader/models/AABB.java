package eu.tankernn.gameEngine.loader.models;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.models.obj.ModelData;

public class AABB {
	protected Vector3f position;
	private final Vector3f halfSize, relativeMiddlePos, relativeLb, relativeRt;

	public AABB(Vector3f relativeMiddlePos, Vector3f halfSize) {
		this.relativeMiddlePos = relativeMiddlePos;
		this.halfSize = halfSize;
		relativeLb = Vector3f.sub(relativeMiddlePos, halfSize, null);
		relativeRt = Vector3f.add(relativeMiddlePos, halfSize, null);
	}

	public AABB(ModelData model) {
		Vector3f max = new Vector3f(), min = new Vector3f();
		float[] vertexArray = model.getVertices();

		for (int i = 0; i < vertexArray.length; i += 3) {
			if (vertexArray[i] > max.x)
				max.setX(vertexArray[i]);
			if (vertexArray[i] < min.x)
				min.setX(vertexArray[i]);
		}

		for (int i = 1; i < vertexArray.length; i += 3) {
			if (vertexArray[i] > max.y)
				max.setY(vertexArray[i]);
			if (vertexArray[i] < min.y)
				min.setY(vertexArray[i]);
		}

		for (int i = 2; i < vertexArray.length; i += 3) {
			if (vertexArray[i] > max.z)
				max.setZ(vertexArray[i]);
			if (vertexArray[i] < min.z)
				min.setZ(vertexArray[i]);
		}

		Vector3f fullSize = Vector3f.sub(max, min, null);
		this.halfSize = new Vector3f(fullSize.x / 2, fullSize.y / 2, fullSize.z / 2);
		relativeLb = min;
		relativeRt = max;
		this.relativeMiddlePos = Vector3f.add(relativeLb, relativeRt, null);
		this.relativeMiddlePos.x /= 2;
		this.relativeMiddlePos.y /= 2;
		this.relativeMiddlePos.z /= 2;
	}

	public void updatePosition(Vector3f pos) {
		this.position = pos;
	}

	public static boolean collides(AABB a, AABB b) {
		if (Math.abs(a.getMiddlePos().x - b.getMiddlePos().x) < a.halfSize.x + b.halfSize.x) {
			if (Math.abs(a.getMiddlePos().y - b.getMiddlePos().y) < a.halfSize.y + b.halfSize.y) {
				if (Math.abs(a.getMiddlePos().z - b.getMiddlePos().z) < a.halfSize.z + b.halfSize.z) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean inside(AABB a, Vector3f b) {
		if (Math.abs(a.getMiddlePos().x - b.x) < a.halfSize.x) {
			if (Math.abs(a.getMiddlePos().y - b.y) < a.halfSize.y) {
				if (Math.abs(a.getMiddlePos().z - b.z) < a.halfSize.z) {
					return true;
				}
			}
		}
		return false;
	}

	public Vector3f getLb() {
		return Vector3f.add(position, relativeLb, null);
	}

	public Vector3f getRt() {
		return Vector3f.add(position, relativeRt, null);
	}
	
	public Vector3f getMiddlePos() {
		return Vector3f.add(position, relativeMiddlePos, null);
	}
	
	public Vector3f getSize() {
		return (Vector3f) new Vector3f(halfSize).scale(2);
	}
	
	public AABB copy() {
		return new AABB(new Vector3f(this.relativeMiddlePos), new Vector3f(this.halfSize));
	}
}
