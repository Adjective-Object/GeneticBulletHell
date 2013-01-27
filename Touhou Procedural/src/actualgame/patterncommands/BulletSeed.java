package actualgame.patterncommands;
import java.awt.Color;
import java.util.Random;

import actualgame.Boss;
import actualgame.Bullet;


public class BulletSeed {
	
	protected double power, angle, torque, acceleration, speed;
	protected int size;
	
	/**
	 * makes a bulletseed with random properties
	 */
	public BulletSeed(Random r){
		angle = r.nextDouble()*(2*Math.PI);
		power = r.nextDouble();//0.0-1.0
		speed = 1.0+r.nextDouble()*2.0; // 1-3
		size = (int) (5+r.nextDouble()*15);
		torque = r.nextDouble()*0.005-r.nextDouble()*0.005;
		acceleration = 0.99+r.nextDouble()*0.02;
	}

	/**
	 * makes a bulletseed with random properties
	 */
	public BulletSeed(){
		this(new Random());
	}
	/**
	 * makes an in-game bullet from the bulletSeed;
	 * @param boss
	 * @return
	 */
	public Bullet makeBullet(Boss boss){
		Bullet toRet = new Bullet((int)boss.getCenter().x, (int)boss.getCenter().y,
				(int)(power*boss.power),
				speed*boss.bulletSpeed,
				angle,
				acceleration,
				torque,
				size,
				boss.baseColor,
				35*(1-power),
				35*(1-power),
				35*(1-power)
		);
		return toRet;
		
		
	}

	public double getManaCost() {
		return 0.002*(this.power+this.speed+Math.abs(1-this.acceleration)*10+this.torque);
	}
}
