package framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ParagraphText extends Text{

	String[] texts;
	int heightDiff;
	
	public ParagraphText(String[] texts, Color color, Font font, double x, double y, int heightDifference) {
		super("", color, font, x, y);
		
		this.texts = texts;
		heightDiff=heightDifference;
	}
	
	public ParagraphText(String text, Color color, Font font, double x, double y, int heightDifference) {
		super(text, color, font, x, y);
		
		this.texts = text.split("\n");
		heightDiff=heightDifference;
	}
	
	public Graphics render(Graphics g){
		if(this.visible){
			for(int i=0; i<this.texts.length; i++){
				char[] c = this.texts[i].toCharArray();
				g.setColor(color);
				g.setFont(this.font);
				g.drawChars(c, 0, this.texts[i].length(),(int)x,(int)y+i*(heightDiff+font.getSize()));
			}
		}
		return g;
	}
	
}
