package atouhougame.bullets;

import java.awt.Color;

import atouhougame.gamescreens.TouhouGame;

import framework.GameComponent;
import framework.RelativeColorComponent;

public abstract class Bullet extends RelativeColorComponent{

	public double power, bulletSize;
	
	public Bullet(double x, double y, double size, double power, Color c){
		this(x, y, size, power, c, 0,0,0);
	}
	
	public Bullet(double x, double y, double size,
			double power,
			Color color, int relativeRed, int relativeGreen, int relativeBlue ){
		super(x, y, size, size, color, relativeRed, relativeGreen, relativeBlue,
				TouhouGame.playFieldLeft, TouhouGame.playFieldRight, GameComponent.BOUNDARY_KILL_ON_CROSS);
		this.bulletSize=size;
		this.power=power;
	}
	
}
