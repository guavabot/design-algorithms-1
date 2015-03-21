package com.guavabot.algo1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Applies Dijkstra's algorithm using a heap to find the shortest path from a vertex 
 * to all other vertices of a graph by adjacency list. 
 * 
 * The input file contains an adjacency list representation of an undirected weighted 
 * graph with 200 vertices labeled 1 to 200. Each row consists of the node tuples 
 * that are adjacent to that particular vertex along with the length of that edge.
 */
public class Dijkstra {
	
	private static final int DEFAULT_DISTANCE = 1000000;
	
	public static void main(String[] args) throws IOException {
		Node startNode = null;
		Map<Integer, Node> graph = new HashMap<>();
		File file = new File("data/dijkstraData.txt");
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("[,\\s]");
		while (scanner.hasNextLine()) {
			int nodeNum = scanner.nextInt();
			Node node = graph.get(nodeNum);
			if (node == null) {
				node = new Node(nodeNum);
				graph.put(nodeNum, node);
			}
			
			List<Edge> edges = node.getEdges();
			while (scanner.hasNextInt()) {
				int node2Num = scanner.nextInt();
				Node node2 = graph.get(node2Num);
				if (node2 == null) {
					node2 = new Node(node2Num);
					graph.put(node2Num, node2);
				}
				int weight = scanner.nextInt();
				edges.add(new Edge(node2, weight));
			}
			
			if (startNode == null) startNode = node;
			scanner.nextLine();
		}
		scanner.close();
		System.out.println("graph size: " + graph.size());
		
		Dijkstra dijkstra = new Dijkstra();
		dijkstra.findShortestPaths(startNode);
		
		//Extra: print distance to certain vertices
		int[] requiredVertices = {7,37,59,82,99,115,133,165,188,197};
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < requiredVertices.length; i++) {
			builder.append(graph.get(requiredVertices[i]).getShortestPath()).append(",");
		}
		System.out.println(builder); //2599,2610,2947,2052,2367,2399,2029,2442,2505,3068
	}
	
	/**
	 * Finds the shortest path using a heap.
	 * 
	 * @param graph List of nodes with a map of connected edges to their weight.
	 * 				First element corresponds to node 1.
	 * @param startNode 
	 * @return Array of shortest distances from the startNode. Item 0 corresponds to node 1.
	 */
	public void findShortestPaths(Node startNode) {
		startNode.setShortestPath(0);
		PriorityQueue<Node> heap = new PriorityQueue<Node>();
		heap.add(startNode);
		
		while (!heap.isEmpty()) {
			Node node = heap.poll();
			node.setExplored();
			for (Edge edge : node.getEdges()) {
				Node other = edge.getNode();
				if (!other.isExplored()) {
					int newDistance = node.getShortestPath() + edge.getWeight();
					int currentDistance = other.getShortestPath();
					if (newDistance < currentDistance) {
						other.setShortestPath(newDistance);
						heap.remove(other);
						heap.add(other);
					}
				}
			}
		}
	}
	
	private static class Node implements Comparable<Node> {
		private int number;
		private List<Edge> edges;
		private boolean explored;
		private int shortestPath = DEFAULT_DISTANCE;
		
		public Node(int number) {
			this.number = number;
		}
		
		public List<Edge> getEdges() {
			if (edges == null) {
				edges = new ArrayList<>();
			}
			return edges;
		}
		
		public boolean isExplored() {
			return explored;
		}
		
		public void setExplored() {
			explored = true;
		}
		
		public int getShortestPath() {
			return shortestPath;
		}
		
		public void setShortestPath(int shortestPath) {
			this.shortestPath = shortestPath;
		}
		
		@Override
		public int compareTo(Node o) {
			return Integer.compare(this.shortestPath, o.shortestPath);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((edges == null) ? 0 : edges.hashCode());
			result = prime * result + (explored ? 1231 : 1237);
			result = prime * result + number;
			result = prime * result + shortestPath;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (edges == null) {
				if (other.edges != null)
					return false;
			} else if (!edges.equals(other.edges))
				return false;
			if (explored != other.explored)
				return false;
			if (number != other.number)
				return false;
			if (shortestPath != other.shortestPath)
				return false;
			return true;
		}
		
	}
	
	private static class Edge {
		Node node;
		int weight;
		
		Edge(Node node, int weight) {
			this.node = node;
			this.weight = weight;
		}

		public Node getNode() {
			return node;
		}

		public int getWeight() {
			return weight;
		}
	}
	
}