package src.ass2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ilog.concert.IloException;
import ilog.opl.IloOplModel;

public class SimpleRounding extends Roundings{
	IloOplModel opl;
	String filePath;
	SetCoverInstance setCoverInstance;
	public boolean isFeasible;
	public float objectiveValue;
	
	public SimpleRounding(IloOplModel opl, String filePath)
	{
		this.opl = opl;
		this.filePath = filePath;
		this.setCoverInstance = SetCoverInstanceParser.parseInstance(filePath);
	}
	
	
	public void simpleRounding(int f) throws IloException {
		int n = getN(opl);
		HashMap<Integer, Double> xs = getXs(opl);
		
		Set<Integer> setIncluded = new HashSet<Integer>();
		float objVal = 0.0f;
		
		int logN = (int) Math.log(n);
		
		double confidence = 1.0 / f;
		
		for(int s = 1; s <= n; s++)
		{
			if(xs.get(s) >= confidence)
			{
				setIncluded.add(s);
				objVal += setCoverInstance.getCost(s);
			}
		}
		
		this.isFeasible = isFeasible(setIncluded, setCoverInstance);
		objectiveValue = (float) objVal;
	}
}
