package com.guavabot.algo1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Sorts an array using QuickSort and counts the number of comparisons needed
 * using three different variations to choose a pivot. 
 */
public abstract class QuickSorter {
	
	public static void main(String[] args) throws IOException {
		List<Integer> integers = new ArrayList<Integer>();
		File file = new File("data/QuickSort.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextInt()) {
			integers.add(scanner.nextInt());
		}
		scanner.close();
		
		QuickSorter quickSorter = new QuickSorter1();
		long comparisons = quickSorter.sortAndCountComparisons(quickSorter, Utils.listToIntArray(integers));
		System.out.println(comparisons + " comparisons with first position as pivot");
		//162085 comparisons
		
		quickSorter = new QuickSorter2();
		comparisons = quickSorter.sortAndCountComparisons(quickSorter, Utils.listToIntArray(integers));
		System.out.println(comparisons + " comparisons with last position as pivot");
		//164123 comparisons
		
		quickSorter = new QuickSorter3();
		comparisons = quickSorter.sortAndCountComparisons(quickSorter, Utils.listToIntArray(integers));
		System.out.println(comparisons + " comparisons with median-of-three as pivot");
		//138382 comparisons
	}
	
	static void swap(int[] array, int index1, int index2) {
		int temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}
	
	abstract void movePivotToFirstPosition(int[] array);
	
	public long sortAndCountComparisons(QuickSorter quickSorter, int[] array) {
		if (array.length <= 1) {
			return 0;
		} else {
			movePivotToFirstPosition(array);
			int pivotPos = sortAroundPivot(array);
			long comparisons = array.length - 1; //Pivot was compared once with each other position
			
			//Recurse on left and right parts
			int[] leftPart = Arrays.copyOfRange(array, 0, pivotPos);
			comparisons += sortAndCountComparisons(this, leftPart);
			int[] rightPart = Arrays.copyOfRange(array, pivotPos + 1, array.length);
			comparisons += sortAndCountComparisons(this, rightPart);
			
			return comparisons;
		}
	}

	/**
	 * @return Position of pivot after sorting
	 */
	private int sortAroundPivot(int[] array) {
		//Sort with respect to pivot
		int pivotValue = array[0];
		int center = 1;
		for (int j = 1; j < array.length; j++) {
			if (array[j] < pivotValue) {
				swap(array, j, center);
				center++;
			}
		}
		
		int pivotPos = center - 1;
		swap(array, 0, pivotPos); //Swap pivot in the correct position
		return pivotPos;
	}
	
	/**
	 * First version uses the first position in the array as pivot.
	 */
	static class QuickSorter1 extends QuickSorter {

		@Override
		void movePivotToFirstPosition(int[] array) {
			// Use directly first position as pivot
		}
	}

	/**
	 * Second version uses the last position in the array as pivot.
	 */
	static class QuickSorter2 extends QuickSorter {

		@Override
		void movePivotToFirstPosition(int[] array) {
			swap(array, 0, array.length - 1);
		}
	}

	/**
	 * Third version finds a median-of-three in constant time
	 * between the first, middle and last positions.
	 * That median is used as pivot.
	 */
	static class QuickSorter3 extends QuickSorter {

		@Override
		void movePivotToFirstPosition(int[] array) {
			int first = 0;
			int middle = (array.length - 1) / 2;
			int last = array.length - 1;
			if ((array[middle] > array[first] && array[middle] < array[last])
					|| (array[middle] < array[first] && array[middle] > array[last])) {
				//middle position is median
				swap(array, first, middle);
			} else if ((array[last] > array[first] && array[last] < array[middle])
					|| (array[last] < array[first] && array[last] > array[middle])) {
				//last position is median
				swap(array, first, last);
			}
		}
	}
	
}
