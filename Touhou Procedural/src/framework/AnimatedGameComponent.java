package framework;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

//TODO most everything
//TODO moving necessary methods to Component class.

public class AnimatedGameComponent extends GameComponent{
	
	public HashMap<String,Animation> animations = new HashMap<String,Animation>(0);
	public String currentAnim = "NO_ANIMATION";
	
	
	public AnimatedGameComponent(int x, int y, BufferedImage img,Point boundingSmall, Point boundingLarge, int boundaryState){
		super(x,y,img.getWidth(),img.getHeight(),new Color(255,255,255),boundingSmall,boundingLarge,boundaryState);
		
		this.animations.clear();
		this.animations.put(currentAnim, new Animation(new BufferedImage[] {img}, 1000));
	}
	
	public AnimatedGameComponent(int x, int y, BufferedImage img,Point boundingSmall, Point boundingLarge){
		this(x,y,img,boundingSmall,boundingLarge,BOUNDARY_BLOCK);
	}
	
	public AnimatedGameComponent(int x, int y,BufferedImage img){
		this(x,y,img,new Point(0,0), new Point(Global.width,Global.height));
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
	 * @param boundaryState : the type of bounding (in Component.somestaticvariable)
	 */
	
	
	/**
	 * plays a new animation,resetting the animation from the start
	 * @param animation : the animation to play
	 */
	public void play(String animation){
		this.currentAnim = animation;
		animations.get(this.currentAnim).start();
	}
	
	
	/**
	 * draws the Component onto the given buffered image at the images X, Y
	 * @param bi: the target bufferedImage
	 * @return : returns the altered BufferedImage
	 */
	@Override
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
	@Override
	public Graphics render(Graphics g){
		if (visible){
			BufferedImage bi = this.image();
			Graphics2D g2 = (Graphics2D)(g);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(bi,(int)x, (int)y, (int)(bi.getWidth()*scale.x), (int)(bi.getHeight()*scale.y), null);
			//g.drawImage(bi,(int)(x),(int)(y),null);
		}
		return g;
	}
}
