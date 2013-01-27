package framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BakedGameComponent extends GameComponent{
	
	protected BufferedImage image;
	protected Point imageOffset;
	
	public BakedGameComponent(int x, int y,BufferedImage bi){
		this(x,y,bi,new Point(0,0), new Point(0,0), GameComponent.BOUNDARY_NONE);
	}
	
	public BakedGameComponent(int x, int y,BufferedImage bi,
			Point boundingSmall, Point boundingLarge, int boundaryState){
		super(x,y,bi.getWidth(), bi.getHeight(), new Color(255,255,255),boundingSmall, boundingLarge,boundaryState);
		System.out.println(bi.getWidth()+" "+bi.getHeight());
		this.image= bi;
		this.imageOffset=new Point(0,0);
	}
	
	@Override
	public Graphics render(Graphics g){
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
