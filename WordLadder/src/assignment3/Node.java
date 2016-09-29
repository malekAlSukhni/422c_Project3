package assignment3;

import java.util.*;

public class Node {
	
	public String word;
	public Node previous;
	
	public Node (String m, Node p){
		this.word = m;
		this.previous = p;
	}
	
	public ArrayList<String> createNodeList(){
		ArrayList<String> list = new ArrayList<String>();
		list.add(this.word);
		while(previous != null){
			list.add(previous.word);
			previous = previous.previous;
		}
		
		Collections.reverse(list);
		return list;
	}
	
}
