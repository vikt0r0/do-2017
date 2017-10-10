package edu.aa12;

public class MainMethods {

	public static void main(String[] args){
		System.out.println("ILP with CPLEX for instance 1");
		long start = System.nanoTime();
		new CplexTspSolver(new Instance2()).solveModelTSP();
		long end = System.nanoTime();
		System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
		
		System.out.println("ILP with CPLEX for instance 2");
		start = System.nanoTime();
		new CplexTspSolver(new Instance2()).solveModelTSP();
		end = System.nanoTime();
		System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
		
		System.out.println("ILP with CPLEX for instance 3");
		start = System.nanoTime();
		new CplexTspSolver(new Instance3()).solveModelTSP();
		end = System.nanoTime();
		System.out.printf("Took %.2fms\n",(end-start)/1000000.0);
		
		//System.out.println(TwoApproximationTSP.computeBound(new Instance1(), root));
		//solveGraph(new Instance1());
		//solveGraph(new Instance2());
		//solveGraph(new Instance3());
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
}
