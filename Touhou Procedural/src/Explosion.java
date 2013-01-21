

import java.awt.Color;
import java.awt.Graphics;

import framework.GameComponent;
import framework.Global;
import framework.Group;

public class Explosion extends GameComponent{
	
	private double endSize, alpha=255;
	
	public long elapsedExplosion=0;
	
	public Explosion(double x, double y, double endSize, Color c){
		super(x,y,1,1,c);
		this.endSize=endSize;
	}
	
	public void update( long elapsedTime){
		
		elapsedExplosion+= elapsedTime;
		
		this.scale.x = endSize - (250.0/elapsedExplosion)*endSize;
		this.scale.y = endSize - (250.0/elapsedExplosion)*endSize;
		
		if(elapsedExplosion>1000){
			this.alpha = (30.0/Math.pow(elapsedExplosion-1000,1.15))*255;
		}
		else{
			this.alpha=255;
		}
		
		if(this.alpha>=255){
			this.alpha=255;
		}
		
		if(this.alpha<=5 && this.alpha>=0){
			this.kill();
		}
		TouhouGame tg= (TouhouGame)(this.parentGame);
		this.killonCollide(tg.bossBullets);
	}
	
	public void killonCollide(Group g){
		for (int i=0; i<g.size();i++){
			GameComponent c = g.content.get(i);
			if(Math.sqrt( Math.pow(x-c.getCenter().x,2) + Math.pow(y-c.getCenter().y,2) ) <= scale.x){
				TouhouGame tg= (TouhouGame)(this.parentGame);
				tg.particles.addAll(Global.createSimpleExplosion(c));
				c.kill();
			}
		}
	}
	
	public Graphics render(Graphics g){
		if(this.alive){
			g.setColor(new Color(this.color.getRed(),this.color.getGreen(),this.color.getBlue(),(int)this.alpha));
			g.fillOval((int)(x-(size.x*scale.x/2)),(int)((y-size.x*scale.x/2)),(int)(size.x*scale.x),(int)(size.y*scale.y));
			g.drawOval((int)(x-(size.x*scale.x)),(int)((y-size.x*scale.x)),(int)(size.x*scale.x)*2,(int)(size.y*scale.y)*2);
		}
		return g;
	}
}
