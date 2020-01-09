import java.io.IOException;
/**
 * Training test to see if methods to get emission and transmissionmaps are working. 
 * @author bibaswankhadka
 *
 */
public class Training {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Sudi trainer = new Sudi();
//		System.out.println(trainer.emissionmap("inputs/simple-train-sentences.txt", "inputs/simple-train-tags.txt"));
//		System.out.println(trainer.transmissionmap("inputs/simple-train-tags.txt"));
		System.out.println(trainer.emissionlog("inputs/simple-train-sentences.txt", "inputs/simple-train-tags.txt"));
		System.out.println(trainer.transmissionlog("inputs/simple-train-tags.txt"));
		
	}

}
