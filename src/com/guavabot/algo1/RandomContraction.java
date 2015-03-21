package com.guavabot.algo1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Finds the Min-Cut of a graph using the RandomContraction algorithm. 
 * The accuracy of the result depends on the number of experiments {@code NUM_EXPERIMENTS}
 */
public class RandomContraction {
	
	private static final int NUM_EXPERIMENTS = 10_000;
	
	private final Random mRandom = new Random();
	
	public static void main(String[] args) throws IOException {
		Map<Integer, List<Integer>> graph = new HashMap<Integer, List<Integer>>();
		File file = new File("data/kargerMinCut.txt");
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("\\s");
		while (scanner.hasNextLine()) {
			int vertex = scanner.nextInt();
			List<Integer> edges = new ArrayList<Integer>();
			while (scanner.hasNextInt()) {
				edges.add(scanner.nextInt());
			}
			graph.put(vertex, edges);
			scanner.nextLine();
		}
		scanner.close();
		System.out.println("graph size: " + graph.keySet().size());
		
		RandomContraction minCut = new RandomContraction();
		int edgeCount = minCut.findMinCut(graph);
		System.out.println("Edges in min cut: " + edgeCount);
		//Edges in min cut: 17
	}
	
	/**
	 * Receives an adjacency list and computes the min cut
	 * @param graph Map of vertices and the edges each is connected to
	 * @return Number of edges in the min cut
	 */
	public int findMinCut(Map<Integer, List<Integer>> graph) {
		int count = Integer.MAX_VALUE;
		for (int i = 0; i < NUM_EXPERIMENTS; i++) {
			int result = runMinCut(deepClone(graph));
			if (result < count) {
				count = result;
			}
		}
		return count;
	}
	
	private int runMinCut(Map<Integer, List<Integer>> graph) {
		while (graph.size() > 2) {
			removeOneVertex(graph);
		}
		
		//Edges in min cut are the edges between the remaining two vertices
		List<Integer> keys = new ArrayList<Integer>(graph.keySet());
		return graph.get(keys.get(0)).size();
	}

	private void removeOneVertex(Map<Integer, List<Integer>> graph) {
		//Select a random vertex
		List<Integer> keys = new ArrayList<Integer>(graph.keySet());
		int keyPos = mRandom.nextInt(keys.size());
		Integer firstVertex = keys.get(keyPos);
		
		//Remove a random neighbor of that vertex
		List<Integer> firstVertexEdges = graph.get(firstVertex);
		int edgePos = mRandom.nextInt(firstVertexEdges.size());
		Integer removedNeighbor = firstVertexEdges.get(edgePos);
		List<Integer> edgesNeighbor = graph.get(removedNeighbor);
		graph.remove(removedNeighbor);
		
		//Remove all edges from firstVertex to removedNeighbor
		while (firstVertexEdges.contains(removedNeighbor)) {
			firstVertexEdges.remove(removedNeighbor);
		}
		
		//Make all edges of removedNeighbor point to the firstVertex
		for (int vertex : edgesNeighbor) {
			if (vertex != firstVertex) {
				List<Integer> vertexEdges = graph.get(vertex);
				vertexEdges.remove(removedNeighbor);
				vertexEdges.add(firstVertex);
				firstVertexEdges.add(vertex);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	static Map<Integer, List<Integer>> deepClone(Map<Integer, List<Integer>> o) {
	    try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(o);
			out.flush();
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray()));
			return (Map<Integer, List<Integer>>) (in.readObject());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
