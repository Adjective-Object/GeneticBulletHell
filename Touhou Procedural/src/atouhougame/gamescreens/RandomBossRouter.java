package atouhougame.gamescreens;

import java.awt.event.ActionEvent;

import atouhougame.TGlobal;
import framework.Game;
import framework.SwitchGameEvent;


public class RandomBossRouter extends Game{

	@Override
	public void onSwitch(){
		SwitchGameEvent e = new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED,new TouhouGame(TGlobal.evolutionManager.getTestingSeed(),true),endGameDelay);
		TGlobal.evolutionManager.advanceSeed();
		switchGame(e);
	}
	
}
