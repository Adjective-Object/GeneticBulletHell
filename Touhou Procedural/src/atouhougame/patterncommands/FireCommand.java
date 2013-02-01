package atouhougame.patterncommands;
import java.util.ArrayList;

import atouhougame.Boss;
import atouhougame.bullets.BulletSeed;

public class FireCommand extends Command{
	protected ArrayList<BulletSeed> bullets = new ArrayList<BulletSeed>(0);
	
	public FireCommand(ArrayList<BulletSeed> bullets){
		this.bullets=bullets;
		
	}
	
	public void apply(Boss boss){
		
		for(int i=0;i<bullets.size();i++){
			if(boss.bullets.size()<boss.volleySize &&  boss.MP>bullets.get(i).getManaCost()){
				boss.bullets.add(bullets.get(i).makeBullet(boss));
				boss.MP-=bullets.get(i).getManaCost();
			}
			else{
				break;
			}
		}
	}
	
}
