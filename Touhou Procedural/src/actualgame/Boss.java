package actualgame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import actualgame.gamescreens.TouhouGame;
import actualgame.patterncommands.AttackPattern;
import framework.BakedGameComponent;
import framework.FadeoutGameComponent;
import framework.Game;
import framework.GameComponent;
import framework.Global;
import framework.Group;
import framework.Keys;
import framework.Point;

public class Boss extends BakedGameComponent{
	
	protected ArrayList<AttackPattern> patterns = new ArrayList<AttackPattern>(0);
	public Group<Bullet> bullets = new Group<Bullet>();
	
	public int destX, destY;
	
	/**
	 * fireRate: Bullets per Second
	 * fireMillis: milliseconds since last bullet fired
	 * manaRegenRate: rate of mana regeneration in mana / second
	 */
	public int lastfired=0, HP, maxHP, maxMP, volleySize, currentPhase=0, totalPhases;
	public long comMillis=0;
	public double MP, manaRegenRate, comRate, weight, moveSpeed,dashDist, power,bulletSpeed,torquePower;
	double radius;
	
	public boolean phaseChanged= false, active = false;
	
	public Color baseColor;
	
	public Boss(int x, int y, BossSeed seed){
		super(x,y,makeImage(seed),TouhouGame.playFieldLeft,TouhouGame.playFieldRight,GameComponent.BOUNDARY_BLOCK);
		
		this.baseColor 		=seed.color;
		this.power 			=seed.STR*0.5;
		this.bulletSpeed	=seed.DEX;
		this.patterns		=seed.patterns;
		this.weight			=(seed.STR/5.0+seed.CON)/2;
		this.maxHP			=(int) seed.CON;
		this.HP				=maxHP;
		this.maxMP			=(int) seed.WIS/2;
		this.MP				=maxMP/4.0;
		this.manaRegenRate	=seed.INT/6000;
		this.moveSpeed		=5+10*seed.LUK/seed.CON;
		this.volleySize		=(int)(seed.INT*0.8+seed.STR*0.6+seed.DEX*0.4);//most # of bulltets onscreen at the time
		this.comRate		=500+seed.CON/2+seed.STR/4-seed.INT-seed.DEX*2-seed.WIS;
		
		destX=x;
		destY=y;

		
		this.radius			=(seed.CON*0.25);
		this.size			= new Point(radius,radius);
		this.imageOffset 	= new Point(
				this.image.getWidth()/2-this.radius/2,
				this.image.getWidth()/2-this.radius/2
				);
		
	}
	
	/**
	 * updates the boss
	 * "shot" bullets are in fact stored to an internal list meant to be called by the game object the boss is a part of, then added to the component list of that game
	 */
	@Override
	public void update(long elapsedTime){
		
		if(this.active){
			
			//mana regen
			this.MP+=manaRegenRate*(1.0*elapsedTime/1000);
			if(MP>maxMP){MP=maxMP;}
			
			//using Commands
			comMillis+=elapsedTime;
			if(comMillis>comRate){
				this.patterns.get(currentPhase).apply(this);
				comMillis=(long)(comMillis-comRate);
			}
			
			//movement
			if(destX!=x || destY!=y){
				Point pawnt = Global.scaleAlong(moveSpeed*(elapsedTime/1000.0),Global.getsincos(x,y,destX,destY));
				
				//if catches to avoid jittering
				if(Math.abs(x-destX)<pawnt.x){
					this.x=destX;
				}
				else{
					x+=pawnt.x;
				}
				if(Math.abs(y-destY)<pawnt.y){
					this.y=destY;
				}
				else{
					this.y+=pawnt.y;
				}
			}
		
			if(HP<=0 || Keys.isKeyPressed(KeyEvent.VK_T))
			{
				if(currentPhase >= patterns.size()-1){
					this.kill();
				}
				else{
					HP=maxHP;
					currentPhase++;
					phaseChanged=true;
				}
			}
			
		}
		
		super.update(elapsedTime);
	}
	
	@Override
	public void kill(){
		if(this.visible && this.active){
			this.visible=false;
			this.active=false;
			TouhouGame g = (TouhouGame) this.parentGame;
			g.particles.addAll(Global.createSimpleExplosion(5,(int)this.size.x*4,this.color,
						this.getCenter(),this.velocity,2000,1000,(int)this.size.x, true));	
			g.particles.add(new Explosion(this.getCenter().x, this.getCenter().y,this.size.x*4,this.color));
			g.particles.add(new FadeoutGameComponent((int)(this.getCenter().x), (int)(this.getCenter().y), this.image, 0, 1000, GameComponent.BOUNDARY_KILL_ON_CROSS, true));
		}
		super.kill();
	}
	
	/**
	 * meant to be called by the Game object the boss is in. 
	 * @return all the bullets in that the boss has been storing
	 */
	public Group<Bullet> getBullets(boolean remove){
		Group<Bullet> bull = bullets.clone();
		if (remove){bullets.clear();}
		return bull;
	}
	
	@Override
	public void setParent(Game g){
		this.bullets.setParent(g);
		super.setParent(g);
	}
	
	public static BufferedImage makeImage(BossSeed seed){
		
		
		double radius = seed.CON*0.25+8;
		double numstep = 3+seed.LUK;
		double anglestep = 2*Math.PI/numstep;
		double spikeyness = radius * (0.5*seed.DEX/seed.STR);
		if(spikeyness<1){
			spikeyness=1;
		}
		
		BufferedImage img = new BufferedImage(2*(int)(radius+spikeyness),2*(int)(radius+spikeyness),BufferedImage.TYPE_INT_ARGB);
		int center = img.getWidth()/2;
		Graphics g = img.getGraphics();
		
		Polygon poly = new Polygon();
		
		
		Random r = new Random((long) (seed.CON+seed.DEX+seed.INT+seed.LUK+seed.STR+seed.WIS));
		
		double tempradius = radius;
		
		for(int i=0; i<numstep; i++){
			tempradius = radius+spikeyness*(r.nextDouble()-r.nextDouble());
			Point p = Global.rotate(new Point(0, -tempradius),anglestep*i);
			
			poly.addPoint(center+(int)p.x, center+(int)p.y);
			
		}
		
		
		g.setColor(new Color(seed.color.getRed()+20, seed.color.getGreen()+20, seed.color.getBlue()+20));
		g.fillOval((int)spikeyness, (int)spikeyness, (int)(radius*2), (int)(radius*2));
		g.setColor(seed.color);
		g.fillPolygon(poly);
		
		return img;
	}
}
