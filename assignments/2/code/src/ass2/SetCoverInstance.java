package src.ass2;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class SetCoverInstance {
	
	// Number of sets
	private int n;
	
	// Number of vertices
	private int m;
	
	// Costs of sets - ArrayList as this maintains ordering
	private ArrayList<Integer> costs;
	
	// Covers - (vertex, sets covering vertex)
	private Map<Integer, Set<Integer>> coveringV;
	
	// Covers - (vertex, sets covering vertex)
	private Map<Integer, Set<Integer>> coveredByS;
	
	public SetCoverInstance(int n, int m, ArrayList<Integer> costs, Map<Integer, Set<Integer>> coveringV, Map<Integer, Set<Integer>> coveredByS) {
		this.n = n;
		this.m = m;
		this.costs = costs;
		this.coveringV = coveringV;
		this.coveredByS = coveredByS;
	}

	// Get number of sets
	public int getN() {
		return n;
	}

	// Get number of vertices
	public int getM() {
		return m;
	}
	
	// Get sets that cover vertex v
	public Set<Integer> getSetsCovering(int v) {
		return this.coveringV.get(v);
	}
	
	// Get vertices covered by s
	public Set<Integer> getVerticesCovered(int s) {
		return this.coveredByS.get(s);
	}
	
	// Get cost of set s
	public Integer getCost(int s) {
		return this.costs.get(s-1);
	}
	
}
