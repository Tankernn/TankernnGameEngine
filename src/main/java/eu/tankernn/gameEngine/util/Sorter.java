package eu.tankernn.gameEngine.util;

import java.util.List;
import eu.tankernn.gameEngine.entities.Positionable;

public class Sorter <T extends Positionable> {
	private List<T> list;
	private Positionable centerPoint;
	
	public Sorter (List<T> list, Positionable centerPoint) {
		this.centerPoint = centerPoint;
		this.list = list;
	}
	
	public List<T> sortByDistance() {
		quickSort(0, list.size() -1);
		return list;
		
//		if (objects.size() < 2)
//			return objects;
//		
//		Map<T, Float> lengths = new HashMap<T, Float>();
//		for (T pos: objects)
//			lengths.put(pos, Maths.distanceBetweenPoints(centerPoint.getPosition(), pos.getPosition()));
//		
//		List<T> sortedList = new ArrayList<T>();
//		sortedList.addAll(objects);
//		
//		for (int j = 1; j < sortedList.size(); j++) {
//			for (int i = j; i < sortedList.size(); i++) {
//				if (lengths.get(sortedList.get(i)) < lengths.get(sortedList.get(i - 1))) {
//					T temp = sortedList.get(i);
//					sortedList.set(i, sortedList.get(i - 1));
//					sortedList.set(i - 1, temp);
//				}
//			}
//		}
//		return sortedList;
	}
	
	private void quickSort(int lowIndex, int highIndex) {
        int i = lowIndex;
        int j = highIndex;
        // calculate pivot number, I am taking pivot as middle index number
        float pivot = Maths.distanceBetweenPoints(list.get(lowIndex+(highIndex-lowIndex)/2).getPosition(), centerPoint.getPosition());
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (Maths.distanceBetweenPoints(list.get(i).getPosition(), centerPoint.getPosition()) < pivot) {
                i++;
            }
            while (Maths.distanceBetweenPoints(list.get(j).getPosition(), centerPoint.getPosition()) > pivot) {
                j--;
            }
            if (i <= j) {
                swap(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowIndex < j)
            quickSort(lowIndex, j);
        if (i < highIndex)
            quickSort(i, highIndex);
	}
	
	private void swap(int i, int j) {
		T temp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
	}
}
