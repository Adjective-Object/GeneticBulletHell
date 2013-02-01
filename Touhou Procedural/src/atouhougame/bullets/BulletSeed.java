package atouhougame.bullets;
import java.io.Serializable;
import java.util.Random;

import atouhougame.Boss;


public class BulletSeed implements Serializable{
	
	public double power, angle, torque, acceleration, speed, friction;
	public int size;
	
	public BulletSeed(double angle, double power, double speed, int size, double torque, double acceleraton, double friction){
		this.size=size;
		this.speed=speed;
		this.power=power;
		this.torque=torque;
		this.acceleration=acceleraton;
		this.angle=angle;
		this.friction=friction;
	}
	
	/**
	 * makes a bulletseed with random properties
	 */
	public BulletSeed(Random r){
		angle = r.nextDouble()*(2*Math.PI);
		power = r.nextDouble();//0.0-1.0
		speed = 1.0+r.nextDouble()*2.0; // 1-3
		size = (int) (5+r.nextDouble()*15);
		torque = r.nextDouble()*0.005-r.nextDouble()*0.005;
		acceleration = r.nextDouble()*-r.nextDouble();
		friction = 0.99+r.nextDouble()*0.02;
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
	public ParticleBullet makeBullet(Boss boss){
		ParticleBullet toRet = new ParticleBullet((int)boss.getCenter().x, (int)boss.getCenter().y,
				(int)(power*boss.power),
				speed*boss.bulletSpeed,
				angle,
				acceleration,
				torque,
				friction,
				size,
				boss.baseColor,
				35*(1-power),
				35*(1-power),
				35*(1-power)
		);
		return toRet;
		
		
	}

	public double getManaCost() {//TODO rebalance
		return 0.0002*(this.power+this.speed+Math.abs(1-this.acceleration)*10+this.torque);
	}

	public BulletSeed rotatedCopy(double angle) {
		return new BulletSeed(this.angle+angle, power, speed, size, torque, acceleration, friction);
	}
}
