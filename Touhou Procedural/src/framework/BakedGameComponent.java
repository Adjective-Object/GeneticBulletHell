package framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BakedGameComponent extends GameComponent{
	
	protected BufferedImage image;
	public BakedGameComponent(int x, int y,BufferedImage bi,
			Point boundingSmall, Point boundingLarge, int boundaryState){
		super(x,y,bi.getWidth(), bi.getHeight(), new Color(255,255,255),boundingSmall, boundingLarge,boundaryState);
		System.out.println(bi.getWidth()+" "+bi.getHeight());
		this.image= bi;
	}
	
	public Graphics render(Graphics g){
		g.drawImage(image(),(int)x,(int)y,null);
		return g;
	}
	
	public BufferedImage image(){
		return image;
	}
}
