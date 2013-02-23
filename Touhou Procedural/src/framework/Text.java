package framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Text extends GameComponent{
	
	public String text = "NO_TEXT";
	protected int fontSize;
	public Color color;
	protected Font font;
	
	public Text(String text, Color color, Font font, double x, double d) {
		super(x,d,0,0,new Color (0,0,0,0));
		
		this.text=text;
		this.color = color;
		this.font=font;
		
		this.boundaryState=GameComponent.BOUNDARY_NONE;
		this.size.y = font.getSize();
		BufferedImage bi = new BufferedImage(1,1,BufferedImage.TYPE_3BYTE_BGR);
		this.size.x = bi.getGraphics().getFontMetrics(font).stringWidth(text);
		//Ugly, but avoids random improper width returning on FM found when called in render()
		//But that was uglier anyway.
		}
	
	@Override
	public Graphics render(Graphics g){
		char[] c = this.text.toCharArray();
		g.setColor(color);
		g.setFont(this.font);
		g.drawChars(c, 0, this.text.length(),(int)x,(int)y);
		return g;
	}
	
	@Override
	public Graphics renderHilight(Graphics g){
		g.setColor(Global.hilightColor);
		g.fillRect((int)x,(int)y-this.font.getSize(),(int)(size.x*scale.x),(int)(size.y*scale.y));
		return g;
	}
	
}