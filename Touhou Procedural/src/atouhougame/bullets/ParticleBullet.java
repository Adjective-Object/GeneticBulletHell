package atouhougame.bullets;
import java.awt.Color;

import atouhougame.gamescreens.TouhouGame;
import framework.GameComponent;
import framework.Global;
import framework.Point;
import framework.SimpleFadeoutComponent;

public class ParticleBullet extends Bullet{

	static final double cutoff = 1;
	
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
		this.maxLifetime = (100);
		this.friction=friction;
	}

	@Override
	public void update(long elapsedTime){
		this.velocity = Global.rotate(this.velocity,torque);
		this.velocity.x += this.acceleration.x*elapsedTime/1000;
		this.velocity.y += this.acceleration.y*elapsedTime/1000;
		this.acceleration=this.acceleration.scale(this.friction);
		this.velocity=this.velocity.scale(this.friction);
		
		super.update(elapsedTime);
		
		if(Math.abs(this.velocity.x)<=cutoff && Math.abs(this.velocity.y)<=cutoff){
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
