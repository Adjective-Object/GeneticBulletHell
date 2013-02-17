package atouhougame.patterncommands;
import java.util.ArrayList;

import atouhougame.Boss;
import atouhougame.bullets.BulletSeed;
import atouhougame.bullets.WaitingBullet;

public class FireCommand extends Command{
	
	boolean staggered, symmetrical;
	
	protected ArrayList<BulletSeed> bullets = new ArrayList<BulletSeed>(0);
	
	public FireCommand(ArrayList<BulletSeed> bullets, boolean staggered, boolean symmetrical){
		this.bullets=bullets;
		this.staggered=staggered;
		this.symmetrical=symmetrical;
	}
	
	@Override
	public void apply(Boss boss){
		
		for(int i=0;i<bullets.size();i++){
			int symmetries=1;
			double angleStep=0;
			if(symmetrical){
				symmetries=boss.symmetry;
				angleStep=2*Math.PI/symmetries;
			}
			
			if(boss.bullets.size()<boss.volleySize &&  boss.MP>bullets.get(i).getManaCost()*symmetries){
				for(int s=0; s<symmetries; s++){
					if(staggered){
						boss.bullets.add(new WaitingBullet(bullets.get(i).rotatedCopy(angleStep*s).makeBullet(boss), (int)(i*boss.comRate/bullets.size()) ));
					} else{
						boss.bullets.add(bullets.get(i).rotatedCopy(angleStep*s).makeBullet(boss));
					}
				}
				boss.MP-=bullets.get(i).getManaCost()*symmetries;
			}
			else{
				break;
			}
		}
	}
	
}
