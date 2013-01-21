import java.util.ArrayList;


public class AttackPattern {
	protected ArrayList<Command> commands;
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
