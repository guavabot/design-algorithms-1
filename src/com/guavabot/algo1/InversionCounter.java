package com.guavabot.algo1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Applies a divide and conquer recursive algorithm to count the inversions in an unsorted array.
 */
public class InversionCounter {
	
	public static void main(String[] args) throws IOException {
		List<Integer> integers = new ArrayList<Integer>();
		File file = new File("data/IntegerArray.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextInt()) {
			integers.add(scanner.nextInt());
		}
		scanner.close();
		
		InversionCounter counter = new InversionCounter();
		long inversions = counter.sortAndCountInversions(Utils.listToIntArray(integers));
		System.out.println(inversions + " inversions");
		//2407905288 inversions
	}
	
	/**
	 * The algorithm is based on Merge-Sort; it counts inversions as it sorts.
	 * @param input	Unsorted array
	 * @return	Number of inversions
	 */
	public long sortAndCountInversions(int[] input) {
		int length = input.length;
		if (length <= 1) {
			return 0;
		} else {
			int[] leftPart = Arrays.copyOfRange(input, 0, length / 2);
			int[] rightPart = Arrays.copyOfRange(input, length / 2, length);
			long leftCount = sortAndCountInversions(leftPart);
			long rightCount = sortAndCountInversions(rightPart);
			long splitCount = mergeAndCountSplitInversions(leftPart, rightPart, input);
			return leftCount + rightCount + splitCount;
		}
	}
	
	/**
	 * Inversions inside each part are already counted. Now we only need to count inversions 
	 * split across both sides.
	 * @param output Sorted output array
	 * @return Number of split inversions
	 */
	private long mergeAndCountSplitInversions(int[] sortedLeft, int[] sortedRight, int[] output) {
		long splitCount = 0;
		for (int k = 0, i = 0, j = 0; k < output.length; k++) {
			if (j >= sortedRight.length || 
					i < sortedLeft.length && sortedLeft[i] < sortedRight[j]) {
				//left is smaller; no inversion
				output[k] = sortedLeft[i];
				i++;
			} else {
				output[k] = sortedRight[j];
				j++;
				//right is smaller than at least sortedLeft.length numbers on the left side
				splitCount += sortedLeft.length - i;
			}
		}
		return splitCount;
	}

}
