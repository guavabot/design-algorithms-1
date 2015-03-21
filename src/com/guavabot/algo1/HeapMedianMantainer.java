package com.guavabot.algo1;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Implements the "Median Maintenance" algorithm.
 * Receives a stream of numbers, arriving one by one. 
 * 
 * Letting xi denote the ith number of the file, the kth median mk is defined as the median of the numbers x1,…,xk. 
 * (So, if k is odd, then mk is ((k+1)/2)th smallest number among x1,…,xk; if k is even, then mk is the (k/2)th smallest number among x1,…,xk.)
 */
public class HeapMedianMantainer {

	private final PriorityQueue<Integer> highHeap; 
	private final PriorityQueue<Integer> lowHeap;
	
	public static void main(String[] args) throws IOException {
		HeapMedianMantainer medianer = new HeapMedianMantainer();
		int medianSum = 0;
		File file = new File("data/Median.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextInt()) {
			int number = scanner.nextInt();
			int median = medianer.addAndGetMedian(number);
			medianSum += median;
		}
		scanner.close();
		System.out.println("Median sum: " + medianSum);
		//prints 46831213
	}
	
	public HeapMedianMantainer() {
		highHeap = new PriorityQueue<Integer>();
		lowHeap = new PriorityQueue<Integer>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
		});
	}

	/**
	 * Uses two heaps to maintain the median every time a number is added
	 * in O(log n) time. 
	 */
	public int addAndGetMedian(int number) {
		Integer heapMedian = lowHeap.peek();
		int median = heapMedian != null ? heapMedian : Integer.MAX_VALUE;
		if (number > median) {
			addToHighHeap(number);
		} else {
			addToLowHeap(number);
		}
		return lowHeap.peek();
	}

	private void addToHighHeap(int number) {
		highHeap.add(number);
		if (highHeap.size() > lowHeap.size()) {
			lowHeap.add(highHeap.poll());
		}
	}

	private void addToLowHeap(int number) {
		lowHeap.add(number);
		if (lowHeap.size() - highHeap.size() >= 2) {
			highHeap.add(lowHeap.poll());
		}
	}
}