package framework;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class FadeoutGameComponent extends BakedGameComponent{
	
	protected long millisToLive;
	protected int fadeoutFrames;
	public boolean shrinkOut;
	
	public FadeoutGameComponent(int x, int y, BufferedImage img, long millistolive, int fadeoutmillis, int boundMethod){
		super(x,y,img,new Point(10,10),new Point(Global.width, Global.height),boundMethod);
		this.millisToLive=millistolive;
		this.fadeoutFrames=fadeoutmillis;
	}
	
	public void update(long elapsedTime){
		millisToLive-=elapsedTime;
		if(millisToLive<=0){
			kill();
		}
		super.update(elapsedTime);
	}
	
	public Graphics render(Graphics g){
		if (visible && alive){
			Graphics2D g2 = (Graphics2D)(g);
			if (millisToLive<fadeoutFrames){
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)(millisToLive)/fadeoutFrames ));
				if(this.shrinkOut){
					this.scale = new Point((float)(millisToLive)/fadeoutFrames,(float)(millisToLive)/fadeoutFrames);
				}
			}
			g2.drawImage(image(),(int)x, (int)y, (int)(image().getWidth()*scale.x), (int)(image().getHeight()*scale.y), null);
			//g.drawImage(bi,(int)(x),(int)(y),null);
		}
		return g;
	}
}
