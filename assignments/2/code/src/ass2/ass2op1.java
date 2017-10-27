package ass2;

import ilog.cplex.*;
import ilog.opl.*;
import ilog.concert.*;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class ass2op1 {

	public static void main(String[] args) throws IloException, FileNotFoundException {
		
		String file1 = "scpa3.dat";
		String file2 = "scpc3.dat";
		String file3 = "scpnrf1.dat";
		String file4 = "scpnrg5.dat";
		
		double file1Obj =  objectiveValueOfFile("data/" + file1);
		double file2Obj =  objectiveValueOfFile("data/" + file2);
		double file3Obj =  objectiveValueOfFile("data/" + file3);

		System.out.println("File " + file1 + " has objective value " + file1Obj);
		System.out.println("File " + file2 + " has objective value " + file2Obj);
		System.out.println("File " + file3 + " has objective value " + file3Obj);
	}

	public static double runOnFile(IloOplModel opl, IloOplFactory oplF, IloCplex cplex, String str) throws IloException
	{
		IloOplDataSource dataSource=oplF.createOplDataSource(str);
		opl.addDataSource(dataSource);

		opl.generate();
		//opl.convertAllIntVars(); // converts integer bounds into LP compatible format
		
		
		if (cplex.solve()){
			return cplex.getObjValue();
		 }
		else{
			System.out.println("Solution could not be achieved, probably insufficient memory or some other weird problem.");
			return 0.0;
		}
	}

	public static double objectiveValueOfFile(String filename) throws IloException
	{
		IloOplFactory.setDebugMode(false);
		IloOplFactory oplF = new IloOplFactory();
		IloOplErrorHandler errHandler = oplF.createOplErrorHandler(System.out);
		IloOplModelSource modelSource = oplF.createOplModelSource("ModelSparse1.mod");
		IloCplex cplex = oplF.createCplex();
		IloOplSettings settings = oplF.createOplSettings(errHandler);
		IloOplModelDefinition def=oplF.createOplModelDefinition(modelSource,settings);
		IloOplModel opl=oplF.createOplModel(def,cplex);

		String inDataFile = filename;
		Double objVal = runOnFile(opl, oplF, cplex, inDataFile);
		
		cplex.end();
		opl.end();
		def.end();
		settings.end();
		modelSource.end();
		errHandler.end();
		oplF.end();
		return objVal;
	}
	
}
