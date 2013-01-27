package framework;

import java.awt.Color;

public class RelativeColorComponent extends GameComponent{
	protected int relativeRed, relativeGreen, relativeBlue;
	public Color baseColor;
	protected Color currentColor;
	
	public RelativeColorComponent(int x,int y, int width, int height, Color baseColor,
			int relativeRed, int relativeGreen, int relativeBlue){
		this(x,y,width,height,baseColor,relativeRed, relativeGreen,relativeBlue,new Point(0,0),new Point(Global.width,Global.height),GameComponent.BOUNDARY_BLOCK);
	}
	
	public RelativeColorComponent(int x, int y, int width, int height,
			Color baseColor, int relativeRed, int relativeGreen, int relativeBlue,
			Point point, Point point2, int bindMode) {
		super(x,y,width,height,new Color(
				baseColor.getRed()+relativeRed,
				baseColor.getGreen()+relativeGreen,
				baseColor.getBlue()+relativeBlue),
				point,point2,bindMode);
		this.baseColor=baseColor;
		this.relativeRed=relativeRed;
		this.relativeGreen=relativeGreen;
		this.relativeBlue=relativeBlue;
		currentColor = getColor();
	}

	public Color getColor(){
		return new Color(baseColor.getRed()+relativeRed,baseColor.getGreen()+relativeGreen,baseColor.getBlue()+relativeBlue);
	}
	
	public void setRelativeTo(Color c){
		this.baseColor = c;
	}
	
	@Override
	public void update(long elapsedTime){
		Color oldCC = currentColor;
		currentColor = getColor();
		if(!oldCC.equals(currentColor)){
			makeImage(getWidth(),getHeight(),currentColor);
		}
		super.update(elapsedTime);
	}
	
	
}
