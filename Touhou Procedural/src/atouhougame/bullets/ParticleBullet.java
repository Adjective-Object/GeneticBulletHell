package atouhougame.bullets;
import java.awt.Color;

import atouhougame.gamescreens.TouhouGame;

import framework.*;

public class ParticleBullet extends Bullet{

	public double torque;
	public double friction;
	public Point acceleration;
	public int lifetime=0, maxLifetime;
	
	public ParticleBullet(int x, int y, int power,
			double speed, double direction,
			double acceleration, double torque,
			double friction, int size, 
			Color color, double relativeRed, double relativeGreen, double relativeBlue){
		super(x-size/2,y-size/2,size,
				power, color, (int)relativeRed,(int)relativeGreen,(int)relativeBlue);
		this.torque=torque;
		this.velocity = Global.rotate(0, speed, direction);
		this.acceleration = new Point(acceleration*velocity.x, acceleration*velocity.y);
		this.maxLifetime = (int) (20000/(10+power));
		this.friction=friction;
	}

	public void update(long elapsedTime){
		this.velocity = Global.rotate(this.velocity,torque);
		this.velocity.x += this.acceleration.x*elapsedTime/1000;
		this.velocity.y += this.acceleration.y*elapsedTime/1000;
		this.acceleration=this.acceleration.scale(this.friction);
		this.velocity=this.velocity.scale(this.friction);
		
		super.update(elapsedTime);
		
		if(Math.abs(this.velocity.x)<=1 && Math.abs(this.velocity.y)<=1){
			lifetime+=elapsedTime;
		}
		if(lifetime>maxLifetime){
			TouhouGame tg = (TouhouGame)this.parentGame;
			tg.particles.add(new SimpleFadeoutComponent(
					(int)this.x,(int)this.y,
					(int)this.size.x, (int)this.size.y,
					this.color, 0, 100, GameComponent.BOUNDARY_NONE, true));
			this.kill();
		}
		
	}
	
}
