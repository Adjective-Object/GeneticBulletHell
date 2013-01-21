package framework;
import java.util.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import java.awt.image.BufferedImage;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


public abstract class Game extends JPanel implements ActionListener{
	
	protected long lastUpdate = System.currentTimeMillis();
	protected long elapsedTime = 0;
	public static int fps = 60, width, height, endGameDelay = 0;	
	protected BufferedImage bufferImage;
	protected Timer frameRefresh, killGame;
	
	public static String endGameText = "gameOver";
	
	public Color bkgColor = new Color(0,0,0);
	public Game nextGame;
	
	protected ArrayList<GameComponent> content = new ArrayList<GameComponent>(0);
	
	public Game(){
		
		this.setSize(width,height);
		this.setPreferredSize(new Dimension(width,height));
		
		setupDoubleBuffer();
		
		setupListeners();
	}
	
	/**
	 * creates the variables needed to double buffer
	 **/
	protected void setupDoubleBuffer(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();
		bufferImage = gc.createCompatibleImage(800, 600, Transparency.OPAQUE);
	}
	
	/**
	 * sets up the various timers
	 **/
	protected void setupListeners(){
		frameRefresh = new Timer(1000/fps,this);
		frameRefresh.setActionCommand("Refresh_Screen");
	}
	
	/**
	 * starts the game
	 */
	public void start(){
		frameRefresh.start();
	}
	
	/**
	 * stops the game
	 */
	public void stop(){
		frameRefresh.stop();
	}
	
	public JFrame getUltimateFrame(){
		Component p = this;
		while (p!=null && !(p instanceof JFrame)){
			p=p.getParent();
		}
		return (JFrame)p;
	}
	
	/**
	 * parses performed actions. used to do timer based actions, primarily. 
	 * will dispatch an event to the parent so it can parse the
	 * various ending conditions of the game;
	 **/
	public void actionPerformed (ActionEvent evt){
		if (evt.getActionCommand().equals("Refresh_Screen")){
			update();
			try{
				paint(getGraphics());
			}
			catch(NullPointerException e){
				e.printStackTrace();
			}	
		}
		else if (evt.getActionCommand().equals("Kill_Game")){
			stop();
			ActionEvent endGame = new ActionEvent(this,1,endGameText);
			this.getParent().dispatchEvent(endGame);
		}
	}
	
	/**
	 * advances the game one step
	 * returns "true" if the game should continue
	 **/
	public void update(){
		this.elapsedTime = System.currentTimeMillis() - lastUpdate;
		lastUpdate = System.currentTimeMillis();
		
		for(int i=0; i<content.size();i++){
			content.get(i).update(elapsedTime);
			if(!content.get(i).alive){
				this.content.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * adds a new Component to the game Component
	 */
	public void add(GameComponent gcomponent){
		this.content.add(gcomponent);
		gcomponent.setParent(this);
	}
	
	/**
	 * prepares and draws the double buffered image onto the screen
	 * called automatically by Swing magyks
	 **/
	public void paint(Graphics g){
		
		renderToBuffer();
		
		try{ g.drawImage(bufferImage,0,0,this); }
		catch(NullPointerException nex){}
	}
	
	/**
	 * renders to the bufferedImage that stores the image that will be painted onto the
	 * target graphics
	 * 
	 * called automatically by paint(Graphics g)
	 */
	public void renderToBuffer(){
		Graphics bufferGraphics = bufferImage.getGraphics();
		
		bufferGraphics.setColor(bkgColor);
		bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		for(int i=0; i<content.size(); i++){
			content.get(i).render(bufferImage);
		}
	}
}

