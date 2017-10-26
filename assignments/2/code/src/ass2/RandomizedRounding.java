package ass2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.javafx.geom.transform.GeneralTransform3D;

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

		int logN = (int) Math.log(n);
		
		for(int i = 1; i <= n; i++)
		{
			if(xs.get(i) != 0)
			{
				setIncluded.add(i);
			}
		}
		/*
		int Const = 1;
		for(int iter = 0; iter < Const*logN; iter++)
		{
			double randomNumber = Math.random();
			
			for(int c = 1; c <= n; c++)
			{
				if(randomNumber <= xs.get(c))
				{
					setIncluded.add(c);
				}
			}
		}
		*/
		
		this.isFeasible = isFeasible(setIncluded, setCoverInstance);
		System.out.println(isFeasible);
	}
}
