package anetworkcode;

import java.util.HashMap;

import atouhougame.BossSeed;
import atouhougame.Generation;
import atouhougame.LocalEvolutionManager;

public class ClientEvolutionManager extends LocalEvolutionManager{
	
	BossSeed current;
	
	HashMap<Integer, Boolean> existingGenerations = new HashMap<Integer, Boolean> (0);
	
	//makes a new EvolutionManager, w/ seed generation an all, from scratch
	public ClientEvolutionManager(){
		current=Client.requestBoss();
	}
		
	BossSeed currentSeed=new BossSeed(System.currentTimeMillis());
	
	@Override
	public BossSeed currentSeed(){
		return current;
	}
	
	//advances to next boss seed
	@Override
	public void scoreLastSeed(double score){
		Client.submitScore((int)(score), current.bossID);
		
		current.score+=(score-current.score)
		/(1+current.timesTested);

		current.timesTested++;//purely asthetic
	}
	
	@Override
	public void advanceSeed(){
		current=Client.requestBoss();
	}
	
	@Override
	public Generation getGeneration(int generationNumber){
		return Client.getGeneration(generationNumber);
	}
	
	@Override
	public boolean hasGeneration(int n) {
		if (!existingGenerations.containsKey(n)){
			existingGenerations.put(n,Client.checkGenerationExists(n));
		}
		return existingGenerations.get(n);

	}
	
	@Override
	public void refreshCache(){
		existingGenerations.clear();
	}
	
}
