package framework;

import java.awt.event.ActionEvent;

public class SwitchGameEvent extends ActionEvent{

	public Game targetGame;
	
	public SwitchGameEvent(Object source, int id, Game toSwitch, int delayTime) {
		super(source, id, "SwitchToGame_"+toSwitch.getName());
		this.targetGame = toSwitch;
		// TODO Auto-generated constructor stub
	}
		

}
