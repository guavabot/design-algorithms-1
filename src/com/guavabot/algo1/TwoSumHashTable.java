package com.guavabot.algo1;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Uses a hash table to compute the number of target values t in the interval [-10000,10000] (inclusive) 
 * such that there are distinct numbers x,y in the input file that satisfy x+y=t  
 */
public class TwoSumHashTable {
	
	public static void main(String[] args) throws IOException {
		HashSet<Long> set = new HashSet<Long>();
		File file = new File("data/2sum.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLong()) {
			set.add(scanner.nextLong());
		}
		scanner.close();
		
		System.out.println("Set size: " + set.size());
		TwoSumHashTable twoSum = new TwoSumHashTable();
		int count = twoSum.computeCountTwoSums(set, -10_000, 10_000);
		System.out.println("Two sums: " + count);
		//prints: 427
	}
	
	public int computeCountTwoSums(HashSet<Long> set, int minTarget, int maxTarget) {
		int count = 0;
		for (int target = minTarget; target <= maxTarget; target++) {
			if (target % 500 == 0) {
				System.out.println("Current target: " + target + "; current count: " + count);
			}
			if (hasTwoSum(set, target)) {
				count++;
			}
		}
		return count;
	}
	
	private static boolean hasTwoSum(HashSet<Long> set, int target) {
		for (long first : set) {
			long complement = target - first;
			if (first != complement) { //should be distinct
				if (set.contains(complement)) {
					return true;
				}
			}
		}
		return false;
	}
}