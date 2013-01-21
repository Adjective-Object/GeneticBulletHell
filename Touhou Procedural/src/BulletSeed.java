import java.awt.Color;
import java.util.Random;


public class BulletSeed {
	
	protected double power, angle, torque, speed;
	protected int size;
	
	/**
	 * makes a bulletseed with random properties
	 */
	public BulletSeed(Random r){
		angle = r.nextDouble()*(2*Math.PI);
		power = r.nextDouble();//0.0-1.0
		speed = 1.0+r.nextDouble()*2.0; // 1-3
		size = (int) (5+r.nextDouble()*15);
		torque = r.nextDouble()*0.05-r.nextDouble()*0.05;
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
		Random r = new Random();
		Bullet toRet = new Bullet((int)boss.getCenter().x, (int)boss.getCenter().y,
				(int)(power*boss.power),
				speed*boss.bulletSpeed,
				angle,
				torque*boss.torquePower,
				size,
				boss.baseColor,
				35*(1-power),
				35*(1-power),
				35*(1-power)
		);
		return toRet;
		
		
	}
}
