package atouhougame.patterncommands;
import java.util.ArrayList;

import atouhougame.Boss;
import atouhougame.Player;
import atouhougame.TGlobal;
import atouhougame.bullets.BulletSeed;
import atouhougame.bullets.WaitingBullet;

public class FireCommand extends Command{
	
	boolean staggered, symmetrical, symmetrical2, fireRows, spinning, cloneVolley;
	
	protected ArrayList<BulletSeed> bullets = new ArrayList<BulletSeed>(0);
	
	public FireCommand(ArrayList<BulletSeed> bullets, boolean staggered, boolean spinning, boolean symmetrical, boolean symmetrical2, boolean fireRows, boolean cloneVolley){
		this.bullets=bullets;
		this.staggered=true;
		this.symmetrical=symmetrical;
		this.symmetrical2=symmetrical2;
		this.fireRows = fireRows;
		this.spinning=true;
		this.cloneVolley=cloneVolley;
	}
	
	@Override
	public void apply(Boss boss, Player player){
		boolean fired = false;
		
		BulletSeed seedBullet = bullets.get(0);
		int bulletCount = 0;
		
		//calculating the axies of symmetry, and the anglestep of each axis
		int symmetries=1;
		double angleStep=0;
		if(symmetrical){
			symmetries=boss.symmetry;
			angleStep=2*Math.PI/symmetries;
		}
		
		//calculate the number of volleys to fire to keep at ~ the bullet limitation of the boss.
		int goingToFire = boss.volleySize;
		if(symmetrical){
			goingToFire=goingToFire*2;
			if(symmetrical2){
				goingToFire=goingToFire*2;
			}
		}
		if(goingToFire>bullets.size()){
			goingToFire=bullets.size();
		}
		
		int volleysToFire = goingToFire;
		if(symmetrical){
			volleysToFire=volleysToFire/2;
			if(symmetrical2){
				volleysToFire=volleysToFire/2;
			}
		}
		
		//firing bullets
		for(int i=0;i<volleysToFire;i++){
			if(bulletCount>boss.volleySize){
				break;
			}
			
			bulletCount +=symmetries;
			double manaCost = bullets.get(i).getManaCost()*symmetries;
			if(symmetrical2){
				manaCost=manaCost*2;
				bulletCount +=symmetries;
			}
			
				
			if( boss.MP>manaCost){
				fired=true;
				for(int s=0; s<symmetries; s++){
					BulletSeed b;
					if(cloneVolley){
						b = seedBullet.rotatedCopy(angleStep*s+boss.angle);
					} else{
						b = bullets.get(i).rotatedCopy(boss.angle);
					}
					
					if(staggered){
						if(fireRows){
							b.angle=seedBullet.angle;
							b.speed=seedBullet.speed;
							b.torque=seedBullet.torque;
						}
						if(spinning){
							b.angle=seedBullet.angle+i*(boss.bulletSpeed/2/Math.PI);
							b.speed=seedBullet.speed;
							b.torque=seedBullet.torque;
						}
					}
					
					if(staggered){
						boss.bullets.add(new WaitingBullet(b.makeBullet(boss), (int)(i*boss.comRate/volleysToFire) ));
					} else{
						boss.bullets.add(bullets.get(i).rotatedCopy(angleStep*s).makeBullet(boss));
					}
					if(symmetrical2 && b.angle%angleStep>1){
						b.angle=b.angle-(b.angle%angleStep)+angleStep-(b.angle%angleStep);
						b.torque = -b.torque;
						if(staggered){
							boss.bullets.add(new WaitingBullet(b.makeBullet(boss), (int)(i*boss.comRate/volleysToFire) ));
						} else{
							boss.bullets.add(bullets.get(i).rotatedCopy(angleStep*s).makeBullet(boss));
						}
					}
				}
				boss.MP-=manaCost;
			}
			else{
				break;
			}
		}
		if(fired){
			TGlobal.sound_fire_boss.play();
		}
	}
	
}
