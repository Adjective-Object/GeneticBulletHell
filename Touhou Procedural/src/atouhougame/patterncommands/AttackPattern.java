package atouhougame.patterncommands;
import java.io.Serializable;
import java.util.ArrayList;

import atouhougame.Boss;


public class AttackPattern implements Serializable{
	public ArrayList<Command> commands;
	protected int currentCommand;
	
	public AttackPattern(ArrayList<Command> commands){
		this.commands = commands;
	}
	
	public void apply(Boss boss){
		commands.get(currentCommand).apply(boss);
		currentCommand++;
		if(currentCommand>=commands.size()){
			currentCommand = currentCommand%commands.size();
		}
	}
}
