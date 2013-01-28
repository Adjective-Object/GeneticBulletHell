package atouhougame.gamescreens;

import java.awt.event.ActionEvent;

import atouhougame.BossSeed;
import atouhougame.LocalEvolutionManager;
import framework.Game;
import framework.SwitchGameEvent;

public class BossRushRouter extends Game{
	
	boolean toBoss=true;
	LocalEvolutionManager manager;
	double lastScore;
	
	public BossRushRouter(LocalEvolutionManager m){
		this.manager=m;
	}
	
	public void scoreNext(double score){
		this.lastScore=score;
	}
	
	@Override
	public void onSwitch(){
		SwitchGameEvent e;
		if(toBoss){
			e = new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED, new TouhouGame(manager) ,endGameDelay);
		}
		else{
			BossSeed last = manager.currentSeed();
			manager.scoreLastSeed(this.lastScore);
			manager.advanceSeed();
			BossSeed next = manager.currentSeed();
			e = new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED, new BetweenScreen(last,next,this.lastScore) ,endGameDelay);
		}
		switchGame(e);
		toBoss=!toBoss;
	}
	
}
