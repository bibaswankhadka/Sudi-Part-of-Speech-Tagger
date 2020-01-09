import java.io.IOException;

/**
 * Scanner that allows you to type, then receive parts of speech. 
 * @author bibaswankhadka
 *
 */
public class Scanner {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		Testsudi tester = new Testsudi();
		tester.scanner( "inputs/brown-train-sentences.txt", "inputs/brown-train-tags.txt");

	}

}
