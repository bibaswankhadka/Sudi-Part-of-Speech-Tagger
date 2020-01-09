import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

/*
 * Methods to test testsudi
 * @author Bibaswan Khadka
 */
public class Testsudi {
	
	/**
	 * Method to train, read a unknown dataset and write to a file. 
	 */
	public void testviturbi(String inputfile, String outputfile, String trainingsentences, String trainingtags) throws IOException{
		BufferedReader input = new BufferedReader(new FileReader(inputfile));  // Reads input
		BufferedWriter output = new BufferedWriter(new FileWriter(outputfile));  // Writes output
		Sudi trainer = new Sudi();
		TreeMap<String,TreeMap<String,Double>> emission = trainer.emissionlog(trainingsentences, trainingtags);
		TreeMap<String,TreeMap<String,Double>> transmission = trainer.transmissionlog(trainingtags);
		String line;
		while ((line = input.readLine()) != null){
			String[] splitline = line.split(" "); 
			System.out.println(trainer.viterbi(splitline, transmission, emission));
			output.write(trainer.viterbi(splitline, transmission, emission));  // Calls viturbi algorithm and writes predictions
			output.newLine();  // Newline in output
		}
		input.close();
		output.close();
	}
	
	/**
	 * Given 2 files, compares and return differences and correct scores. 
	 */
	public void similaritychecker(String file, String otherfile) throws IOException{
		BufferedReader input1 = new BufferedReader(new FileReader(file));  // Get first file
		BufferedReader input2 = new BufferedReader(new FileReader(otherfile));  // Get second file
		String line1;
		String line2;
		Integer wrong = 0;
		Integer right = 0;
		while (((line1 = input1.readLine()) != null) && ((line2 = input2.readLine()) != null)) {
			String[] splitline1 = line1.split(" ");
			String[] splitline2 = line2.split(" ");
			for (int x=0;x<splitline1.length;x++) {
				if (splitline1[x].equals(splitline2[x])) {  // If they are euual add to right, if wrong add to wrong.
					right +=1;
				}
				else {
					wrong +=1;
				}
			}
		}
		input1.close();
		input2.close();
		System.out.println("wrong = " + wrong);
		System.out.println("right = " + right);
	}
	
	/**
	 * Method used to input words from keyboard and calculate parts of speech. 
	 */
	public void scanner(String trainingsentences, String trainingtags) throws IOException{
		Scanner in = new Scanner(System.in);  // Get input
		while (true) {
			System.out.print(">");
			Sudi trainer = new Sudi();
			TreeMap<String,TreeMap<String,Double>> emission = trainer.emissionlog(trainingsentences, trainingtags);
			TreeMap<String,TreeMap<String,Double>> transmission = trainer.transmissionlog(trainingtags);
			String line = in.nextLine();
			String[] splitline = line.split(" ");  // Split input by space and input into viterbi. 
			System.out.println(trainer.viterbi(splitline, transmission, emission));
		}
	}
	
	// Tests simple tests
	public static void main(String[] args) throws IOException{
		Testsudi tester = new Testsudi();
		tester.testviturbi("inputs/simple-test-sentences.txt", "Testing/output.txt", "inputs/simple-train-sentences.txt", "inputs/simple-train-tags.txt");
		tester.similaritychecker("inputs/simple-test-tags.txt", "Testing/output.txt");

		// TODO Auto-generated method stub

	}

}
