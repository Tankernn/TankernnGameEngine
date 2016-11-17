package eu.tankernn.gameEngine.loader.models;

import org.lwjgl.util.vector.Vector3f;

public class AABB {
	protected Vector3f middlePos, halfSize;
	
	public AABB(Vector3f middlePos, Vector3f halfSize)
	{
		this.middlePos = middlePos;
		this.halfSize = halfSize;
	}
	
//	public AABB(Vector3f middlePos, RawModel model) {
//		float maxX, maxY, maxZ;
//		
//	}
	
	public static boolean collides(AABB a, AABB b)
	{
		if (Math.abs(a.middlePos.x - b.middlePos.x) < a.halfSize.x + b.halfSize.x)
		{
			if (Math.abs(a.middlePos.y - b.middlePos.y) < a.halfSize.y + b.halfSize.y)
			{
				if (Math.abs(a.middlePos.z - b.middlePos.z) < a.halfSize.z + b.halfSize.z)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean inside(AABB a, Vector3f b)
	{
		if (Math.abs(a.middlePos.x - b.x) < a.halfSize.x)
		{
			if (Math.abs(a.middlePos.y - b.y) < a.halfSize.y)
			{
				if (Math.abs(a.middlePos.z - b.z) < a.halfSize.z)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public Vector3f getLb() {
		return new Vector3f(middlePos.x - halfSize.x, middlePos.y/* - halfSize.y*/, middlePos.z - halfSize.z);
	}
	
	public Vector3f getRt() {
		return new Vector3f(middlePos.x + halfSize.x, middlePos.y + halfSize.y, middlePos.z + halfSize.z);
	}
}
