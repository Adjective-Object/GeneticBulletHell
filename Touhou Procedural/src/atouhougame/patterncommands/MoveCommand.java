package atouhougame.patterncommands;

import atouhougame.Boss;

public class MoveCommand extends Command{
	
	protected int x,y;
	protected double urgency, angle;
	
	public MoveCommand(int x, int y, double angle){
		this.x=x;
		this.y=y;
		this.angle=angle;
	}
	
	@Override
	public void apply(Boss boss){
		boss.destX=x;
		boss.destY=y;
		boss.destAngle=angle;
	}
}
