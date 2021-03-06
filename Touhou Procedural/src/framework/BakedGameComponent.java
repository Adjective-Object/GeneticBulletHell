package framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BakedGameComponent extends GameComponent{
	
	protected BufferedImage image;
	public Point imageOffset;
	
	public BakedGameComponent(double x, double y,BufferedImage bi){
		this(x,y,bi,new Point(0,0), new Point(0,0), GameComponent.BOUNDARY_NONE);
	}
	
	public BakedGameComponent(double x, double y,BufferedImage bi,
			Point boundingSmall, Point boundingLarge, int boundaryState){
		super(x,y,bi.getWidth(), bi.getHeight(), new Color(255,255,255),boundingSmall, boundingLarge,boundaryState);
		this.image= bi;
		this.imageOffset=new Point(0,0);
		//this.color = Global.randomColor();
	}

	@Override
	public Graphics render(Graphics g){
		//super.render(g);
		g.drawImage(image(),(int)(x-imageOffset.x),(int)(y-imageOffset.y),null);
		return g;
	}
	
	@Override
	public BufferedImage image(){
		return image;
	}
	
	public void setImage(BufferedImage bi){
		this.size= new Point(bi.getWidth(), bi.getHeight());
		this.image=bi;
	}
}
