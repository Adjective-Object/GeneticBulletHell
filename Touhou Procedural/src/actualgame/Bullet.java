package actualgame;
import java.awt.Color;

import actualgame.gamescreens.TouhouGame;

import framework.*;

public class Bullet extends RelativeColorComponent{

	public double torque;
	public double acceleration;
	public int power, lifetime=0, maxLifetime;
	
	public Bullet(int x, int y, int power,
			double speed, double direction,
			double acceleration, double torque,
			int size, 
			Color color, double relativeRed, double relativeGreen, double relativeBlue){
		super(x-size/2,y-size/2,size,size, color, (int)relativeRed,(int)relativeGreen,(int)relativeBlue, TouhouGame.playFieldLeft, TouhouGame.playFieldRight, GameComponent.BOUNDARY_KILL_ON_CROSS);
		this.power = power;
		this.torque=torque;
		this.acceleration = acceleration;
		this.velocity = Global.rotate(0, speed, direction);
		this.maxLifetime = (int) (20000/(10+power));
	}

	public void update(long elapsedTime){
		this.velocity = Global.rotate(this.velocity.scale(acceleration),torque);
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
	
	/**
	 * for updating bullet arcs in bullet subtypes.
	 * @return double [] with {x,y}
	 */
	public void updateBulletArc(){
		
	}
	
}
