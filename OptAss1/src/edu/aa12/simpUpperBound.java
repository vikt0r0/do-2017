package edu.aa12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import edu.aa12.DisjointSet.DSNode;

/** Class for finding the minimum spanning tree using Kruskals algorithm */
public class simpUpperBound {

	public static double upperBound(Graph g, BnBNode node){
		
		double dist =0.0;
		int[] allowedEdges = new int[g.getVertices()];
		
		for(int i = 0; i < g.getVertices(); i++)
		{
			allowedEdges[i] = 2;
		}
		
		List<Edge> mstEdges = new LinkedList<Edge>(g.edges);
		List<Edge> usedEdges = new ArrayList<Edge>();

		while(node.parent!=null){
			if(node.edgeIncluded){
				usedEdges.add(node.edge);
				usedEdges.add(node.edge);
				allowedEdges[node.edge.u]--;
				allowedEdges[node.edge.v]--; // HACK
			}
			else
			{
				mstEdges.remove(node.edge);						//Disregard excluded edges
			}
			node=node.parent;
		}
		
		List<Edge> tmp = new ArrayList<Edge>(mstEdges);
		Collections.sort(tmp, new Comparator<Edge>(){	//Sort edges in nondescending order
			public int compare(Edge o1, Edge o2) {
				return Double.compare(g.getDistance(o2.u, o2.v), g.getDistance(o1.u, o1.v));
			}});
		
		for(int v_i = 0; v_i<g.getVertices(); v_i++)
		{
			while(allowedEdges[v_i] > 0)
			{
				for(Edge e : tmp)
				{
					if(e.u == v_i || e.v == v_i)
					{
						allowedEdges[v_i]--;
						usedEdges.add(e);
					}
					if(allowedEdges[v_i] == 0)
					{
						break;
					}
				}
			}
		}
		
		for(Edge e : usedEdges)
		{
			dist += g.getLength(e);
		}
		
		dist = dist / 2;
		
		return dist;
	}
	
}
