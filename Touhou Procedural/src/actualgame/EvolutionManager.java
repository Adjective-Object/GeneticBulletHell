package actualgame;

import java.util.ArrayList;

public class EvolutionManager {

	ArrayList<BossSeed> currentGeneration;
	
	public BossSeed nextSeed(){
		return new BossSeed(System.currentTimeMillis());
	}
	
	public void scoreLastSeed(int score){
		
	}
	
}
