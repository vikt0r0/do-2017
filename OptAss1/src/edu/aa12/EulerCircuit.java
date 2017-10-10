package edu.aa12;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EulerCircuit {

	public static List<Edge> computeEulerCircuit(Graph g) {

		// Maintain degrees of vertices
		int[] degree = new int[g.getVertices()];
		
		for (Edge e : g.edges) {
			degree[e.u]++;
			degree[e.v]++;
		}
		
		// Add reverse edges
		@SuppressWarnings("unchecked")
		List<Edge> unused = new LinkedList<Edge>(g.edges);
		List<Edge> reversed = unused.stream().map(e -> (new Edge(e.v,e.u))).collect(Collectors.toList());
		unused.addAll(reversed);
		
		// Sort the edges lexicographically
		unused.sort(new Comparator<Edge>() {
			@Override
			public int compare(Edge e1, Edge e2) {
				if (e1.u < e2.u)
					return -1;
				if (e1.u == e2.u)
					return g.getLength(e1) < g.getLength(e2) ? 1 : -1;
				return 1;
			}
		});
		
		// Compute adjacency graph
		Map<Integer, List<Integer>> adjacent = new HashMap<Integer, List<Integer>>();
		
		for (int i = 0; i < g.getVertices(); ++i)
			adjacent.put(i, new LinkedList<Integer>());
			
		for (Edge e : unused)
			adjacent.get(e.u).add(e.v);
		
		// Determine successors
		Map<Edge, Edge> succ = new HashMap<Edge, Edge>();

		for (int j = 0; j < g.getVertices(); ++j) {
			List<Integer> adj = adjacent.get(j);
			Integer d = adj.size();
			for(int i = 0; i < d; ++i) {
				succ.put(new Edge(adj.get(i), j), new Edge(j, adj.get((i+1) % d)));
			}
		}
		
		// The tour.
		List<Edge> tour = new LinkedList<Edge>();
		Edge current = succ.keySet().stream().collect(Collectors.toList()).get(0);

		for (int i = 0; i<succ.size(); ++i)  {
			System.out.println(current);
			for (Edge e : succ.keySet()) {
				if (current.u == e.u && current.v == e.v) {
					current = succ.get(e);
					tour.add(current);
					break;
				}
			}
		}
		return tour;
	}
}
