package eu.tankernn.gameEngine.util;

import java.util.Collections;
import java.util.List;

public class DistanceSorter {
	public static <T extends IPositionable> void sort(List<T> list, IPositionable centerPoint) {
		Collections.sort(list, (t1, t2) -> {
			return Float.compare(Maths.distanceBetweenPoints(t1.getPosition(), centerPoint.getPosition()), Maths.distanceBetweenPoints(t2.getPosition(), centerPoint.getPosition()));
		});
	}
}
