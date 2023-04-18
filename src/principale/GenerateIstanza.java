package principale;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GenerateIstanza {

	public static void main(String[] args) {
		
		List<Integer> daStampare = new ArrayList<Integer>();
		daStampare = generaNumeriRandom(0,20,50,25);
		for(int i: daStampare) {
			System.out.println(i);
		}

	}
	
	public static List<Integer> generaNumeriRandom(long seed, int start, int stop, int l){
		
		Random generator = new Random(seed);
		List<Integer> daRestituire = new ArrayList<Integer>();
		
		for(int i=0; i<l; i++) {
			int j = generator.nextInt(start, stop);
			daRestituire.add(j);
		}
		
		return daRestituire;
		
		
	}

}
