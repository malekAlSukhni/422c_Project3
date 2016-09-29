/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Tiraj Parikh
 * trp589
 * 16460
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Fall 2016
 */

package assignment3;

import java.util.*;
import java.io.*;

public class Main {

	// static variables and constants only here.
	static ArrayList<String> input;
	

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

		//initialize();
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
		System.out.println("enter 2 words sep by space");
		parsedWords.add(keyboard.next());
		if(parsedWords.get(0).equals("exit/")){
			System.exit(0);
		}
		parsedWords.add(keyboard.next());
		if(parsedWords.get(1).equals("exit/")){
			System.exit(0);
		}
		return parsedWords;
	}

	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		
		boolean gotIt = false;
		
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		
		ArrayList<String> wordL = new ArrayList<String>();		
		Set<String> hs = new HashSet<String>();
		
		hs.add(start);
		wordL.add(start);
		
		gotIt = recursDFS(dict, hs, wordL, start, end);
		
		if (gotIt){ return wordL; }
		
		else {
		return null; // replace this line later with real return
		}
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		Set<String> dict = makeDictionary();
		Queue<Node> queue = new LinkedList<Node>();
		Node prevNode = new Node(start, null);
		queue.add(prevNode);
		dict.remove(start);
		while (!queue.isEmpty()) {
			prevNode = queue.remove();
			StringBuilder current = new StringBuilder(prevNode.word);
			if (current.toString().equals(end)) {
				return prevNode.createNodeList();
			}
			for (int placeToChange = 0; placeToChange < start.length(); placeToChange++) {
				char letter = current.charAt(placeToChange);
				for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
					current.setCharAt(placeToChange, alphabet);
					if (dict.contains(current.toString())) {
						Node currNode = new Node(current.toString(), prevNode);
						queue.add(currNode);
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

	public static void printLadder(ArrayList<String> ladder) {
		
		String start = ladder.get(0);
		String end = ladder.get(1);
		int n = ladder.size() - 2;
		
		if(ladder.isEmpty()){
		
			System.out.println("no word ladder can be found between " 
			+ start + " and " + end + ".");
		}
		
		else { 
			
			System.out.println("a " + n + "-rung word ladder exists between "
					+ start + " and " + end + ".");
		
			for (int i = 0; i < ladder.size(); i++){
				System.out.println(ladder.get(i) + "\n");
			}
		}
	}
	// TODO
	// Other private static methods here
	
	private static boolean recursDFS(Set<String> dict, Set<String> hs,	
			ArrayList<String> wordL, String start, String end) {

		boolean flag = false;
		
		int count = 0;
		int i,j,k = 0;
		int curr = 0;
		
		String temp = start;
		String[] startTemp = start.split("");
		String[] endTemp = end.split("");
		
		if (start.equals(end)){
			return true;
		}		
		
		for (i = 0; i < end.length(); i++){
			
			if ((startTemp[i]).equals(endTemp[i])){
				curr = i + 1;
				
				if (curr >= end.length()){
					curr = 0;
					break;
				}
			}
		}
		
		while (count != start.length() - 1){
			
			for (j = 'a'; j < 'z'; j++){
				String s = "";
				char tempChar = ((char)j);
				startTemp[curr] = String.valueOf(tempChar);
				
				for (k = 0; k < startTemp.length; k++) {
					s += startTemp[k];
				}
				start = s;
				
				if (dict.contains(start.toUpperCase()) && !hs.contains(start)){
					hs.add(start);
					wordL.add(start);
					flag = recursDFS(dict, hs, wordL, start, end);
					
					if (flag){
						return true;
					}
					wordL.remove(start);
					break;
				}	
				hs.add(start);
			}
			start = temp.toString();
			startTemp = start.split("");
			if (curr != start.length() - 1){
				curr++;
			} 
			else {
				curr = 0;
			}
		count++;
		}
		
		return false;
	}
}
