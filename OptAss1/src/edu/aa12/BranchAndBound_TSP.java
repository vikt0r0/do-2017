package edu.aa12;

import java.awt.print.Printable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import edu.aa12.DisjointSet.DSNode;

/**
 * An implementation of branch-and-bound for TSP with the lower-bound method missing.
 */
public class BranchAndBound_TSP {
	/** Finds the minimum spanning tree using Kruskals algorithm */
	private final Kruskal kruskal = new Kruskal();
	private final Graph graph;
	/** The number of BnBNodes generated */
	private long nodesGenerated = 0;
	
	/** Construct a problem instance */
	public BranchAndBound_TSP(Graph g){
		graph = g;
	}
	
	/** Find the shortest tour visiting all nodes exactly once and return the result as a BnBNode. */
	public BnBNode solve(){
		//Sorting edges by length might or might not help
//		Collections.sort(graph.edges, new Comparator<Edge>(){
//			public int compare(Edge arg0, Edge arg1) {
//				return -Double.compare(graph.getLength(arg0),graph.getLength(arg1));
//			}});
		
		//The ordering in the nodePool determines which nodes gets polled first. 
		PriorityQueue<BnBNode> nodePool = new PriorityQueue<BnBNode>(10000,	new Comparator<BnBNode>(){
			public int compare(BnBNode n0, BnBNode n1) {
				return Double.compare(n0.lowerBound, n1.lowerBound);//Best-first
				// return (int) Math.signum(Math.random() - 0.5);
				// return (n0.depth-n1.depth);//Breadth-first
				// return (n1.depth-n0.depth);//Depth-first
			}});
		
		BnBNode root = new BnBNode(null,null, false);
		root.lowerBound = Double.POSITIVE_INFINITY;
		double upperBoundIncumbent = upperBound(root);
		nodePool.add(root);
		
		BnBNode best = root;
		
		while(!nodePool.isEmpty()){
			BnBNode node = nodePool.poll();
			
			// If the number of fixed edges is equal to the number of vertices
			// in G, we have a Hamiltonian cycle and we stop recursing.
			if(node.edgesDefined==(graph.getVertices())){
				if(node.lowerBound<best.lowerBound) {
					best = node;
					// System.out.println("Updated best, yay! Best: " + best.lowerBound);
				}
			}else{
				// System.out.println(node.lowerBound);
				if(node.lowerBound > upperBoundIncumbent && node.parent != null) {
					// System.out.println("Prune by bound at feasible solution = " + best.lowerBound + ", " + node.lowerBound + " > " + upperBoundIncumbent);
				} else {
					if(node.lowerBound<=best.lowerBound){
						branch(node,nodePool);
					} else {
						// Prune
					}
				}
			}
		}
		
		System.out.printf("Finished branch-and-bound. Path-length: %.3f, %d nodes generated\n",best.lowerBound, nodesGenerated);
		return best;
	}
	
	/** 
	 * Branch on a node, generating either 0, 1 or 2 new children of the node. A child is only generated 
	 * if it does not result in a sub-tour, a vertex with degree more than 2, or a vertex where the degree 
	 * can never become 2.    
	 */ 
	private void branch(BnBNode node, PriorityQueue<BnBNode> nodePool){
		if(graph.edges.indexOf(node.edge)==graph.edges.size()-1) return;
		
		//Choose next edge to branch on. Uses the ordering in graph.edges.
		Edge nextEdge = graph.edges.get(graph.edges.indexOf(node.edge)+1);
		
		//Represent graph components as sets (used to detect subtours) 
		DisjointSet ds = new DisjointSet();
		DSNode[] vertexSets = new DSNode[graph.getVertices()];
		for(int i=0;i<graph.getVertices();i++)
			vertexSets[i] = ds.makeSet(i);
		BnBNode n = node;
		while(n.parent!=null){
			if(n.edgeIncluded) {
				ds.union(vertexSets[n.edge.u], vertexSets[n.edge.v]);
			}
			n=n.parent;
		}


		//Check out-degree<=2 and in-degree>=2
		//Find maximum adjacent edges (could be optimized away using an array)
		int uAdjMax = graph.incidentEdges[nextEdge.u].size();
		int vAdjMax = graph.incidentEdges[nextEdge.v].size();
		int uAdj = 0, vAdj = 0;
		//Find length of defined edges
		n = node;
		// Iterate up the BnB tree and see which edges were included/excluded
		// respectively. The total number of incident edges minus the total number
		// of excluded edges plus the total number of included edges for u, v,
		// respectively.
		while(n.parent!=null){
			if(n.edgeIncluded){
				if(n.edge.u==nextEdge.u||n.edge.v==nextEdge.u) uAdj++;
				if(n.edge.u==nextEdge.v||n.edge.v==nextEdge.v) vAdj++;
			}else{
				if(n.edge.u==nextEdge.u||n.edge.v==nextEdge.u) uAdjMax--;
				if(n.edge.u==nextEdge.v||n.edge.v==nextEdge.v) vAdjMax--;
			}
			n=n.parent;
		}
		
		//Exclude nextEdge (assuming constraints can be met)
		if(uAdjMax>2 && vAdjMax>2){
			n = new BnBNode(node, nextEdge, false);
			n.upperBound = upperBound(n);
			n.lowerBound = lowerBound(n);
			nodePool.add(n);
			nodesGenerated++;
		}
		
		//Include nextEdge (assuming constraints can be met)
		if( (node.edgesDefined==graph.getVertices()-1||ds.find(vertexSets[nextEdge.u])!=ds.find(vertexSets[nextEdge.v])) && uAdj<2 && vAdj<2){
			n = new BnBNode(node,nextEdge,true);
			n.lowerBound = lowerBound(n);
			n.upperBound = upperBound(n);
			nodePool.add(n);
			nodesGenerated++;
		}
		
	}
	
	/** Return a lower-bound for the node */
	public double lowerBound(BnBNode node){
		if(node.edgesDefined==graph.getVertices()) {
			return objectiveValue(node);
		}

		// Compute the MST
		List<Edge> MST = kruskal.minimumSpanningTree(graph, node);
		MST.addAll(node.getIncludedEdges());
		
		// The edges we can not add to the lower bound
		Set<Edge> illegalEdges = new HashSet<Edge>();
		illegalEdges.addAll(MST);
		illegalEdges.addAll(node.getExcludedEdges());
		
		@SuppressWarnings("unchecked")
		Set<Edge> legalEdges = (HashSet<Edge>) graph.edgeSet.clone(); 
		legalEdges.removeAll(illegalEdges);
		
		// Get the min cost edge
		Edge min = Collections.min(legalEdges, new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				return Double.compare(graph.distances[e1.u][e1.v], graph.distances[e2.u][e2.v]);
			}
		});
		
		MST.add(min);
		
		double dist = 0.0;
		
		// Compute value of tree
		for (Edge e : MST) {
			dist += graph.getLength(e);
		}
		
		return dist;
	}
	
	public double upperBound(BnBNode node){
		/*
		if(node.edgesDefined==graph.getVertices()) {
			return objectiveValue(node);
		}

		List<Edge> MaxST = kruskal.maximumSpanningTree(graph, node);
		MaxST.addAll(node.getIncludedEdges());
		
		// The edges we can not add to the lower bound
		Set<Edge> illegalEdges = new HashSet<Edge>();
		illegalEdges.addAll(MaxST);
		illegalEdges.addAll(node.getExcludedEdges());
		
		@SuppressWarnings("unchecked")
		Set<Edge> legalEdges = (HashSet<Edge>) graph.edgeSet.clone(); 
		legalEdges.removeAll(illegalEdges);
		
		Edge max = Collections.max(legalEdges, new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				return Double.compare(graph.distances[e1.u][e1.v], graph.distances[e2.u][e2.v]);
			}
		});
		
		MaxST.add(max);
		
		double dist = 0.0;
		for(Edge e : MaxST)
		{
			dist += graph.getLength(e);
		}
		
		return dist;
		*/
		return TwoApproximationTSP.computeBound(graph, node);
	}
	
	/** Assuming that n represents a valid hamiltonian tour return the length of the tour */
	public double objectiveValue(BnBNode n){
		//Find length of defined edges
		double pathLength = 0;
		while(n.parent!=null){
			if(n.edgeIncluded)
				pathLength += graph.getDistance(n.edge.u, n.edge.v);
			n=n.parent;
		}
		return pathLength;
	}
	
}
