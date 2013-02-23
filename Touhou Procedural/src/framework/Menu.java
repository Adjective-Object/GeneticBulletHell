package framework;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


public class Menu extends Game{
	
	protected String[] items;
	protected Game[] results;
	protected Text[] text;
	
	public int currentSelection, defaultPosition = 30;
	public boolean changed = false;
	
	public Color baseColor, selectColor;
	
	public Menu(String[] items, Game[] sendTo, Color bkgColor, Font font){
		super();
		
		this.currentSelection=0;
		this.text = new Text[items.length];

		this.baseColor = new Color(255,255,255,180);
		this.selectColor = Color.white;
		
		for(int i=0; i<items.length; i++){
			this.text[i] = new Text(items[i],this.baseColor,font,this.defaultPosition,(i+1)*(font.getSize()+5));
			this.add(this.text[i]);
		}
		
		this.text[0].color = this.selectColor;
		
		this.bkgColor = bkgColor;
		
		this.items = items;
		this.results = sendTo;
	}
	
	@Override
	public void update(){
		if(Keys.isKeyPressed(KeyEvent.VK_UP)) {
			this.currentSelection = (this.currentSelection-1+this.items.length)%this.items.length;
			this.changed = true;
		}
		else if(Keys.isKeyPressed(KeyEvent.VK_DOWN)){
			this.currentSelection = (this.currentSelection+1)%this.items.length;
			this.changed = true;
		}
		
		
		if(Mouse.updated){
			for(int i=0; i<this.text.length; i++){
				if(text[i].containsPoint(Mouse.position)){
					if(this.currentSelection!=i){
						this.changed=true;
					}
					this.currentSelection = i;
				}
			}
		}
		
		
		if(this.changed){
			onMove();
			for (int i=0; i<this.text.length;i++){
				if(i==this.currentSelection){this.text[i].color = this.selectColor;}
				else{this.text[i].color = this.baseColor;}
			}
			this.changed = false;
		}
		
		if(Keys.isKeyPressed(KeyEvent.VK_ENTER) || Keys.isKeyPressed(KeyEvent.VK_Z) || Mouse.left){
			onSelect();
			switchGame(
					new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED,this.results[this.currentSelection],endGameDelay)
							);
		}

		super.update();
	}
	
	protected void onMove(){}
	
	protected void onSelect(){}
}
