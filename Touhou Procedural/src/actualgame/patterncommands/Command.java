package actualgame.patterncommands;

import java.io.Serializable;

import actualgame.Boss;

public abstract class Command implements Serializable{

	public boolean finished=false;
	
	public Command(){
	}
	
	public abstract void apply(Boss boss);
}
