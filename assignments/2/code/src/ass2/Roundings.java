package ass2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.javafx.collections.MappingChange.Map;

import ilog.concert.IloException;
import ilog.concert.IloNumMap;
import ilog.opl.IloOplModel;

public class Roundings {
	
	public Roundings() {
		// TODO Auto-generated constructor stub
	}
	
	protected static boolean isFeasible(Set<Integer> roundedSets, SetCoverInstance setcover)
	{
		for(int v = 1; v <= setcover.getM(); v++)
		{
			Set<Integer> covering = setcover.getSetsCovering(v);
			
			covering.retainAll(roundedSets);
			
			if(covering.isEmpty())
			{
				return false;
			}
			
		}
		
		return true;
	}
	
	
	protected static int getN(IloOplModel opl) throws IloException
	{
		int n = opl.getElement("n").asInt();
		return n;
	}
	
	protected static int getM(IloOplModel opl) {
		int m = opl.getElement("m").asInt();
		return m;
	}
	
	protected static HashMap<Integer, Double> getXs(IloOplModel opl) throws IloException
	{
		int n = getN(opl);
		
		HashMap<Integer, Double> xs = new HashMap<Integer, Double>();
		IloNumMap xsMap = opl.getElement("x").asNumMap();
		
		for(int c = 1; c <= n; c++)
		{
			xs.put(c, xsMap.get(c));
		}
		
		return xs;
	}
}
