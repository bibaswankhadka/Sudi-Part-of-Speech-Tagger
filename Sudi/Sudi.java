import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Class to create emission and transmission maps and run viterbi algorithm.
 * @author bibaswankhadka
 *
 */
public class Sudi {
	String sentencefile;
	String tagfile;
	TreeMap<String,TreeMap<String,Double>> transmissionlog;
	TreeMap<String,TreeMap<String,Double>> emissionlog;
	
	/**
	 * Creates emission map from given file. 
	 */
	public TreeMap<String,TreeMap<String,Integer>> emissionmap(String sentencefile, String tagfile) throws IOException{
		BufferedReader sentences;
		BufferedReader thetags;
		TreeMap<String, TreeMap<String,Integer>> emissionmap = new TreeMap<String, TreeMap<String,Integer>>();
        try {
    		sentences = new BufferedReader(new FileReader(sentencefile));  // Read the files
    		thetags = new BufferedReader(new FileReader(tagfile));
        } 
        catch (FileNotFoundException e) {
            System.err.println("Cannot open file.\n" + e.getMessage());
            return emissionmap;
        }
		try {
			String wordline;
			String tagline;
			int lineNum = 0;
			while ((wordline = sentences.readLine()) != null && (tagline = thetags.readLine()) != null){
				String[] words = wordline.split(" ");
				String[] tags = tagline.split(" ");
				if (words.length != tags.length) {  // If the two lines are not of equal length, there is something wrong with data. 
					System.out.println("incorrect format at "+lineNum);
					sentences.close();
					thetags.close();
					return emissionmap;
				}
				else {
					for (int x = 0;x<words.length; x++) {
						if (emissionmap.containsKey(words[x])) { // Outer Map
							TreeMap<String,Integer> tagfreq = emissionmap.get(words[x]); // Inside Map
							if (tagfreq.containsKey(tags[x])) {  
								emissionmap.get(words[x]).put(tags[x], tagfreq.get(tags[x])+1);  // Increment count
								emissionmap.get(words[x]).put("Normalize", emissionmap.get(words[x]).get("Normalize")+1);  // Normalize is the total
							}
							else {
								tagfreq.put(tags[x], 1);  // If not in map put into map with a frequency of 1.
								emissionmap.get(words[x]).put("Normalize", emissionmap.get(words[x]).get("Normalize")+1);
							}
						}
						if (!emissionmap.containsKey(words[x])) {  // Put into outer map if it does not exist. 
							emissionmap.put(words[x], new TreeMap<String,Integer>());
							emissionmap.get(words[x]).put(tags[x], 1);
							emissionmap.get(words[x]).put("Normalize", 1);
						}
					}
				}
				lineNum++;
			}
		}
		catch (IOException e) {
			System.err.println("IO error while reading.\n" + e.getMessage());
		}
		try {
			sentences.close();
			thetags.close();
		}
		catch (IOException e) {
			System.err.println("Cannot close file.\n" + e.getMessage());
		}
		return emissionmap;
	}
	
	/**
	 * Creates transmissionmap from given input. 
	 */
	public TreeMap<String,TreeMap<String,Integer>> transmissionmap(String tagfile) throws IOException{
		BufferedReader thetags;
		TreeMap<String,TreeMap<String,Integer>> transmissionmap = new TreeMap<String,TreeMap<String,Integer>>();
		transmissionmap.put("Start", new TreeMap<String,Integer>());
		transmissionmap.get("Start").put("Normalize", 0);
        try {
    		thetags = new BufferedReader(new FileReader(tagfile));
        } 
        catch (FileNotFoundException e) {
            System.err.println("Cannot open file.\n" + e.getMessage());
            return transmissionmap;
        }
		try {
			String tagline;
			while ((tagline = thetags.readLine()) != null){
				String[] tags = tagline.split(" ");
				if (transmissionmap.get("Start").containsKey(tags[0])) {
					transmissionmap.get("Start").put(tags[0], transmissionmap.get("Start").get(tags[0])+1);
					transmissionmap.get("Start").put("Normalize", transmissionmap.get("Start").get("Normalize")+1);
				}
				else {
					transmissionmap.get("Start").put(tags[0], 1);
					transmissionmap.get("Start").put("Normalize", transmissionmap.get("Start").get("Normalize")+1);  // Normalize is total.
				}
				for (int x=0;x<tags.length-1;x++) {
					if (transmissionmap.containsKey(tags[x])) {  
						if (transmissionmap.get(tags[x]).containsKey(tags[x+1])) {
							transmissionmap.get(tags[x]).put(tags[x+1], transmissionmap.get(tags[x]).get(tags[x+1])+1);  // Increment counts
							transmissionmap.get(tags[x]).put("Normalize", transmissionmap.get(tags[x]).get("Normalize")+1);
						}
						else {
							transmissionmap.get(tags[x]).put(tags[x+1], 1);
							transmissionmap.get(tags[x]).put("Normalize", transmissionmap.get(tags[x]).get("Normalize")+1);	
						}
						
					}
					else {
						transmissionmap.put(tags[x], new TreeMap<String,Integer>());
						transmissionmap.get(tags[x]).put(tags[x+1], 1);
						transmissionmap.get(tags[x]).put("Normalize", 1);
					}
				}
			}
		}
		catch (IOException e) {
			System.err.println("IO error while reading.\n" + e.getMessage());
		}
		try {
			thetags.close();
		}
		catch (IOException e) {
			System.err.println("Cannot close file.\n" + e.getMessage());
		}
		return transmissionmap;
	}
	
	/**
	 * Converts values in transmission to log probabilities. 
	 */
	public TreeMap<String,TreeMap<String,Double>> transmissionlog(String tagfile) throws IOException{
		TreeMap<String,TreeMap<String,Integer>> transmissionmap = transmissionmap(tagfile);
		transmissionlog = new TreeMap<String,TreeMap<String,Double>>();
		for (String s:transmissionmap.keySet()) {
			TreeMap<String,Integer> nextprob = transmissionmap.get(s);
			transmissionlog.put(s, new TreeMap<String,Double>());
			for (String p:nextprob.keySet()) {
				if (!p.equals("Normalize")) {  // Convert to log probability by taking log(z/total) 
					transmissionlog.get(s).put(p, Math.log((double)nextprob.get(p)/(double)nextprob.get("Normalize")));  // Normalize is the total
				}
			}
		}
		return transmissionlog;
	}
	
	/**
	 * Converts values in emissionmap to log probabilities. 
	 */
	public TreeMap<String,TreeMap<String,Double>> emissionlog(String sentencefile, String tagfile) throws IOException{
		TreeMap<String,TreeMap<String,Integer>> emissionmap = emissionmap(sentencefile, tagfile);
		emissionlog = new TreeMap<String,TreeMap<String,Double>>();
		for (String s:emissionmap.keySet()) {
			TreeMap<String,Integer> probs = emissionmap.get(s);
			emissionlog.put(s, new TreeMap<String,Double>());
			for (String p:probs.keySet()) {
				if (!p.equals("Normalize")) { // Convert to log probability by taking log(z/total)
					emissionlog.get(s).put(p, Math.log((double)probs.get(p)/(double)probs.get("Normalize")));  // Normalize is the total
				}
			}
		}
		return emissionlog;
		
	}
	
	/**
	 * Runs vieterbi algorithm on String
	 */
	public String viterbi(String[] line, TreeMap<String,TreeMap<String,Double>> transmissionlog,TreeMap<String,TreeMap<String,Double>> emissionlog) throws IOException{
		ArrayList<TreeMap<String,String>> backtracklist = new ArrayList<TreeMap<String,String>>();
		Set<String> currStates = new TreeSet<String>();
		String predic = "";
		currStates.add("Start");  
		TreeMap<String, Double> currScores = new TreeMap<String,Double>();
		currScores.put("Start", 0.0);
		for (int x=0;x<line.length;x++) { // Length of line
			Set<String> nextstates = new TreeSet<String>();
			TreeMap<String, Double> nextScores = new TreeMap<String,Double>();
			for (String y:currStates) {  // each currentstate in currentStates
				if (transmissionlog.containsKey(y) && !transmissionlog.get(y).isEmpty()) {
					for (String k:transmissionlog.get(y).keySet()) {
						nextstates.add(k);
						Double nextscore;
						if (emissionlog.containsKey(line[x]) && emissionlog.get(line[x]).containsKey(k)) {  // Check to see that there are no null pointers
							nextscore = currScores.get(y) + transmissionlog.get(y).get(k) + emissionlog.get(line[x]).get(k);
						}
						else {  // If word not found add a not found penalty
							nextscore = currScores.get(y) + transmissionlog.get(y).get(k) + (-100.00);
						}
						if (!nextScores.containsKey(k) || nextscore >nextScores.get(k)) {
							nextScores.put(k, nextscore);
							TreeMap<String,String> backtrack = new TreeMap<String,String>();  // Stores previous and next
							backtrack.put(k, y);
							backtracklist.add(backtrack);  // Add map to a list of maps
						}
						
					}
				}
				
			}
			predic += backtrack(line,currScores, backtracklist );  // Backtrack to find String
			currStates = nextstates;
			currScores = nextScores;
		}
	predic += backtrack(line,currScores, backtracklist);
	return predic;
	}
	
	/**
	 * Back tracks from maximum to find most probable probability. 
	 */
	public String backtrack(String[]line,TreeMap<String,Double> currScores, ArrayList<TreeMap<String,String>> backtracklist ) {
		String predicted ="";
		Double maximum = -10000000000000000000000.00;  // Make maximum ridiculously low so that it is overwritten.
		String maxstring = null;
		for (String s:currScores.keySet()) {  // For every score
			if (currScores.get(s)>maximum) {  // Get the maximum number
				maximum = currScores.get(s);  
				maxstring = s;                // Get max String
			}
		}
		if ( maxstring != "Start") {  // Do not add start to string
			predicted += maxstring + " ";
		}
		
		for (int x =backtracklist.size()-1; x<0; x--) {  // Track back from maximum to start
			if (backtracklist.get(x).containsValue(maxstring)){
				for (String p: backtracklist.get(x).keySet() ) {
					predicted += p + " ";
					maxstring = p;
				}
			}
		}
		return predicted;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
