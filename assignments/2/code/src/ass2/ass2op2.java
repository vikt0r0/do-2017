package ass2;

import java.io.FileNotFoundException;
import java.util.Arrays;

import ilog.concert.IloException;
import ilog.concert.IloNumMap;
import ilog.cplex.IloCplex;
import ilog.opl.IloOplDataSource;
import ilog.opl.IloOplErrorHandler;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;
import ilog.opl.IloOplModelDefinition;
import ilog.opl.IloOplModelSource;
import ilog.opl.IloOplSettings;

public class ass2op2 {
	public static void main(String[] args) throws IloException, FileNotFoundException {
		IloOplFactory.setDebugMode(false);
		IloOplFactory oplF = new IloOplFactory();
		IloOplErrorHandler errHandler = oplF.createOplErrorHandler(System.out);
		IloOplModelSource modelSource = oplF.createOplModelSource("ModelSparse1relax.mod");
		IloCplex cplex = oplF.createCplex();
		IloOplSettings settings = oplF.createOplSettings(errHandler);
		IloOplModelDefinition def=oplF.createOplModelDefinition(modelSource,settings);
		IloOplModel opl=oplF.createOplModel(def,cplex);

		String inDataFile = "data/scpnrg5.dat";
		
		runOnFile(opl, oplF, cplex, inDataFile);

		System.out.println("File = " + inDataFile);
		simpleRoundingRun(opl, inDataFile);
		
		randomizedRound(opl, inDataFile);
		
	}
	
	private static void simpleRoundingRun(IloOplModel opl, String filepath) throws IloException {
		
		int f = 0;
		
		while(true)
		{
			SimpleRounding simpRound = new SimpleRounding(opl, filepath);
			
			f = f + 1;
			simpRound.simpleRounding(f);
			
			System.out.println("The simple rounding with f = " + f + " produced a feasible :" + simpRound.isFeasible + " with a cost of " + simpRound.objectiveValue);
			if(simpRound.isFeasible == true)
			{
				return;
			}
		}
		
	}
	
	private static void randomizedRound(IloOplModel opl, String filepath) throws IloException{
		
		int iter = 10;
		RandomizedRounding randRound;
		
		double tot_obj = 0.0;
		for(int i = 0; i < iter; i++)
		{
			int tries = 0;
			while(true)
			{
				tries++;
				randRound = new RandomizedRounding(opl, filepath);
				randRound.randomizedRounding();
				
				if(randRound.isFeasible)
				{
					break;
				}
			}
			
			tot_obj += randRound.objectiveValue;
			System.out.println("Randomized rounding produced average objective value of " + randRound.objectiveValue + " after " + tries);
		}
		
		tot_obj = tot_obj/10.0;
		System.out.println("Randomized rounding produced average objective value of " + tot_obj);
		
		
	}

	public static void runOnFile(IloOplModel opl, IloOplFactory oplF, IloCplex cplex, String str) throws IloException
	{
		IloOplDataSource dataSource=oplF.createOplDataSource(str);
		opl.addDataSource(dataSource);

		opl.generate();
		//opl.convertAllIntVars(); // converts integer bounds into LP compatible format
		
		if (cplex.solve()){
			System.out.println(cplex.getObjValue());
		 }
		else{
			System.out.println("Solution could not be achieved, probably insufficient memory or some other weird problem.");
		}
	}

	
	

	
}
