package ass2;

import java.io.FileNotFoundException;

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
		IloOplModelSource modelSource = oplF.createOplModelSource("ModelSparse1.mod");
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

	public static void objectiveValueOfFile(String filename)
	{
		
		
	}
	
}
