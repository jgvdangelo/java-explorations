package misc;

import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.io.*;

public class MiscProblems {
	// Problem to find max memory being used when multiple tasks are scheduled
	public static void processScheduling(String file) {
		Scanner scan;
		try {
			scan = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		int lines = scan.nextInt();
		int start; int end; int weight; int currWeight;
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
		for (int i = 0; i < lines; i++) {
			currWeight = 0;
			start = scan.nextInt();
			end = scan.nextInt();
			weight = scan.nextInt();
			if (map.containsKey(start)) {
				currWeight = map.get(start);
			}
			map.put(start, currWeight + weight);
			currWeight = 0;
			if (map.containsKey(end)) {
				currWeight = map.get(end);
			}
			map.put(end, currWeight - weight);
		}
		scan.close();
		int max = 0; int curr = 0;
		for (Integer key : map.keySet()) {
			curr += map.get(key);
			if (curr > max) {
				max = curr;
			}
		}
		System.out.println(map.toString());
		System.out.println(max);
	}
	
	// Problem: giving the minimum change for a price
	public static int[] minimumChange(int price, int[] denominations) {
		int[] change = new int[denominations.length];
		
		int currIndex = denominations.length - 1;
		while (price != 0 && currIndex >= 0) {
			change[currIndex] = price / denominations[currIndex];
			price = price % denominations[currIndex];
			currIndex--;
		}
		
		return change; 
	}
	
	// Problem: Modify the array by moving all the zeros to the end (right side).
	private static void swap(int[] arr, int first, int second) {
		int temp = arr[first];
		arr[first] = arr[second];
		arr[second] = temp;
	}
	
	private static int findNextNonZeroIndex(int[] arr, int currIndex) {
		boolean nonZeroFound = false;
		while (currIndex >= 0 && !nonZeroFound) {
			if (arr[currIndex] != 0) {
				nonZeroFound = true;
			} else {
				currIndex--;
			}
		}
		
		return currIndex;
	}
	
	private static int findNextZeroIndex(int[] arr, int currIndex) {
		boolean zeroFound = false;
		while (currIndex < arr.length && !zeroFound) {
			if (arr[currIndex] == 0) {
				zeroFound = true;
			} else {
				currIndex++;
			}
		}
		
		return currIndex;
	}
	
	public static int[] moveZeros(int[] arr) {
		if (arr.length <= 1) {
			return arr;
		} 
		
		int nonZeroIndex = findNextNonZeroIndex(arr, arr.length - 1);
		int zeroIndex = findNextZeroIndex(arr, 0);
		if (nonZeroIndex == -1 || zeroIndex == arr.length || zeroIndex > nonZeroIndex) {
			// passed an array of either all zeros or non-zeros, or sorted
			return arr;
		}
		
		while (zeroIndex < nonZeroIndex) {
			swap(arr, zeroIndex, nonZeroIndex);
			zeroIndex = findNextZeroIndex(arr, zeroIndex);
			nonZeroIndex = findNextNonZeroIndex(arr, nonZeroIndex);
		}
		
		return arr;
	}
	
	// Problem to find the next permutation of an integer
	public static int nextPermute(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		
		int[] counts = new int[10];
		
		int prevDigit = n % 10;
		counts[prevDigit]++;
		n /= 10;
		int currDigit = n % 10;
		n /= 10;
		while (n != 0 && currDigit >= prevDigit) {
			prevDigit = currDigit;
			counts[prevDigit]++;
			currDigit = n % 10;
			n /= 10;
		}
		
		if (currDigit >= prevDigit) {
			throw new IllegalArgumentException();
		}
		
		counts[currDigit]++;
		
		int nextUp = -1;
		boolean found = false;
		int i = currDigit + 1;
		while (i < counts.length && !found) {
			if (counts[i] != 0) {
				nextUp = i;
				found = true;
			}
			i++;
		}
		
		counts[nextUp]--;
		n = n * 10 + nextUp;
		
		for (i = 0; i < counts.length; i++) {
			for (int j = 0; j < counts[i]; j++) {
				n = n * 10 + i;
			}
		}
		
		return n;
	}
	
	// Swap two strings without using a temp variable
	public static void swapStrings(String[] str) {
		System.out.println(Arrays.toString(str));
		str[0] = str[0] + str[1];
		str[1] = str[0].substring(0, str[0].length() - str[1].length());
		str[0] = str[0].substring(str[1].length());
		System.out.println(Arrays.toString(str));
	}
	
	// Calculate difference between two clock hands
	public static float determineAngle(int hour, int minutes) {
		// calc hour angle from 12
		// calc minute angle from 12
		// subtract the two + have some sort of math.abs etc.
		
		if (hour == 12) {
			hour = 0;
		}
		
		float hourAngleFrom12 = hour * 30f + (0.5f * minutes);
		float minuteAngleFrom12 = minutes * 6f;
		
		float diff = Math.abs(hourAngleFrom12 - minuteAngleFrom12);
		if (diff > 180) {
			diff = 360 - diff;
		}

		return diff;
	}

	
	public static String splitIntoGroups(String str, int groupSize) {
		Queue<Character> temp = new LinkedList<Character>();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != '-') {
				temp.add(str.charAt(i));
			}
		}

		StringBuffer ret = new StringBuffer();
		int leftOver = temp.size() % groupSize;
		while (leftOver > 0 && !temp.isEmpty()) {
			ret.append(temp.remove());
	    leftOver--;
		}

		if (ret.length() != 0) {
			ret.append('-');
		}

		int count = 0;
		while (!temp.isEmpty()) {
			if (count < groupSize) {
				ret.append(temp.remove());
				count++;
			} else if (count >= groupSize) {
				count = 0;
				ret.append('-');
			} 
		}

		return ret.toString();
	}
	
	// Problem: given K lists of sorted integers, find the smallest range that includes at least one number from each of the k-lists
	public static int[] findSmallestRange(int[][] lists) {
		TreeMap<Integer, Integer> queue = new TreeMap<Integer, Integer>();

		int[] indices = new int[lists.length];

		for (int i = 0; i < lists.length; i++) {
			queue.put(lists[i][0], i);
			indices[i]++;
		}

		int minRange = getRange(queue);
		int[] rangeVals = getRangeVals(queue);

		boolean finished = false;
		int listToReplace;
		while (!finished) {
			listToReplace = queue.get(queue.firstKey());
			queue.remove(queue.firstKey());
			if (indices[listToReplace] == lists[listToReplace].length) {
				finished = true;
			} else {
				queue.put(lists[listToReplace][indices[listToReplace]], listToReplace);
				indices[listToReplace]++;
				if (minRange > getRange(queue)) {
					minRange = getRange(queue);
					rangeVals = getRangeVals(queue);
				}
			}
		}

		return rangeVals;
	}

	public static int getRange(TreeMap<Integer, Integer> queue) {
		return queue.lastKey() - queue.firstKey();
	}

	public static int[] getRangeVals(TreeMap<Integer, Integer> queue) {
		return new int[] {queue.firstKey(), queue.lastKey()};
	}
	
	public static void smallestListsRangeRecursive(int[][] lists) {
		int length = 0;
		for (int i = 0; i < lists.length; i++) {
			length += lists[i].length;
		}

		int[] listNum = new int[length];
		int[] vals = new int[length];
		int[] indices = new int[lists.length];

		int valsIndex = 0;

		TreeMap<Integer, Integer> priorityMap = new TreeMap<Integer, Integer>();
		for (int i = 0; i < lists.length; i++) {
			priorityMap.put(lists[i][0], i);
			indices[i]++;
		}

		int lowestVal = 0;
		int lowestValList = 0;
		int nextVal = 0;
		while (valsIndex < vals.length) {
			lowestVal = priorityMap.firstKey();
			lowestValList = priorityMap.get(lowestVal);
			vals[valsIndex] = lowestVal;
			listNum[valsIndex] = lowestValList;

			priorityMap.remove(lowestVal);
			valsIndex++;
			if (indices[lowestValList] < lists[lowestValList].length) {
				nextVal = lists[lowestValList][indices[lowestValList]];
				priorityMap.put(nextVal, lowestValList);
				indices[lowestValList]++;
			}
		}

		int[] range = findSmalllestRangeRecursiveHelper(lists.length, listNum, vals, 0);
		System.out.println("From " + vals[range[0]] + " to " + vals[range[1]]);
	} 

	public static int[] findSmalllestRangeRecursiveHelper(int k, int[] listNum, int[] values, int startIndex) {
		if (startIndex > listNum.length - k)
			return null;
		boolean subsetFound = false;
		boolean[] listFound = new boolean[k];
		int[] ret = new int[2];
		int endIndex = startIndex + k - 1;
		int list;
		while (!subsetFound && endIndex < listNum.length) {
			for (int i = startIndex; i <= endIndex; i++) {
				list = listNum[i];
				if (!listFound[list])
					listFound[list] = true;
			}
			subsetFound = true;
			for (int i = 0; i < listFound.length; i++) {
				if (!listFound[i])
					subsetFound = false;
			}

			if (!subsetFound) {
				endIndex++;
			} else {
				ret[0] = startIndex;
				ret[1] = endIndex;
			}
		}

		if (ret[0] == ret[1])
			return null;
		else {
			int[] nextIndex = findSmalllestRangeRecursiveHelper(k, listNum, values, startIndex + 1);
			if (nextIndex == null) 
				return ret;
			else {
				return (values[nextIndex[1]] - values[nextIndex[0]] < values[ret[1]] - values[ret[0]]) ? nextIndex : ret;
			}
		}
	}
	
	// Problem: print out all numbers less than n in string-order
	public static void printStringOrder(int n) {
		int width = 0;
		int widthTemp = n;
		while (widthTemp > 0) {
			width++;
			widthTemp /= 10;
		}
		for (int i = 1; i <= 9; i++) {
			helper(i, n, 0, width);
		}
	}

	private static void helper(int cNum, int mNum, int cLevel, int mLevel) {
		if (cLevel == mLevel && cNum <= mNum) {
			System.out.println(cNum);
		} else if (cLevel < mLevel && cNum <= mNum) {
			System.out.println(cNum);
			for (int i = 0; i <= 9; i++) {
				helper(cNum * 10 + i, mNum, cLevel + 1, mLevel);
			}
		}
	}
	
	// Problem: check if a number is a perfect square, using only addition or subtraction
	public static boolean isPerfectSquare(int n) {
		if (n == 0 || n == 1)
			return true;

		int diff = 3;
		int currRes = 1;
		while (currRes < n) {
			currRes += diff;
			diff += 2;
		}

		return (currRes == n);
	}
	
	// from here: https://www.careercup.com/question?id=5661939564806144
	public static void printDiag(int[][] mat) {
		Queue<DiagPoint> q = new LinkedList<DiagPoint>();

		q.add(new DiagPoint(0, 0, 0));

		DiagPoint curr;
		while (!q.isEmpty()) {
			curr = q.remove();
			if (q.isEmpty() || q.peek().layer != curr.layer) {
				System.out.println(mat[curr.i][curr.j]);
			} else {
				System.out.print(mat[curr.i][curr.j]);
			}

			addIfValid(curr.i + 1, curr.j, curr.layer + 1, mat, q);
			addIfValid(curr.i, curr.j + 1, curr.layer + 1, mat, q);
		}
	}

	private static void addIfValid(int i, int j, int layer, int[][] mat, Queue<DiagPoint> q) {
		if (i < mat.length && i >= 0 && j < mat[0].length && j >= 0) {
			DiagPoint toAdd = new DiagPoint(i, j, layer);
			if (!q.contains(toAdd)) {
				q.add(toAdd);
			}
		}
	}

	public static class DiagPoint {
		int i; int j; int layer;

		public DiagPoint(int i, int j, int layer) {
			this.i = i;
			this.j = j;
			this.layer = layer;
		}

		@Override
		public boolean equals(Object obj) {
			 if (!(obj instanceof DiagPoint))
						return false;
				if (obj == this)
						return true;

				DiagPoint rhs = (DiagPoint) obj;
				return (rhs.i == this.i && rhs.j == this.j);
		}
	}
	
	// Problem: https://www.careercup.com/question?id=5698055485521920
	public static void pagesRange(int d, int k) {
		List<Integer> sequence = new LinkedList<Integer>();

		int levels = 0;
		int t = k;
		while (t != 0) {
			t /= 10;
			levels++;
		}

		populateSequenceUpToK(levels, d, sequence, 0, false);
		System.out.println(sequence);
	}

	public static void populateSequenceUpToK(int level, int d, List<Integer> sequence, int soFar, boolean findAll) {
		if (level > 1) {
			for (int i = 0; i < 10; i++) {
				if (i != d) {
					populateSequenceUpToK(level - 1, d, sequence, soFar * 10 + i, findAll);
				} else {
					populateSequenceUpToK(level - 1, d, sequence, soFar * 10 + i, true);
				}
			}
		} else if (level == 1) {
			if (findAll) {
				for (int i = 0; i < 10; i++) {
					populateSequenceUpToK(level - 1, d, sequence, soFar * 10 + i, true);
				}
			} else {
				populateSequenceUpToK(level - 1, d, sequence, soFar * 10 + d, false);
			}
		} if (level == 0) {
			sequence.add(soFar);
		}
	}
	
	// From here: https://www.careercup.com/question?id=14370695
	public static int[] plusPlus(int[] num) {
		int leftOver = 1;
		int currIndex = 0;
		int currIndexSum = 0;
		while (leftOver != 0 && currIndex < num.length) {
			currIndexSum = num[currIndex] + leftOver;
			num[currIndex] = currIndexSum % 10;
			leftOver = currIndexSum / 10;
			currIndex++;
		}

		if (leftOver > 0) {
			num = Arrays.copyOf(num, num.length + 1);
			num[num.length - 1] = leftOver;
		}

		return num;
	}
	
	// Problem: Program a simple calculator, ex input: "((4+5)*8)-3/2"
	public static float parseExpression(String str) {
		Queue<String> tokens = new LinkedList<String>();
		Queue<Character> operators = new LinkedList<Character>();

		fillQueues(tokens, operators, str);

		float ret = parseToken(tokens.remove());

		float next = 0.0f;
		while (!operators.isEmpty()) {
			char operator = operators.remove();
			next = parseToken(tokens.remove());

			ret = operateToken(ret, next, operator);
		}

		return ret;
	}

	public static void fillQueues(Queue<String> tokens, Queue<Character> operators, String expr) {
		int nestedParamCount = 0;
		// TODO: check for invalid input
		int tokenIndexStart = 0;
		for (int i= 0; i < expr.length(); i++) {
			if (expr.charAt(i) == '(') {
				nestedParamCount++;
			} else if (expr.charAt(i) == ')') {
				nestedParamCount--;
			}

			if (nestedParamCount == 0) {
				if (i == expr.length() - 1) {
					tokens.add(expr.substring(tokenIndexStart));
				} else if (isOperator(expr.charAt(i))) {
					tokens.add(expr.substring(tokenIndexStart, i));
					operators.add(expr.charAt(i));
					tokenIndexStart = i + 1;
				}
			}
		}
	}

	public static boolean isOperator(char c) {
		return (c == '+' || c == '-' || c == '/' || c == '*');
	}

	public static float parseToken(String token) {
		if (token.charAt(0) == '(' && token.charAt(token.length() - 1) == ')') {
			return parseExpression(token.substring(1, token.length() - 1));
		} else {
			return Integer.parseInt(token);
		}
	}

	public static float operateToken(float ret, float next, char operator) {
		switch (operator) {
			case ('+'):
				return ret + next;
			case ('-'):
				return ret - next;
			case ('/'):
				return ret / next;
			case ('*'):
				return ret * next;
			default:
				return -1;
		}
	}
	
	// Problem, from here: https://careercup.com/question?id=5693718846767104
	public static int getLongestPalindromeChunk(String str) {
		 if (str.length() == 1)
			return 1;
		 char lastLetter = str.charAt(str.length() - 1);

		 int index = 1;
		 while (index < str.length() / 2) {
			if (str.charAt(index) == lastLetter && wordsMatchAtBothEnds(index, str)) {
				return 2 + getLongestPalindromeChunk(removeStartEndChunk(index, str));
			}
			index++;
		 }

		 return 1;
	} 

	public static String removeStartEndChunk(int endIndex, String str) {
		return str.substring(endIndex + 1, str.length() - endIndex - 1);
	}

	public static boolean wordsMatchAtBothEnds(int endIndex, String str) {	
		for (int i = 0; i <= endIndex; i++) {
			if (str.charAt(i) != str.charAt(str.length() - 1 - endIndex + i)) {
				return false;
			}
		}
		return true;
	}
	
	// Problem, from here: https://careercup.com/question?id=5754527530614784
	public static void printWorkHierarchy(Map<String, List<String>> map) {
		Map<String, Person> mapToPerson = new HashMap<String, Person>();

		Person curr;
		List<Person> reports;
		for (String str : map.keySet()) {
			if (mapToPerson.containsKey(str)) {
				curr = mapToPerson.get(str);
			} else {
				curr = new Person(str); 
				mapToPerson.put(str, curr);
			}
			reports = new LinkedList<Person>();
			for (String reportName : map.get(str)) {
				if (mapToPerson.containsKey(reportName)) {
					reports.add(mapToPerson.get(reportName));
					mapToPerson.get(reportName).manager = curr;
				} else {
					Person newPerson = new Person(reportName);
					mapToPerson.put(reportName, newPerson);
					newPerson.manager = curr;
					reports.add(newPerson);
				}
			}
			curr.reports = reports;
		}

		boolean found = false;
		Person root = null;
		Map.Entry<String, Person> pair = null;
		Iterator<Entry<String, Person>> it = mapToPerson.entrySet().iterator();
		while (!found && it.hasNext()) {
			pair = it.next();
			if (pair.getValue().manager == null) {
				root = pair.getValue();
				found = true;
			}
		}

		if (root == null) {
			throw new IllegalArgumentException();
		}

		printInOrder(root, 0);
	}

	private static void printInOrder(Person node, int level) {
		String toPrint = "";
		for (int i = 0; i < level; i++) 
			toPrint += "	";
		toPrint += "-";
		System.out.println(toPrint + node.name);
		for (Person report : node.reports) {
			printInOrder(report, level + 1);
		}
	}

	public static class Person {
		String name;
		Person manager;
		List<Person> reports;

		public Person(String name) {
			this.name = name;
		}
	}
	
	// Problem http://www.practice.geeksforgeeks.org/problem-page.php?pid=389
	public static void jumpingNumbers(int n) { 
		if (n >= 0) {
			System.out.print("0 ");
		}
		for (int i = 1; i <= 9; i++) {
			printJumps(n, i);
		}
	}

	private static void printJumps(int n, int numSoFar) {
		if (numSoFar > n) {  
			return;
		} else {
			System.out.print(numSoFar + " ");
			int lastDigit = numSoFar % 10;
			if (lastDigit > 0) {
				printJumps(n, numSoFar * 10 + lastDigit - 1);
			}
			if (lastDigit < 9) {
				printJumps(n, numSoFar * 10 + lastDigit + 1);
			}
		}
	}
	
	// Problem: meghan's Quad problem
	public static Quad logicalAnd(Quad q1, Quad q2) {
		if (q1.isColor() && q2.isColor()) {
			Quad ret = new Quad(true);
			ret.colored = q1.colored && q2.colored;
			return ret;
		} else {
			Quad ret = new Quad(false);
			if (!q1.colored && !q2.colored) {
				for (int i = 0; i < 4; i++) {
					ret.grid.add(helper(q1.get(i), q2.get(i)));
				}
			} else {
				Quad toIterate;
				Quad toPass;
				if (q1.isColor() && !q2.isColor()) {
					toIterate = q2;
					toPass = q1;
				} else {
					toIterate = q1;
					toPass = q2;
				}
				for (int i = 0; i < 4; i++) {
					ret.grid.add(helper(toPass, toIterate.get(i)));
				}
			}
			return ret;
		}
	}

	public static class Quad {
		List<Quad> grid;
		boolean colored;
		boolean isAColorQuad;

		public boolean isColor() { 
			if ((grid == null && !isAColorQuad) || (grid != null && isAColorQuad)) {
				throw new IllegalArgumentException();
			return (isAColorQuad);
		}

		public Quad(boolean isColored) {
			if (isColored) {
				isAColorQuad = true;
				colored = false;
			} else {
				grid = new List<Quad>();
			}
		}
	}

	public static void main(String[] args) {
		processScheduling("scheduling");
		System.out.println(Arrays.toString(minimumChange(69, new int[] {1, 5, 10, 25})));
		int n = 10111;
		System.out.println("Original: " + n + ", Modified: " + nextPermute(n));
		System.out.println(determineAngle(3,30));
		System.out.println(splitIntoGroups("a-b-c-d-e", 2));
		//printStringOrder(20); 
		System.out.println(isPerfectSquare(44));
		pagesRange(4, 3);
		jumpingNumbers(50);
	}
}
