package atouhougame.patterncommands;

import java.io.Serializable;

import atouhougame.Boss;
import atouhougame.Player;

public abstract class Command implements Serializable{

	public boolean finished=false;
	
	public Command(){
	}
	
	public abstract void apply(Boss boss, Player p);
}
