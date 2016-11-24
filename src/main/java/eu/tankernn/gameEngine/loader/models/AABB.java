package eu.tankernn.gameEngine.loader.models;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.loader.obj.ModelData;

public class AABB {
	protected Vector3f middlePos = new Vector3f(0, 0, 0), halfSize;

	public AABB(Vector3f middlePos, Vector3f halfSize) {
		this.middlePos = middlePos;
		this.halfSize = halfSize;
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
	}

	public void updatePosition(Vector3f pos) {
		this.middlePos = new Vector3f(pos);
		this.middlePos.setY(pos.y + halfSize.y);
	}

	public static boolean collides(AABB a, AABB b) {
		if (Math.abs(a.middlePos.x - b.middlePos.x) < a.halfSize.x + b.halfSize.x) {
			if (Math.abs(a.middlePos.y - b.middlePos.y) < a.halfSize.y + b.halfSize.y) {
				if (Math.abs(a.middlePos.z - b.middlePos.z) < a.halfSize.z + b.halfSize.z) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean inside(AABB a, Vector3f b) {
		if (Math.abs(a.middlePos.x - b.x) < a.halfSize.x) {
			if (Math.abs(a.middlePos.y - b.y) < a.halfSize.y) {
				if (Math.abs(a.middlePos.z - b.z) < a.halfSize.z) {
					return true;
				}
			}
		}
		return false;
	}

	public Vector3f getLb() {
		return new Vector3f(middlePos.x - halfSize.x, middlePos.y - halfSize.y, middlePos.z - halfSize.z);
	}

	public Vector3f getRt() {
		return new Vector3f(middlePos.x + halfSize.x, middlePos.y + halfSize.y, middlePos.z + halfSize.z);
	}
	
	public AABB copy() {
		return new AABB(new Vector3f(this.middlePos), new Vector3f(this.halfSize));
	}
}
