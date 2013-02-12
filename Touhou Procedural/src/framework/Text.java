package framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

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
		
		}
	
	@Override
	public Graphics render(Graphics g){
		if(this.visible){
			char[] c = this.text.toCharArray();
			g.setColor(color);
			g.setFont(this.font);
			g.drawChars(c, 0, this.text.length(),(int)x,(int)y);
		}
		return g;
	}
	
}