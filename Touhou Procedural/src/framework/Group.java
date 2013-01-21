package framework;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Group extends GameComponent{
	
	public boolean removeDead = false;
	public ArrayList<GameComponent> content;
	
	public Group(ArrayList<GameComponent> content){
		this();
		this.content.addAll(content);
	}
	
	public Group(GameComponent content){
		this();
		this.content.add(content);
	}
	
	public Group(){
		super(0,0);
		this.content = new ArrayList<GameComponent>(0);
	}
	
	public Group(boolean removeDead){
		this();
		this.removeDead = true;
	}
	
	public void add(GameComponent comp){
		this.content.add(comp);
		comp.setParent(this.parentGame);
		
	}
	
	public void addAll(Group Component){
		for(int i=0; i<Component.content.size(); i++){
			Component.content.get(i).setParent(this.parentGame);
		}
		content.addAll(Component.content);
	}
	
	public int size(){
		return this.content.size();
	}
	
	public void update(long elapsedTime){
		for(int i=0;i<content.size();i++){
			content.get(i).update(elapsedTime);
			if(removeDead){
				if(!this.content.get(i).alive){
					this.content.remove(i);
					i--;
				}
			}
		}
	}
	

	/**
	 * draws the Component onto the given buffered image at the images X, Y
	 * @param bi: the target bufferedImage
	 * @return : returns the altered BufferedImage
	 */
	public BufferedImage render(BufferedImage bi){
		if (visible){
			for(int i=0; i<content.size();i++){
				bi=content.get(i).render(bi);
			}
		}
		return bi;
	}
	
	/**
	 * draws the Component onto the given buffered image at the images X, Y
	 * @param g : the target Graphics
	 * @return : returns the altered Graphics
	 */
	public Graphics render(Graphics g){
		if (visible){
			for(int i=0; i<content.size();i++){
				g=content.get(i).render(g);
			}
		}
		return g;
	}
	
	/**
	 * Collision n' junk
	 * 
	 * shouldn't end up calling self, save if you nest groups in groups
	 */
	public boolean collide(GameComponent other){
		for(int x=0; x<content.size();x++){
			if (content.get(x).collide(other)){
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		return content.toString();
	}
	
	public void setParent(Game g){
		super.setParent(g);
		for(int x=0; x<content.size();x++){
			content.get(x).setParent(g);
		}
	}
}
