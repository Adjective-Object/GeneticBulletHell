package atouhougame.patterncommands;

import atouhougame.Boss;
import atouhougame.Player;

public class MoveCommand extends Command{
	
	protected int x,y;
	protected double urgency, angle;
	protected boolean lookToPlayer;
	
	public MoveCommand(int x, int y, double angle, boolean lookToPlayer){
		this.x=x;
		this.y=y;
		this.angle=angle;
		this.lookToPlayer=lookToPlayer;
	}
	
	@Override
	public void apply(Boss boss, Player player){
		boss.destX=x;
		boss.destY=y;
		boss.destAngle=angle;
		boss.lockedToPlayer=this.lookToPlayer;
	}
}
