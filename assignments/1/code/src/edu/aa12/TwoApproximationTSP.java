package edu.aa12;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class TwoApproximationTSP {

	public static Double computeBound(final Graph G, BnBNode node) {

		// Compute MST
		List<Edge> MST = (new Kruskal()).minimumSpanningTree(G, node);
		MST.addAll(node.getIncludedEdges());

		// Create MST graph
		Graph H = new Graph(G.vertexCoords);
		for (Edge e : MST) {
			H.createEdge(e.u, e.v);
		}
		
		// Compute Euler tour
		List<Edge> circuit= EulerCircuit.computeEulerCircuit(H);
		
		// Short circuit
		List<Edge> shorted = new LinkedList<Edge>();
		
		Set<Integer> visited = new HashSet<Integer>();
		
		// Visit the first node
		visited.add(circuit.get(0).u);
		
		for (int i = 0; i < circuit.size(); ++i) {
			Edge cur = circuit.get(i);
			Edge next = circuit.get((i + 1) % circuit.size());

			if (visited.contains(cur.v)) {
				// Skip next
				i++;
				// Shortcut as (cur.u, next.v) \leq (cur.u, cur.v) +  (next.u, next.v)
				shorted.add(new Edge(cur.u, next.v));
				// Visit v
				visited.add(next.v);
			} else {
				// Unvisited, so no need to shortcircuit
				// Traverse the edge
				shorted.add(cur);
				// Visit v
				visited.add(cur.v);
			}
		}
		
		H = new Graph(G.vertexCoords);
		
		Double sum = 0.0;
		
		for (Edge e : shorted) {
			H.createEdge(e.u, e.v);
			sum += H.getLength(e);
		}
		
		return sum;
	}
}
