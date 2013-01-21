import java.awt.Color;

import framework.*;

public class Bullet extends RelativeColorComponent{

	public double torque;
	public int power;
	
	public Bullet(int x, int y, int power, double speed, double direction, double torque,
			int size, Color color, double relativeRed, double relativeGreen, double relativeBlue){
		super(x-size/2,y-size/2,size,size, color, (int)relativeRed,(int)relativeGreen,(int)relativeBlue, TouhouGame.playFieldLeft, TouhouGame.playFieldRight, GameComponent.BOUNDARY_KILL_ON_CROSS);
		this.power = power;
		this.velocity = Global.rotate(0, speed, direction);
	}

	public void update(long elapsedTime){
		this.velocity=rotate(this.velocity,this.torque);
		this.torque = this.torque*0.99;
		super.update(elapsedTime);
	}
	
	/**
	 * given the current x & y move conditions in Point form, rotate to new x & y condition
	 * @return double [] with {x,y}
	 */
	public Point rotate (Point xy,double angle){
		return Global.rotate(xy.x,xy.y,angle);
	}
	
}
