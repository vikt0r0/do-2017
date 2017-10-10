package edu.aa12;

public class MainMethods {

	public static void main(String[] args){
		// Solve instances using BnB
		solveGraph(new Instance1());
		solveGraph(new Instance2());
		solveGraph(new Instance3());
		
		// Solve instances using CPLEX, MTZ formulation.
		System.out.println("ILP with CPLEX for instance 1");
		solveCPlex(new Instance1());
		System.out.println("ILP with CPLEX for instance 2");
		solveCPlex(new Instance2());
		System.out.println("ILP with CPLEX for instance 3");
		solveCPlex(new Instance3());
	}
	
	public static void solveGraph(Graph g){
		BranchAndBound_TSP solver = new BranchAndBound_TSP(g);
		long start = System.nanoTime();
		BnBNode n = solver.solve();
		long end = System.nanoTime();
		System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
//		System.out.println(n);
//		Visualization.visualizeSolution(g, n);//Requires ProGAL (www.diku.dk/~rfonseca/ProGAL)
	}
	
	public static void solveCPlex(Graph g){
		long start = System.nanoTime();
		new CplexTspSolver(g).solveModelTSP();
		long end = System.nanoTime();
		System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
	}
}
