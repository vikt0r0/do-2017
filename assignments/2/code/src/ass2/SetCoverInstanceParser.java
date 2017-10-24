package ass2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SetCoverInstanceParser {
	
	public static SetCoverInstance parseInstance(String filePath) {
		
		// Patterns to match
		Pattern nPattern = Pattern.compile("\\s*n\\s*=\\s*([0-9]+)\\s*;\\s*");      // n = 500;
		Pattern mPattern = Pattern.compile("\\s*m\\s*=\\s*([0-9]+)\\s*;\\s*");      // m = 5000;
		Pattern coversPattern = Pattern.compile("\\s*<([0-9]+)\\s*([0-9]+)>\\s*");  // <1 4>
		Pattern costPattern = Pattern.compile("\\s*c\\s*=\\s*\\[\\s*([[0-9]+\\s*]+)\\]\\s*;");
		
		// Instance variables
		Integer n = null;
		Integer m = null;
		ArrayList<Integer> c = new ArrayList<Integer>();
		Map<Integer, Set<Integer>> coveringV = new HashMap<Integer, Set<Integer>>();
		Map<Integer, Set<Integer>> coveredByS = new HashMap<Integer, Set<Integer>>();
		
	    try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
	        for (String line : (Iterable<String>) stream::iterator) {
				try {
					Matcher cPatternMatcher = coversPattern.matcher(line);
					Matcher nPatternMatcher = nPattern.matcher(line);
					Matcher mPatternMatcher = mPattern.matcher(line);
					Matcher costPatternMatcher = costPattern.matcher(line);
					
					if (cPatternMatcher.find()) {
						// Define cover relation - most common case
						Integer vertex = Integer.parseInt(cPatternMatcher.group(1));
						Integer set = Integer.parseInt(cPatternMatcher.group(2));
						
						// Sets covering v
						if (coveringV.get(vertex) == null)
							coveringV.put(vertex, new HashSet<Integer>(set));
						else
							coveringV.get(vertex).add(set);
						
						// Vertices covered by s
						if (coveredByS.get(set) == null)
							coveredByS.put(set, new HashSet<Integer>(vertex));
						else
							coveredByS.get(set).add(vertex);
					} else if (nPatternMatcher.find()) {
						// Define n
						n = Integer.parseInt(nPatternMatcher.group(1));
					} else if (mPatternMatcher.find()) {
						// Define m
						m = Integer.parseInt(mPatternMatcher.group(1));
					} else if (costPatternMatcher.find()) {
						for (String t : costPatternMatcher.group(1).split("\\s")) {
							c.add(Integer.parseInt(t));
						}
					}
				} catch (IllegalStateException e) {
					;
				}
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    if (n == null || m == null || c.size() != n) {
	    		throw new IllegalStateException("No m or n specified or wrong number of costs in set cover instance .dat file " + filePath + ". Aborting.");
	    }
	    
		return new SetCoverInstance(n, m, c, coveringV, coveredByS);
	}
}
