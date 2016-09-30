/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Tiraj Parikh
 * trp589
 * 16460
 * Malek Al Sukhni
 * mha664
 * 16470
 * Slip days used: <0>
 * https://github.com/malekAlSukhni/422c_Project3.git
 * Fall 2016
 */

package assignment3;

import java.util.*;
import java.io.*;

public class Main {

	// static variables and constants only here.
	static ArrayList<String> input;
	static String start;
	static String end;

	public static void main(String[] args) throws Exception {

		Scanner kb; // input Scanner for commands
		PrintStream ps; // output file
		// If arguments are specified, read/write from/to files instead of Std
		// IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps); // redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out; // default to Stdout
		}

		ArrayList<String> boom = parse(kb);
		printLadder(getWordLadderBFS(boom.get(0), boom.get(1)));
		System.out.println("DFS");
		printLadder(getWordLadderDFS(boom.get(0), boom.get(1)));
		boom = parse(kb);
		printLadder(getWordLadderBFS(boom.get(0), boom.get(1)));
		System.out.println("DFS");
		printLadder(getWordLadderDFS(boom.get(0), boom.get(1)));
	}

	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests. So call it
		// only once at the start of main.
	}

	/**
	 * @param keyboard
	 *            Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. If
	 *         command is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		ArrayList<String> parsedWords = new ArrayList<String>(2);
		parsedWords.add(keyboard.next().toUpperCase());
		if (parsedWords.get(0).equals("/QUIT")) {
			System.exit(0);
		} else {
			start = parsedWords.get(0).toLowerCase();
		}
		parsedWords.add(keyboard.next().toUpperCase());
		if (parsedWords.get(1).equals("/QUIT")) {
			System.exit(0);
		} else {
			end = parsedWords.get(1).toLowerCase();
		}
		return parsedWords;
	}

	/**
	 * Returned list should be ordered start to end. Include start and end.
	 * Return empty list if no ladder.
	 * 
	 * @param start
	 *            is first word entered by user
	 * @param end
	 *            is second word input by user
	 * @return ladder if full or empty
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) throws StackOverflowError {
		// initialize variables
		if (start.length() == end.length()) {
			boolean gotIt = false;

			Set<String> dict;

			ArrayList<String> wordL;
			Set<String> hs;

			// error handling
			try {
				dict = makeDictionary();
				wordL = new ArrayList<String>();
				hs = new HashSet<String>();
				hs.add(start);
				wordL.add(start);

				gotIt = recursDFS(dict, hs, wordL, start, end);
			} catch (StackOverflowError e) {
				// if there is a stack overflow attempt to find list form the
				// other
				// direction
				dict = makeDictionary();
				wordL = new ArrayList<String>();
				hs = new HashSet<String>();
				hs.add(end);
				wordL.add(end);

				gotIt = recursDFS(dict, hs, wordL, end, start);
				Collections.reverse(wordL);
			}
			/*We tried to optimize but it didn't work for all cases :(
			// if list size less than 3 no need to optimize
			if (wordL.size() > 3) {
				wordL = optimizeList(wordL);
			}
			*/

			if (gotIt) {
				return wordL;
			}
		}
		return new ArrayList<String>();
	}

	/**
	 * Finds a word ladder using BFS
	 * 
	 * @param start
	 *            starting word
	 * @param end
	 *            ending word
	 * @return a word ladder
	 */
	public static ArrayList<String> getWordLadderBFS(String start, String end) {

		Set<String> dict = makeDictionary();
		Queue<Node> queue = new LinkedList<Node>();
		Node prevNode = new Node(start, null);
		queue.add(prevNode);
		dict.remove(start);
		// while the queue isn't empty keep going
		while (!queue.isEmpty()) {
			prevNode = queue.remove();
			StringBuilder current = new StringBuilder(prevNode.word);
			// did you reach the end, if so return the associated ladder
			if (current.toString().equals(end)) {
				return prevNode.createNodeList();
			}
			// find all permutations and add them to the queue if theyre in the
			// dictionary
			for (int placeToChange = 0; placeToChange < start.length(); placeToChange++) {
				char letter = current.charAt(placeToChange);
				for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
					current.setCharAt(placeToChange, alphabet);
					if (dict.contains(current.toString())) {
						Node currNode = new Node(current.toString(), prevNode);
						queue.add(currNode);
						// remove word from dictionary to avoid duplicate values
						// in queue
						dict.remove(current.toString());
					}

				}
				current.setCharAt(placeToChange, letter);
			}
		}

		return new ArrayList<String>(); // return no ladder
	}

	public static Set<String> makeDictionary() {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner(new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}

	/**
	 * prints out ladder if properly made, if not print error message
	 * 
	 * @param ladder
	 *            is word ladder
	 */
	public static void printLadder(ArrayList<String> ladder) {
		// print message for no ladder found
		if (ladder.isEmpty()) {

			System.out.println("no word ladder can be found between " + start + " and " + end + ".");
		}
		// print out ladder and size
		else {

			System.out.println(
					"a " + (ladder.size() - 2) + "-rung word ladder exists between " + start + " and " + end + ".");

			for (int i = 0; i < ladder.size(); i++) {
				System.out.println(ladder.get(i).toLowerCase());
			}
		}
	}

	/**
	 * recursively creates word ladder
	 * 
	 * @param dict
	 *            is set made by words in dictionary
	 * @param hs
	 *            is set of hash values
	 * @param wordL
	 *            is the word ladder
	 * @param start
	 *            is the first word of the ladder
	 * @param end
	 *            is the last word of the ladder
	 * @return true if word ladder has been completed, false if not completed
	 */
	private static boolean recursDFS(Set<String> dict, Set<String> hs, ArrayList<String> wordL, String start,
			String end) {
		// initialize variables
		boolean flag = false;

		int count = 0;
		int curr = 0;

		String temp = start;
		String[] startTemp = start.split("");
		String[] endTemp = end.split("");

		// edge case
		if (start.equals(end)) {
			return true;
		}

		// loop through first and last word and set curr to index of same letter
		for (int i = 0; i < end.length(); i++) {

			if ((startTemp[i]).equals(endTemp[i])) {
				curr = i + 1;

				if (curr >= end.length()) {
					curr = 0;
					break;
				}
			}
		}
		// loop until count reaches length of first word
		while (count != start.length() - 1) {
			// add each letter to temp variable to test
			for (int j = 'A'; j <= 'Z'; j++) {
				String s = "";
				char tempChar = ((char) j);
				startTemp[curr] = String.valueOf(tempChar);
				// change start to each individual combination of letters
				for (int k = 0; k < startTemp.length; k++) {
					s += startTemp[k];
				}
				start = s;
				// recursively complete DFS after comparing to dicitonary
				if (dict.contains(start.toUpperCase()) && !hs.contains(start)) {
					hs.add(start);
					wordL.add(start);
					dict.remove(start);
					flag = recursDFS(dict, hs, wordL, start, end);

					if (flag) {
						return true;
					}
					wordL.remove(start);
					break;
				}
				hs.add(start);
			}
			// iterate through start
			start = temp.toString();
			startTemp = start.split("");
			if (curr != start.length() - 1) {
				curr++;
			} else {
				curr = 0;
			}
			count++;
		}
		return false;
	}

	/**
	 * optimizes list, gets rid of words with the same increment as the previous
	 * 
	 * @param arrayList
	 *            list to optimize
	 * @return optimized list
	 */
	private static ArrayList<String> optimizeList(ArrayList<String> arrayList) {
		ArrayList<String> optomizedList = new ArrayList<String>();
		int diffIndex = findDiffIndex(arrayList.get(0), arrayList.get(1));
		optomizedList.add(arrayList.get(0));
		for (int index = 0; index < arrayList.size() - 2; index++) {
			int indexToCheck = findDiffIndex(arrayList.get(index), arrayList.get(index + 1));
			if (diffIndex != indexToCheck) {
				optomizedList.add(arrayList.get(index));
				diffIndex = indexToCheck;
			}
		}

		optomizedList.add(arrayList.get(arrayList.size() - 1));
		return optomizedList;

	}

	/**
	 * finds the letter thats the difference between two index's
	 * 
	 * @param s1
	 *            string to check
	 * @param s2
	 *            string to check
	 * @return index thats different
	 */
	private static int findDiffIndex(String s1, String s2) {
		for (int index = 0; index < s1.length(); index++) {
			if (s1.charAt(index) != s2.charAt(index)) {
				return index;
			}
		}
		return -1;
	}
}
