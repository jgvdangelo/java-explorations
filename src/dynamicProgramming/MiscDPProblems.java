package dynamicProgramming;

import java.util.*;
import java.awt.Point;

public class MiscDPProblems {
	// Problem: given a grid, print out all the paths to the top right
	// 	Note on problem - I will do this bottom-up and top-down
	public static void travelRecBottomUp(int x, int y, int cx, int cy, List<String> path) {
		if (cx == x && cy == y) {
			for (String str : path) {
				System.out.print(str + " ");
			}
			System.out.println();
			return;
		}

		if (cy < y) {
			path.add("N");
			travelRecBottomUp(x, y, cx, cy + 1, path);
			path.remove(path.size() - 1);
		}
		if (cx < x) {
			path.add("E");
			travelRecBottomUp(x, y, cx + 1, cy, path);
			path.remove(path.size() - 1);
		}
		if (cy < y && cx < x) {
			path.add("NE");
			travelRecBottomUp(x, y, cx + 1, cy + 1, path);
			path.remove(path.size() - 1);
		}
	}
	
	public static List<List<String>> travelRecTopDown(int x, int y) {
		if (x == 0 && y == 0) {
			return new ArrayList<List<String>>();
		}

		List<List<String>> pathEast = null;
		if (x > 0) {
			pathEast = travelRecTopDown(x - 1, y);
			processPathsList(pathEast, "E");
		}

		List<List<String>> pathNorth = null;
		if (y > 0) {
			pathNorth = travelRecTopDown(x, y - 1);
			processPathsList(pathNorth, "N");
		}

		List<List<String>> pathNorthEast = null;
		if (y > 0 && x > 0) {
			pathNorthEast = travelRecTopDown(x - 1, y - 1);
			processPathsList(pathNorthEast, "NE");
		}

		List<List<String>> ret = new ArrayList<List<String>>();
		addAllIfNotNull(ret, pathEast);
		addAllIfNotNull(ret, pathNorth);
		addAllIfNotNull(ret, pathNorthEast);

		return ret;
	}

	private static void addAllIfNotNull(List<List<String>> ret, List<List<String>> toAdd) {
		if (toAdd != null) {
			ret.addAll(toAdd);
		}
	}

	private static void processPathsList(List<List<String>> paths, String direction) {
		if (paths.isEmpty()) {
			List<String> path = new ArrayList<String>();
			path.add(direction);
			paths.add(path);
		} else {
			for (int i = 0; i < paths.size(); i++) {
				paths.get(i).add(direction);
			}
		}
	}

	public static void travel(int x, int y) {
		travelRecBottomUp(x, y, 0, 0, new ArrayList<String>());
		System.out.println();
		List<List<String>> paths = travelRecTopDown(x, y);
		for (List<String> list : paths) {
			for (String str : list) {
				System.out.print(str + " ");
			}
			System.out.println();
		}
	}
	
	// Problem: find the maximum sum that can be generated by adding elements of L that does not exceed n
	public static int maxSumBURec(int n, int[] arr, int currIndex, int currSum) {
		int withCurrent = currSum + arr[currIndex];
		if (currIndex == arr.length - 1) {
			if (withCurrent <= n)
				return withCurrent;
			else
				return currSum;
		} else {
			int with = 0;
			if (withCurrent < n) {
				with = maxSumBURec(n, arr, currIndex + 1, withCurrent);
			}
			int without = maxSumBURec(n, arr, currIndex + 1, currSum);
			if (with <= n && without <= n) {
				return Math.max(with,without);
			} else if (with > n && without > n) {
				return 0;
			} else if (without < n){
				return without;
			} else {
				return with;
			}
		}
	}
	
	public static int maxSumBUGreedyTab(int n, int[] arr) {
		int[] sums = new int[arr.length];
		int cSum = 0;
		int triage = cSum;
		for (int i = 0; i < arr.length; i++) {
			triage = cSum + arr[i];
			if (triage < n) {
				sums[i] = triage;
			} else {
				sums[i] = cSum;
			}
			cSum = sums[i];
		}
		
		return sums[arr.length - 1];
	}
	
	public static int maxSumTDRec(int n, int[] arr) {
		return 0;
	}
	
	public static void maxSum(int n, int[] arr) {
		int greedy = maxSumBUGreedyTab(n, arr);
		int nonGreedy = maxSumBURec(n, arr, 0, 0);
		System.out.println("GREEDY = " + greedy + ", NON-GREEDY = " + nonGreedy);
	}
	
	// Problem: given a number, re-order the digits so that they are in sorted order
	public static void reorderDigits(int n) {
		int[] counts = new int[10];
		
		while (n != 0) {
			counts[n % 10]++;
			n /= 10;
		}
		
		int output = 0;
		for (int i = 0; i < counts.length; i++) {
			for (int j = 0; j < counts[i]; j++) {
				output = output * 10 + i;
			}
		}
		
		System.out.println(output);
	}
	
	public static void main(String[] args) {
		travel(2, 2);
		maxSum(19, new int[] {7, 30, 8, 22, 6, 1, 14});
		maxSum(42, new int[] {5, 30, 15, 13, 8});
		reorderDigits(78876589);
	}
}
