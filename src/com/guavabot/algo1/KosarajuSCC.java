package com.guavabot.algo1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Uses Kosaraju's algorithm to find strongly connected components in a graph by adjacency list.  
 */
public class KosarajuSCC {
	
	private int sourceVertex; //the current source vertex from the outer loop of DFS
	private int currentFinishingTime;
	
	public static void main(String[] args) throws IOException {
		Map<Integer, Node> graph = new HashMap<Integer, Node>();
		
		File file = new File("data/SCC.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextInt()) {
			int node1Num = scanner.nextInt();
			int node2Num = scanner.nextInt();
			
			Node node1 = graph.get(node1Num);
			if (node1 == null) {
				node1 = new Node(node1Num);
				graph.put(node1Num, node1);
			}
			Node node2 = graph.get(node2Num);
			if (node2 == null) {
				node2 = new Node(node2Num);
				graph.put(node2Num, node2);
			}
			
			List<Node> edges = node1.getEdges();
			edges.add(node2);
			List<Node> invertedEdges = node2.getInvertedEdges();
			invertedEdges.add(node1);
		}
		scanner.close();
		System.out.println("graph size: " + graph.keySet().size());
		
		KosarajuSCC kosaraju = new KosarajuSCC();
		kosaraju.findSCC(new HashSet<Node>(graph.values()));
		/**
		 *  Group 1: 434821 nodes
			Group 2: 968 nodes
			Group 3: 459 nodes
			Group 4: 313 nodes
			Group 5: 211 nodes
		 */
	}
	
	/**
	 * Uses Kosaraju's algorithm, which is based on doing a depth first search 
	 * first on an inverted graph (all edges pointing in opposite direction)
	 * and then on the normal graph to find strongly connected components. 
	 * @param graph Normal graph
	 * @param invertedGraph Graph with all edges inverted
	 * @param maxNode
	 */
	public void findSCC(Set<Node> graph) {
		//Round One: Run DFS on the inverted graph and find the finishing times
		currentFinishingTime = 0;
		for (Node node : graph) {
			if (!node.isExplored()) {
				sourceVertex = node.getNumber();
				depthFirstSearchInverted(node);
			}
		}
		
		//Sort nodes in reverse finishing time
		List<Node> nodes = new ArrayList<>(graph);
		nodes.sort(new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return Integer.compare(o2.getFinishingTime(), o1.getFinishingTime());
			}
		});
		
		//Round Two: Run DFS in order of last finishing time of round one
		for (Node node : nodes) {
			node.setExplored(false);
		}
		for (Node node : nodes) {
			if (!node.isExplored()) {
				sourceVertex = node.getNumber();
				depthFirstSearchNormal(node);
			}
		}
		
		//Group Strongly Connected Components by their leader
		final Map<Integer, Integer> leaderToCount = new HashMap<Integer, Integer>();
		for (Node node : graph) {
			int lead = node.getLeader();
			if (lead >= 0) {
				Integer count = leaderToCount.get(lead);
				if (count == null) {
					count = 0;
				}
				leaderToCount.put(lead, ++count);
			}
		}
		
		//Sort SCC by size
		List<Integer> leaders = new ArrayList<Integer>(leaderToCount.keySet());
		leaders.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				int size1 = leaderToCount.get(o1);
				int size2 = leaderToCount.get(o2);
				return Integer.compare(size2, size1);
			}
		});
		
		int written = 0;
		for (int lead : leaders) {
			System.out.println("Group " + (++written) + ": " + leaderToCount.get(lead) + " nodes");
			if (written == 10) break;
		}
	}
	
	private void depthFirstSearchInverted(Node node) {
		node.setExplored(true);
		for (Node other : node.getInvertedEdges()) {
			if (!other.isExplored()) {
				depthFirstSearchInverted(other);
			}
		}
		currentFinishingTime++;
		node.setFinishingTime(currentFinishingTime);
	}
	
	private void depthFirstSearchNormal(Node node) {
		node.setExplored(true);
		node.setLeader(sourceVertex);
		for (Node other : node.getEdges()) {
			if (!other.isExplored()) {
				depthFirstSearchNormal(other);
			}
		}
	}
	
	private static class Node {
		final int number;
		List<Node> edges;
		List<Node> invertedEdges;
		boolean explored = false;
		int leader = -1;
		int finishingTime = -1;
		
		public Node(int number) {
			this.number = number;
		}

		public int getNumber() {
			return number;
		}

		public boolean isExplored() {
			return explored;
		}

		public void setExplored(boolean explored) {
			this.explored = explored;
		}

		public int getLeader() {
			return leader;
		}

		public void setLeader(int leader) {
			this.leader = leader;
		}

		public int getFinishingTime() {
			return finishingTime;
		}

		public void setFinishingTime(int finishingTime) {
			this.finishingTime = finishingTime;
		}

		public List<Node> getEdges() {
			if (edges == null) {
				edges = new ArrayList<Node>();
			}
			return edges;
		}

		public List<Node> getInvertedEdges() {
			if (invertedEdges == null) {
				invertedEdges = new ArrayList<Node>();
			}
			return invertedEdges;
		}
		
	}
}
