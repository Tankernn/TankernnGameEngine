package eu.tankernn.gameEngine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.tankernn.gameEngine.entities.Positionable;

public class Sorter {
	public static <T extends Positionable> List<T> sortByDistance(Positionable centerPoint, List<T> objects) {
		if (objects.size() < 2)
			return objects;
		
		Map<T, Float> lengths = new HashMap<T, Float>();
		for (T pos : objects)
			lengths.put(pos, Maths.distanceBetweenPoints(centerPoint.getPosition(), pos.getPosition()));
		
		List<T> sortedList = new ArrayList<T>();
		sortedList.addAll(objects);
		
		for (int j = 1; j < sortedList.size(); j++) {
			for (int i = j; i < sortedList.size(); i++) {
				if (lengths.get(sortedList.get(i)) < lengths.get(sortedList.get(i - 1))) {
					T temp = sortedList.get(i);
					sortedList.set(i, sortedList.get(i - 1));
					sortedList.set(i - 1, temp);
				}
			}
		}
		return sortedList;
	}
}
