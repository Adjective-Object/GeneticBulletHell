package framework;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class SimpleFadeoutComponent extends GameComponent{
	
	protected long millisToLive;
	protected int fadeoutFrames, alpha;
	protected boolean fadeout;
	
	public SimpleFadeoutComponent(int x, int y, int width, int height, Color c, long millistolive, int fadeoutmillis, int boundMethod, boolean fadeout){
		super(x,y,width,height,c,new Point(10,10),new Point(Global.width, Global.height),boundMethod);
		this.millisToLive=millistolive+fadeoutmillis;
		this.fadeoutFrames=fadeoutmillis;
		this.fadeout=fadeout;
	}
	
	public void update(long elapsedTime){
		millisToLive-=elapsedTime;
		if(millisToLive<fadeoutFrames && millisToLive>0){
			this.scale = new Point((float)(millisToLive)/fadeoutFrames,(float)(millisToLive)/fadeoutFrames);
			if(this.fadeout){
				this.color = new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(),(int)(255*(float)(millisToLive)/fadeoutFrames));
			}
		}
		if(millisToLive<=0){
			kill();
		}
		super.update(elapsedTime);
	}
	
	public Graphics render(Graphics g){
		if (visible){
			g.setColor(this.color);
			g.fillRect(
					(int)(getCenter().x-size.x/2*(scale.x)),
					(int)(getCenter().y-size.y/2*(scale.y)),
					(int)(size.x*scale.x),
					(int)(size.y*scale.y));
		}
		return g;
	}
}
