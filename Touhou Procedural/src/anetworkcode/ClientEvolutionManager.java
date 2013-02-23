package anetworkcode;

import java.util.HashMap;

import atouhougame.BossSeed;
import atouhougame.EvolutionManager;
import atouhougame.Generation;

public class ClientEvolutionManager extends EvolutionManager{
	
	BossSeed current, previous;
	
	HashMap<Integer, Boolean> existingGenerations = new HashMap<Integer, Boolean> (0);
	
	//makes a new EvolutionManager, w/ seed generation an all, from scratch
	public ClientEvolutionManager(){
		current=Client.requestBoss();
	}
		
	BossSeed currentSeed=new BossSeed(System.currentTimeMillis());
	
	@Override
	public BossSeed getTestingSeed() {
		return current;
	}
	
	//advances to next boss seed
	@Override
	public boolean scoreSeed(long bossID, double score) {
		Client.submitScore((int)(score), bossID);
		
		if(bossID==current.bossID){//purely asthetic
			current.score+=(score-current.score)
			/(1+current.timesTested);
	
			current.timesTested++;
		}
		
		return true;
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


	@Override
	public int getNumGenerations() {
		return 0;//TODO what will this do again?
	}
}
