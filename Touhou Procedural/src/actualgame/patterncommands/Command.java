package actualgame.patterncommands;

import actualgame.Boss;

public abstract class Command {

	public boolean finished=false;
	
	public Command(){
	}
	
	public abstract void apply(Boss boss);
}
