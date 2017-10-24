package ass2;

import ilog.cplex.*;
import ilog.opl.*;
import ilog.concert.*;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class ass2op1 {

	public static void main(String[] args) throws IloException, FileNotFoundException {
		IloOplFactory.setDebugMode(false);
		IloOplFactory oplF = new IloOplFactory();
		IloOplErrorHandler errHandler = oplF.createOplErrorHandler(System.out);
		IloOplModelSource modelSource = oplF.createOplModelSource("ModelSparse1relax.mod");
		IloCplex cplex = oplF.createCplex();
		IloOplSettings settings = oplF.createOplSettings(errHandler);
		IloOplModelDefinition def=oplF.createOplModelDefinition(modelSource,settings);
		IloOplModel opl=oplF.createOplModel(def,cplex);

		String inDataFile = "data/scpa3.dat";
		runOnFile(opl, oplF, cplex, inDataFile);
		
		
		IloNumMap x = opl.getElement("x").asNumMap();
		System.out.println(x.get(0));
	}

	public static void runOnFile(IloOplModel opl, IloOplFactory oplF, IloCplex cplex, String str) throws IloException
	{
		IloOplDataSource dataSource=oplF.createOplDataSource(str);
		opl.addDataSource(dataSource);

		opl.generate();
		opl.convertAllIntVars(); // converts integer bounds into LP compatible format
		
		
		if (cplex.solve()){
			System.out.println(cplex.getObjValue());
		 }
		else{
			System.out.println("Solution could not be achieved, probably insufficient memory or some other weird problem.");
		}
	}
	
	
}
