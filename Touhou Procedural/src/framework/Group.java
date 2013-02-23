package framework;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Group<T extends GameComponent> extends GameComponent implements Iterable<T> {
	
	public boolean removeDead = false;
	public ArrayList<T> content;
	
	public Group(ArrayList<T> content){
		this();
		this.content.addAll(content);
	}
	
	public Group(T content){
		this();
		this.content.add(content);
	}
	
	public Group(){
		super(0,0);
		this.content = new ArrayList<T>(0);
	}
	
	public Group(boolean removeDead){
		this();
		this.removeDead = true;
	}
	
	public void add(T comp){
		this.content.add(comp);
		comp.setParent(this.parentGame);
		
	}
	
	public void addAll(Group<? extends T> Component){
		for(int i=0; i<Component.content.size(); i++){
			Component.content.get(i).setParent(this.parentGame);
		}
		content.addAll(Component.content);
	}
	
	public int size(){
		return this.content.size();
	}
	
	@Override
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
	@Override
	public BufferedImage render(BufferedImage bi){
		this.render(bi.getGraphics());
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
			for(int i=0; i<content.size();i++){
				if(content.get(i).visible){
					content.get(i).render(g);
					if(content.get(i).hilight){
						content.get(i).renderHilight(g);
					}
				}
			}
		}
		return g;
	}
	
	/**
	 * Collision n' junk
	 * 
	 * shouldn't end up calling self, save if you nest groups in groups
	 */
	@Override
	public boolean collide(GameComponent other){
		for(int x=0; x<content.size();x++){
			if (content.get(x).collide(other)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		return content.toString();
	}
	
	@Override
	public void setParent(Game g){
		super.setParent(g);
		for(int x=0; x<content.size();x++){
			content.get(x).setParent(g);
		}
	}

	public void clear() {
		this.content.clear();
	}
	
	@Override
	public Group<T> clone(){
		Group<T> g = new Group<T>();
		g.addAll(this);
		return g;
	}
	
	public void remove(T target){
		for(T i:this.content){
			if(target==i){
				this.content.remove(i);
			}
		}
	}

	@Override
	public Iterator<T> iterator() {
		return this.content.iterator();
	}
}
