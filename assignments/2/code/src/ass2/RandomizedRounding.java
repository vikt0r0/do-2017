package ass2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


import ilog.concert.IloException;
import ilog.opl.IloOplModel;

public class RandomizedRounding extends Roundings{

	IloOplModel opl;
	String filePath;
	SetCoverInstance setCoverInstance;
	public boolean isFeasible;
	public float objectiveValue;
	
	
	
	public RandomizedRounding(IloOplModel opl, String filePath)
	{
		this.opl = opl;
		this.filePath = filePath;
		this.setCoverInstance = SetCoverInstanceParser.parseInstance(filePath);
	}
	
	
	public void randomizedRounding() throws IloException {
		int n = getN(opl);
		HashMap<Integer, Double> xs = getXs(opl);
		
		Set<Integer> setIncluded = new HashSet<Integer>();

		double logN = Math.log(n);
		float objVal = 0.0f;
		
		
		double Const = 0.2;
		for(int iter = 0; iter < Math.ceil(Const*logN); iter++)
		{
			double randomNumber = Math.random();
			
			for(int s = 1; s <= n; s++)
			{
				if(randomNumber <= xs.get(s))
				{
					setIncluded.add(s);
					objVal += setCoverInstance.getCost(s);
				}
			}
		}
		
		this.isFeasible = isFeasible(setIncluded, setCoverInstance);
		objectiveValue = objVal;
	}
}
