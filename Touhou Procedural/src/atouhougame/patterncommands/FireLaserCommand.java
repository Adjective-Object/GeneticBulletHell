package atouhougame.patterncommands;

import java.util.ArrayList;

import atouhougame.Boss;
import atouhougame.bullets.BulletSeed;
import atouhougame.bullets.Laser;

public class FireLaserCommand extends Command{
	
	ArrayList<BulletSeed> seedValues;
	
	public FireLaserCommand(ArrayList<BulletSeed> seedValues){
		this.seedValues=seedValues;
	}	
	
	@Override
	public void apply(Boss boss) {
		for(int i=0;i<seedValues.size();i++){
			if(boss.bullets.size()<boss.volleySize &&  boss.MP>seedValues.get(i).getManaCost()*2){
				boss.bullets.add(
						new Laser(boss.getCenter().x, boss.getCenter().y, seedValues.get(i).angle+boss.angle, seedValues.get(i).size, seedValues.get(i).power, boss.baseColor)
						);
				boss.MP-=seedValues.get(i).getManaCost()*2;
			}
			else{
				break;
			}
		}
	}

}
