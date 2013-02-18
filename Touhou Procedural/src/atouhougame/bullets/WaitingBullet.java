package atouhougame.bullets;

import atouhougame.gamescreens.TouhouGame;


public class WaitingBullet extends Bullet{

	int waitTime;
	Bullet spawnBullet;
	
	public WaitingBullet( Bullet b, int waitTime) {
		super(0, 0, 0, 0, b.color);
		this.waitTime= waitTime;
		this.spawnBullet=b;
	}
	
	@Override
	public void update(long elapsed){
		waitTime-=elapsed;
		if(waitTime<=0){
			TouhouGame tgame = (TouhouGame)this.parentGame;
			tgame.bossBullets.add(spawnBullet);
			//TGlobal.sound_fire_boss.play();
			this.kill();
		}
	}
	

}
