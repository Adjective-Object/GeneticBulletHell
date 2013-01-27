package atouhougame.patterncommands;

import atouhougame.Boss;

public class MoveCommand extends Command{
	
	protected int x,y;
	protected double urgency;
	
	public MoveCommand(int x, int y, double urgency){
		this.x=x;
		this.y=y;
		this.urgency=urgency;
	}
	
	public void apply(Boss boss){
		boss.destX=x;
		boss.destY=y;
	}
}
