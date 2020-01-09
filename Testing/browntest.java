import java.io.IOException;
/**
 * Class to test brown data set.
 * Trains, then applies to a new dataset. 
 * Also checks output to determine how many are wrong and correct. 
 * @author bibaswankhadka
 *
 */
public class browntest {

	public static void main(String[] args) throws IOException{
		Testsudi tester = new Testsudi();
		tester.testviturbi("inputs/brown-test-sentences.txt", "Testing/outputbrown.txt", "inputs/brown-train-sentences.txt", "inputs/brown-train-tags.txt");
		tester.similaritychecker("inputs/brown-test-tags.txt", "Testing/outputbrown.txt");
		// TODO Auto-generated method stub

	}

}
