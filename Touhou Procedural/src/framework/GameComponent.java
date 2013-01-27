package framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GameComponent {
	
	/**
	 * @param velocity :the rate of change of position
	 * @param acceleration :the rate of change of speed
	 * @param jerk : the rate of change of acceleration
	 * @param drag : the rate at which the speed is reduced : new speed = speed*(1/drag)
	 * @param maxVelocity : the maximum the velocity can hit. Will not set the acceleration, etc. to 0 when capped.
	 * @param image : the image
	 */
	public double x,y;
	public Point velocity = new Point(0,0);
	public Point acceleration = new Point(0,0);
	public Point jerk = new Point(0,0);
	public Point drag = new Point(1,1);
	public Point maxVelocity = new Point(-1,-1);
	public Point size;
	
	public Game parentGame;
	
	/**
	 * Generated a new Component
	 * @param x : x location of Component
	 * @param y : y location of Component
	 * @param img : image for object
	 * @param boundarySmall : top-left corner of bounding rectangle that denotes borders
	 * @param boundaryLarge : bottom-Right corner of bounding rectangle that denotes borders
	 * @param boundaryState : the type of bounding (Component.SOME_STATIC_VARIABLE)
	 */
	public Point boundingSmall = new Point(0,0);
	public Point boundingLarge = new Point(Global.width,Global.height);
	
	public Point scale = new Point(1,1);
	public boolean visible = true, alive=true;
	public Color color;
	
	public int boundaryState=0;
	public static final int BOUNDARY_NONE = 0, BOUNDARY_BLOCK = 1, BOUNDARY_KILL_ON_TOUCH = 2, BOUNDARY_KILL_ON_CROSS = 3, BOUNDARY_BOUNCE=4;
	
	
	public GameComponent(double x, double y, int width, int height, Color color,
			Point boundingSmall, Point boundingLarge, int boundaryState){
		this.x=x;
		this.y=y;
		
		this.boundingSmall=boundingSmall;
		this.boundingLarge=boundingLarge;
		
		this.boundaryState = boundaryState;
		
		this.size=new Point(width,height);
		this.color=color;
	}
	
	/**
	 * Generate a new Component
	 * @param x : x location of Component
	 * @param y : y location of Component
	 * @param width : width of auto generated image
	 * @param height : height of auto-generated image
	 * @param color : the color of auto-generated image
	 * @param boundarySmall : top-left corner of bounding rectangle that denotes borders
	 * @param boundaryLarge : bottom-Right corner of bounding rectangle that denotes borders
	 */
	public GameComponent(double x, double y, int width, int height, Color color, Point boundingSmall, Point boundingLarge){
		this(x,y,width,height,color,boundingSmall,boundingLarge,GameComponent.BOUNDARY_BLOCK);
	}
	
	/**
	 * Generated a new Component
	 * @param x : x location of Component
	 * @param y : y location of Component
	 * @param width: width of auto generated image
	 * @param height: height of auto-generated image
	 * @param color: the color of auto-generated image
	 */
	public GameComponent(double x, double y, int width, int height, Color color){
		this(x,y,width,height,color,new Point(0,0),new Point(Global.width,Global.height));
	}
	
	/**
	 * Generated a new Component
	 * @param x : x location of Component
	 * @param y : y location of Component
	 * @param width: width of auto generated image
	 * @param height: height of auto-generated image
	 */
	public GameComponent(int x, int y, int width, int height){
		this(x,y,width,height,new Color(50,50,100));
	}
	
	/**
	 * Generated a new Component
	 * @param x : x location of Component
	 * @param y : y location of Component
	 */
	public GameComponent(int x, int y){
		this(x,y,10,10);
	}
	
	
	/**
	 * moves the Component according to set variables
	 * 
	 * @param elapsedTime : time elapsed since last update in milliseconds
	 */
	public void update(long elapsedTime){
		double timeRatio = (elapsedTime)/1000.0;
		//System.out.println(velocity);
		
		this.acceleration.x += this.jerk.x*timeRatio;
		this.acceleration.y += this.jerk.y*timeRatio;
		
		this.velocity.x += this.acceleration.x*timeRatio;
		this.velocity.y += this.acceleration.y*timeRatio;
		
		if(this.maxVelocity.x!=-1){
			if (Math.abs(this.velocity.x)>this.maxVelocity.x){this.velocity.x=this.maxVelocity.x*(this.velocity.x/Math.abs(this.velocity.x));}
		}
		if(this.maxVelocity.y!=-1){
			if (Math.abs(this.velocity.y)>this.maxVelocity.y){this.velocity.y=this.maxVelocity.y*(this.velocity.y/Math.abs(this.velocity.y));}
		}
		
		this.velocity.x=this.velocity.x*(this.drag.x);
		this.velocity.y=this.velocity.y*(this.drag.y);
		
		this.x += this.velocity.x*timeRatio;
		this.y += this.velocity.y*timeRatio;
		
		applyBounding();
		
	}
	
	/**
	 * draws the Component onto the given buffered image at the images X, Y
	 * @param bi: the target bufferedImage
	 * @return : returns the altered BufferedImage
	 */
	public BufferedImage render(BufferedImage bi){
		Graphics g = bi.getGraphics();
		render(g);
		return bi;
	}
	

	/**
	 * draws the Component onto the given buffered image at the images X, Y
	 * @param g : the target Graphics
	 * @return : returns the altered Graphics
	 */
	public Graphics render(Graphics g){
		if (visible){
			g.setColor(this.color);
			g.fillRect((int)x,(int)y,(int)(size.x*scale.x),(int)(size.y*scale.y));
		}
		return g;
	}
	
	public Point getCenter(){
		return new Point(this.x+this.getWidth()/2,this.y+this.getHeight()/2);
	}
	
	/**
	 * forces inside bounding box, based on the set mode
	 */
	public void applyBounding(){
		switch(this.boundaryState){
			case GameComponent.BOUNDARY_BLOCK:
				forceBounding();
				break;
			case GameComponent.BOUNDARY_BOUNCE:
				bounceBounding();
				break;
			case GameComponent.BOUNDARY_KILL_ON_TOUCH:
				killTouchBounding();
				break;
			case GameComponent.BOUNDARY_KILL_ON_CROSS:
				killCrossBounding();
				break;			
			case GameComponent.BOUNDARY_NONE:
				break;
		}
	}
	
	/**
	 * applies bounding, such that the Component's "alive" state is set to off when it touches
	 * the boundary
	 */
	public void killTouchBounding(){
		if (
				(this.x<this.boundingSmall.x)||
				(this.x+this.getWidth()>this.boundingLarge.x)||
				(this.y<this.boundingSmall.y)||
				(this.y+this.getHeight()>this.boundingLarge.y)
				){
			this.alive = false;
		}
	}
	
	/**
	 * applies bounding, such that the Component's "alive" state is set to off when it crosses
	 * (crosses = when it's totally crossed)
	 */
	public void killCrossBounding(){
		if (
				(this.x<this.boundingSmall.x-this.getWidth())||
				(this.x>this.boundingLarge.x)||
				(this.y<this.boundingSmall.y-this.getHeight())||
				(this.y>this.boundingLarge.y)
				){
			this.alive = false;
		}
	}
	
	/**
	 * forces inside bounding box, no bounce
	 */
	public void forceBounding(){
		if(this.x<this.boundingSmall.x){
			this.x=(int) this.boundingSmall.x;
		}
		if(this.x+this.getWidth()>this.boundingLarge.x){
			this.x=(int) (this.boundingLarge.x-this.getWidth());
		}
		if(this.y<this.boundingSmall.y){
			this.y=(int) this.boundingSmall.y;
		}
		if(this.y+this.getHeight()>this.boundingLarge.y){
			this.y=(int) (this.boundingLarge.y-this.getHeight());
		}
	}
	
	/**
	 * applies bounding, such that the Components bounce.
	 */
	public void bounceBounding(){
		if((this.x<this.boundingSmall.x && this.velocity.x<0) || (this.x>this.boundingLarge.x && this.velocity.x>0)){
			this.velocity.x = -this.velocity.x;
		}
		
		if((this.y<this.boundingSmall.y && this.velocity.y<0) || (this.y>this.boundingLarge.y && this.velocity.y>0)){
			this.velocity.y = -this.velocity.y;
		}
	}
	
	
	public void kill(){
		this.alive=false;
	}
	
	/**
	 * gets the current image of the current animation
	 * only really exists to shorten codingness....
	 */
	public BufferedImage image(){
		return makeImage((int)size.x,(int)size.y,color);
	}
	
	/**
	 * tests for collisions between two Components.
	 * uses image as collision rectangle (not pixel-perfect)
	 */
	public boolean collide(GameComponent obj){
		return (x<=obj.x+obj.getWidth()) && (obj.x<=x+this.getWidth()) && (y<=obj.y+obj.getHeight()) && (obj.y<=y+this.getHeight());
	}
	
	/**
	 * gets the height of the Component
	 * @return height of Component, factoring in scale
	 */
	public int getHeight(){
		return (int) size.y;
	}
	
	/**
	 * gets the width of the Component
	 * @return width of Component, factoring in scale
	 */
	public int getWidth(){
		return (int) size.x;
	}
	
	public static BufferedImage makeImage(int width, int height, Color color){
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, width, height);
		return img;
	}
	
	public void setParent(Game g){
		this.parentGame=g;
	}
}
