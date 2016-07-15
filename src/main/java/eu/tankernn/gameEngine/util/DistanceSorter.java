package eu.tankernn.gameEngine.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.tankernn.gameEngine.entities.Positionable;

public class DistanceSorter {
	public static <T extends Positionable> void sort(List<T> list, Positionable centerPoint) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T t1, T t2) {
				return Float.compare(Maths.distanceBetweenPoints(t1.getPosition(), centerPoint.getPosition()),
						Maths.distanceBetweenPoints(t2.getPosition(), centerPoint.getPosition()));
			}
		});
	}
}
