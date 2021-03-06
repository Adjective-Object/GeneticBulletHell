package atouhougame;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import atouhougame.bullets.ParticleBullet;
import atouhougame.gamescreens.TouhouGame;
import framework.Game;
import framework.GameComponent;
import framework.Global;
import framework.Keys;
import framework.Point;

public class Player extends GameComponent{
	
	protected int fastSpeed = 4000;
	protected int baseSpeed = 2000;
	protected int slowSpeed = 1000;
	
	public double hp=100, maxHp=100, mana=3, maxMana=10;
	
	public boolean responsive=true, canshoot=false;
	
	protected ArrayList<ParticleBullet> bullets;
	
	TouhouGame tg;
	
	/**
	 * creates the player
	 * @param x : x position
	 * @param y : y position
	 */
	public Player(int x, int y){
		super(x,y,10,10,Color.white);
		drag.x=0.85;
		drag.y=0.85;
		this.bullets = new ArrayList<ParticleBullet>(0);
		
		this.tg  = (TouhouGame) parentGame;
	}
	
	/**
	 * updates the player, supering others. only really interprets keypresses
	 */
	@Override
	public void update(long elapsedTime){
		this.acceleration=new Point(0,0);
		
		int speed = baseSpeed;
		
		if(Keys.isKeyDown(KeyEvent.VK_T)){
			hp+=100;
		}
		
		//if the player is marked as "responsive", it responds to keypresses (changeable in settings later?)
		if(responsive){
			
			if(Keys.isKeyDown(KeyEvent.VK_SHIFT)) {speed = slowSpeed;}
			if(Keys.isKeyDown(KeyEvent.VK_SPACE)) {speed = fastSpeed;}
			
			if(Keys.isKeyDown(KeyEvent.VK_LEFT)) {this.acceleration.x=-speed;}
			else if(Keys.isKeyDown(KeyEvent.VK_RIGHT)) {this.acceleration.x=speed;}
			if(Keys.isKeyDown(KeyEvent.VK_UP)) {this.acceleration.y=-speed;}
			else if(Keys.isKeyDown(KeyEvent.VK_DOWN)){this.acceleration.y=speed;}
			
			if(canshoot){
				if (Keys.isKeyDown(KeyEvent.VK_Z)){
					double bulletRange = 0.05;
					bullets.add(new ParticleBullet((int)(this.getCenter().x), (int)(this.y+this.getHeight()/4),
							1, 1000, Math.PI+new Random().nextDouble()*bulletRange-bulletRange/2,
							0,0,1,
							this.getWidth()/2, this.color,0,0,0));
					TGlobal.sound_fire_player.play();
				}
				
				if(mana>0 && Keys.isKeyPressed(KeyEvent.VK_X)){
					mana-=1;
					int highest = tg.baseColor.getRed();
					if(tg.baseColor.getGreen()>highest){highest = tg.baseColor.getGreen();}
					if(tg.baseColor.getBlue()>highest){highest = tg.baseColor.getBlue();}
					Color c = new Color(tg.baseColor.getRed()-highest+255,
							tg.baseColor.getGreen()-highest+255,
							tg.baseColor.getBlue()-highest+255);
					tg.playerBullets.add(new Explosion(x,y,255,c));
				}
			}
		}
		if(hp<=0 ){
			this.kill();
		}
		super.update(elapsedTime);
	}
	
	@Override
	public void kill(){
		TGlobal.sound_explode_player.play();
		this.responsive=false;
		this.canshoot=false;
		this.visible=false;
		this.color=Color.white;
		for(int i=0; i<3;i++){
			tg.particles.addAll(Global.createSimpleExplosion(this));
		}
		tg.bossScore+=10000;
		super.kill();
	}
	
	public ArrayList<ParticleBullet> getBullets(boolean clear){
		ArrayList<ParticleBullet> toRet = (ArrayList<ParticleBullet>) this.bullets.clone();
		if(clear){this.bullets.clear();}
		return toRet;
	}
	
	@Override
	public void setParent(Game g){
		super.setParent(g);
		tg=(TouhouGame) g;
	}
	
	

}
