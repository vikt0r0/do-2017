package edu.aa12;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class CplexTspSolver
{
	private Graph g = null;
	private final int V;
	private IloCplex cplex;
	private IloIntVar[][] edges;
	private IloNumVar[] t;
	
	
	public CplexTspSolver(Graph g){

		this.g = g;
		V = g.getVertices();
		this.edges = new IloIntVar[V][V];
		this.t = new IloNumVar[V];
		
		try {
			cplex = new IloCplex();
			
			// Objective function is the distance of the edges which are taken.
			IloLinearNumExpr objective = cplex.linearNumExpr();

			for(int i = 0; i < V; ++i) {
				for(int j = 0; j < V; ++j) {
					// Initialize edge variable
					this.edges[i][j] = cplex.boolVar();
					
					// Add to objective function
					double distance = g.getDistance(i,j);
					objective.addTerm(distance, this.edges[i][j]);
				}
			}

			cplex.addMinimize(objective);
			
			// One outgoing pr. vertex
			for(int i = 0; i < V; ++i) {
				IloLinearIntExpr sum = cplex.linearIntExpr();
				for(int j = 0; j < V; ++j) {
					if (i == j)
						continue;
					
					// Initialize edge variable
					sum.addTerm(edges[i][j], 1);
				}
				cplex.addEq(sum, 1);
			}
			
			// One ingoing pr. vertex
			for(int j = 0; j < V; ++j) {
				IloLinearIntExpr sum = cplex.linearIntExpr();
				for(int i = 0; i < V; ++i) {
					if (i == j)
						continue;
					
					// Initialize edge variable
					sum.addTerm(edges[i][j], 1);
				}
				cplex.addEq(sum, 1);
			}
			
			for (int i = 0; i < V; ++i) {
				t[i] = cplex.numVar(0, Double.POSITIVE_INFINITY);
			}

			for(int i = 0; i < V; ++i) {
				for(int j = 1; j < V; ++j) {
					IloNumExpr rhs = cplex.sum(t[i], cplex.sum(1, cplex.negative(cplex.prod(V, cplex.sum(1, cplex.negative(edges[i][j])))))); // HACK
					cplex.addGe(t[j], rhs);
				}
			}
			
			
		} catch (IloException e) {
			e.printStackTrace();
		}
	}
	
	
	public void solveModelTSP() {		
		try {
			// solve model and print solution 
			if(cplex.solve()) {
				for(int i = 0; i < V; ++i){
					for(int j = 0; j < V; ++j){
						if (cplex.getValue(edges[i][j]) == 1.0) {
							System.out.println("Edge("+ i + "->" + j +") in solution.");
						}
					}
				}
				System.out.println("obj = "+ cplex.getObjValue());
			}
			else {
				System.out.println("Model not solved");
			}
			
			cplex.end(); 	

		}
		catch (IloException exc) {
			exc.printStackTrace();
		}
	}
}
