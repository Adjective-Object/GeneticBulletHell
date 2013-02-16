package anetworkcode;

import java.util.HashMap;

import atouhougame.BossSeed;
import atouhougame.Generation;
import atouhougame.LocalEvolutionManager;

public class ClientEvolutionManager extends LocalEvolutionManager{
	
	BossSeed current;
	BossSeed next;
	
	HashMap<Integer, Boolean> existingGenerations = new HashMap<Integer, Boolean> (0);
	
	//makes a new EvolutionManager, w/ seed generation an all, from scratch
	public ClientEvolutionManager(){
		current=Client.requestBoss();
		next=Client.requestBoss();
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
	}
	
	@Override
	public void advanceSeed(){
		current=next;
		next=Client.requestBoss();
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
	
	public void refreshCache(){
		existingGenerations.clear();
	}
	
}
