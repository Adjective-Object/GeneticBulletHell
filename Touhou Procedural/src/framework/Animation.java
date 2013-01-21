package framework;

import java.awt.image.BufferedImage;

public class Animation {
	
	/**
	 * currentFrame: the current Frame
	 * 
	 * looping: if the animation loops
	 * finished: if the (non looping) animation has finished
	 **/
	int currentFrame;
	long lastUpdate;
	long millisIntoFrame;
	long millisPerFrame;
	boolean looping;
	boolean finished;
	BufferedImage[] images;
	
	/**
	 * creates a new animation
	 * @param images : the bufferedimages to play through
	 * @param millisPerFrame : number of milliseconds for each frame
	 */
	public Animation(BufferedImage[] images, long millisPerFrame){
		this.images=images;
		this.millisPerFrame = millisPerFrame;
		looping=true;
		start();
	}
	
	/**
	 * creates a new animation
	 * @param images : the bufferedimages to play through
	 * @param millisPerFrame : number of milliseconds for each frame
	 * @param looping : wheather the animation should loop or stop on the final frame
	 */
	public Animation(BufferedImage[] images, long millisPerFrame, boolean looping){
		this(images,millisPerFrame);
		looping = looping;
	}
	
	/**
	 * restarts the animation from the first frame.
	 * automatically called by Object's play() function
	 */
	public void start(){
		this.lastUpdate = System.currentTimeMillis();
		this.millisIntoFrame=0;
		this.currentFrame=0;
	}
	
	/**
	 * gets the current image
	 * @param elapsedTime the seconds elapsed in milliseconds.
	 * meant so that the aniomation's speed could be manipulated,
	 * if you ever wanted to do that...
	 * @return the current image;
	 */
	public BufferedImage getCurrentImage(long elapsed){
		elapsed += millisIntoFrame;
		this.millisIntoFrame = elapsed%millisPerFrame;
		if (looping){
			this.currentFrame = (int) ((currentFrame+(elapsed/millisPerFrame))%images.length);
		}
		else{
			this.currentFrame = (int) (currentFrame+(elapsed/millisPerFrame));
			if(currentFrame >= images.length){
				this.currentFrame = images.length-1;
				this.finished = true;
			}
		}
		return images[currentFrame];
	}
	
	/**
	 * gets the current image
	 * @return the  current image
	 */
	public BufferedImage getCurrentImage(){
		return getCurrentImage(System.currentTimeMillis()-lastUpdate);
	}
}
