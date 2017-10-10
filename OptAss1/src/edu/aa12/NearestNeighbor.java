package edu.aa12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import com.sun.jmx.remote.util.EnvHelp;

import edu.aa12.DisjointSet.DSNode;

/** Class for finding the minimum spanning tree using Kruskals algorithm */
public class NearestNeighbor {
	private final DSNode[] nodes = new DSNode[200];//Hack
	private final DisjointSet ds = new DisjointSet();
	
	
	/** 
	 * Find the minimum spanning tree in a graph where included edges in <code>node</code> are 
	 * contracted and excluded edges in <code>node</code> are disregarded. 
	 */
	public List<Edge> nearestNeighbor(final Graph graph, BnBNode node){
		for(Edge e: graph.edges){
			nodes[e.u] = ds.makeSet(e.u);
			nodes[e.v] = ds.makeSet(e.v); 
		}
		
		List<Edge> nnEdges = new LinkedList<Edge>(graph.edges);
		BnBNode n = node;
		while(n.parent!=null){
			if(n.edgeIncluded) 	ds.union(nodes[n.edge.u], nodes[n.edge.v]);		//Contract included edges
			else				nnEdges.remove(n.edge);						//Disregard excluded edges
			n=n.parent;
		}
		
		List<Edge> tmp = new ArrayList<Edge>(nnEdges);
		Collections.sort(tmp, new Comparator<Edge>(){	//Sort edges in nondescending order
			public int compare(Edge o1, Edge o2) {
				return Double.compare(o1.u*1000 + graph.getLength(o1), o2.u*1000 + graph.getLength(o2));
			}});
		
		
		
		return nnEdges;
	}
	
	
//	public List<Edge> minimumSpanningTree(final Graph g, List<Edge> edges){
//		
//		List<Edge> ret = new ArrayList<Edge>(edges);
//		Collections.sort(edges, new Comparator<Edge>(){
//			public int compare(Edge o1, Edge o2) {
//				return Double.compare(g.getDistance(o1.u, o1.v), g.getDistance(o2.u, o2.v));
//			}});
//		
//		for(Edge e: edges){
//			nodes[e.u] = ds.makeSet(e.u);
//			nodes[e.v] = ds.makeSet(e.v); 
//		}
//		for(Edge e: edges){
//			if(ds.find(nodes[e.u])!=ds.find(nodes[e.v])){
//				ds.union(nodes[e.u], nodes[e.v]);
//			}else{
//				ret.remove(e);
//			}
//		}
//		
//		return ret;
//	}
	
}
