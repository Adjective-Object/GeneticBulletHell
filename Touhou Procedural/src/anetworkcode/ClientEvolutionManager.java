package anetworkcode;

import atouhougame.BossSeed;
import atouhougame.LocalEvolutionManager;

public class ClientEvolutionManager extends LocalEvolutionManager{
	
	BossSeed current;
	BossSeed next;
	
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
	
}
