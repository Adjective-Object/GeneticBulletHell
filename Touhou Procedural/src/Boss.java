import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import framework.*;

public class Boss extends BakedGameComponent{
	
	protected ArrayList<AttackPattern> patterns = new ArrayList<AttackPattern>(0);
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>(0);
	
	public double movementUrgency;
	public int destX, destY;
	
	/**
	 * fireRate: Bullets per Second
	 * fireMillis: milliseconds since last bullet fired
	 * manaRegenRate: rate of mana regeneration in mana / second
	 */
	protected int lastfired=0, HP, maxHP, maxMP, volleySize, currentPhase=0, totalPhases;
	protected long comMillis=0;
	protected double MP, manaRegenRate, comRate, weight, moveSpeed,dashDist, power,bulletSpeed,torquePower;
	
	public boolean phaseChanged= false, active = false;
	
	public Color baseColor;
	
	public Boss(int x, int y, Color baseColor,ArrayList<AttackPattern> patterns, double damage, double weight,
			int HP, int MP, double manaRegenRate, double moveSpeed, double reactionRate,int volleySize, double bulletSpeed, double torquePower,
			double dodgeChance){
		super(x,y,makeImage((int)weight,(int)weight,baseColor),TouhouGame.playFieldLeft,TouhouGame.playFieldRight,GameComponent.BOUNDARY_BLOCK);
		this.baseColor = baseColor;
		this.power =damage;
		this.bulletSpeed=bulletSpeed;
		this.patterns=patterns;
		this.weight=weight;
		this.HP=HP;
		this.maxHP=HP;
		this.maxMP=MP;
		this.MP=maxMP/4.0;
		this.manaRegenRate=manaRegenRate;
		this.moveSpeed=moveSpeed;
		this.comRate=reactionRate;
		this.torquePower=torquePower;
		this.volleySize=volleySize;
		this.dashDist=dashDist;
		
		destX=x;
		destY=y;
		movementUrgency=0;
		
		System.out.println(this.comRate);
	}
	
	/**
	 * updates the boss
	 * "shot" bullets are in fact stored to an internal list meant to be called by the game object the boss is a part of, then added to the component list of that game
	 */
	public void update(long elapsedTime){
		
		if(this.active){
			
			//mana regen
			this.MP+=manaRegenRate*(1.0*elapsedTime/1000);
			if(MP>maxMP){MP=maxMP;}
			
			//using Commands
			comMillis+=elapsedTime;
			if(comMillis>comRate*(1-movementUrgency)){
				this.patterns.get(currentPhase).apply(this);
				comMillis=(long)(comMillis-comRate);
			}
			
			//movement
			if(movementUrgency!=0 && (destX!=x || destY!=y)){
				Point pawnt = Global.rotate(moveSpeed*(elapsedTime/1000.0)*movementUrgency,0.0,Global.findAngle(x,y,destX,destY));
				
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
		
			if(HP<=0)
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
	
	public void kill(){
		if(this.visible && this.active){
			this.visible=false;
			this.active=false;
			TouhouGame g = (TouhouGame) this.parentGame;
			g.particles.addAll(Global.createSimpleExplosion(5,(int)this.size.x*4,this.color,
						this.getCenter(),this.velocity,2000,1000,(int)this.size.x, true));	
			//Touhou.particles.add(new FadeoutComponent((int)getCenter().x,(int)getCenter().y,image(),0,10000,Component.BOUNDARY_NONE));
			g.playerScore+=10000;
			g.particles.add(new Explosion(this.getCenter().x, this.getCenter().y,this.size.x*4,this.color));
		}
		super.kill();
	}
	
	/**
	 * meant to be called by the Game object the boss is in. 
	 * @return all the bullets in that the boss has been storing
	 */
	public ArrayList<Bullet> getBullets(boolean remove){
		ArrayList<Bullet> bull = (ArrayList<Bullet>) bullets.clone();
		if (remove){bullets.clear();}
		return bull;
	}
	
	public static BufferedImage makeImage(int width, int height, Color drawColor){
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		//g.clearRect(0, 0, width, height);
		
		int sv = 3;
		Random r = new Random();
		for(int i=0;i<width*height;i++){
			g.setColor(new Color((int)(drawColor.getRed()+r.nextDouble()*80-5) ,
						(int)(drawColor.getGreen()+r.nextDouble()*80-5) ,
						(int)(drawColor.getBlue()+r.nextDouble()*80-5) ));
			
			Point pixel = Global.rotate(0,r.nextDouble()*height/2,r.nextDouble()*(Math.PI*2));
			int p =(int) Math.round(r.nextDouble()*sv);
			g.fillRect((int)pixel.x+width/2,(int)pixel.y+height/2, p,p);
		}
		
		return img;
	}
}
