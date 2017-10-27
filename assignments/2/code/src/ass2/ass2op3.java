package ass2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class ass2op3 {

	public static void main(String[] args) {
		SetCoverInstance sc = SetCoverInstanceParser.parseInstance("data/scpnrg5.dat");
		
		// Random initialization pass - produce initial cover
		Random rand = new Random();
		Set<Integer> cover = new HashSet<Integer>();
		
		/* Random Strategy - bad
		for (int v = 1; v <= sc.getM(); ++v) {
			// Add random set covering v to cover
			Set<Integer> sets = sc.getSetsCovering(v);
			Integer s = sets.toArray(new Integer[sets.size()])[rand.nextInt(sets.size())];
			cover.add(s);
		}
		*/
		
		for (int s = 1; s <= sc.getN(); ++s) {
			cover.add(s);
		}
		
		// How many sets cover v
		Map<Integer, Integer> coverCount = new HashMap<Integer, Integer>();
		
		for (Integer s : cover) {
			for (Integer v : sc.getVerticesCovered(s)) {
				if (coverCount.get(v) == null)
					coverCount.put(v, 1);
				else
					coverCount.put(v, coverCount.get(v)+1);
			}
		}
		
		// Get list of sets, ordered by descending cost
		List<Integer> sortedCover = cover.stream().sorted((s1,s2) -> sc.getCost(s2) - sc.getCost(s1) ).collect(Collectors.toList());
		
		// Optimal cover
		Set<Integer> optimalCover = new HashSet<Integer>();
		
		// Repeatedly remove most costly cover while maintaining feasibility
		for (Integer s : sortedCover) {
			boolean sRemovable = sc.getVerticesCovered(s).stream().allMatch(v -> coverCount.get(v) > 1);
			
			if (sRemovable) {
				// Decrement cover count for all vertices covered by s
				for (int v : sc.getVerticesCovered(s)) {
					coverCount.put(v, coverCount.get(v)-1);
				}
			} else {
				optimalCover.add(s);
			}
		}
		
		// Print cost
		Integer cost = 0;
		for (Integer s : optimalCover) {
			cost += sc.getCost(s);
		}
		
		System.out.println("Set cover of cost: " + cost);
	}
	
}
