package atouhougame.patterncommands;
import java.io.Serializable;
import java.util.ArrayList;

import atouhougame.Boss;
import atouhougame.Player;


public class AttackPattern implements Serializable{
	public ArrayList<Command> commands;
	protected int currentCommand;
		
	public AttackPattern(ArrayList<Command> commands){
		this.commands = commands;
	}
	
	public void apply(Boss boss, Player player){
		commands.get(currentCommand).apply(boss, player);
		currentCommand++;
		if(currentCommand>=commands.size()){
			currentCommand = currentCommand%commands.size();
		}
	}
}
