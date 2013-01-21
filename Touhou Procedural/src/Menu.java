

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import framework.Game;
import framework.Keys;
import framework.SwitchGameEvent;
import framework.Text;
import framework.TopFrame;

public class Menu extends Game{
	
	protected String[] items;
	protected Game[] results;
	protected Text[] text;
	
	public int currentSelection, defaultPosition = 30;
	public boolean changed = false;
	
	public Color baseColor, selectColor;
	
	public Menu(String[] items, Game[] sendTo, Color baseColor,Font font){
		super();
		
		currentSelection=0;
		text = new Text[items.length];
		
		for(int i=0; i<items.length; i++){
			this.text[i] = new Text(items[i],baseColor,font,defaultPosition,(i+1)*(font.getSize()+5),0,font.getSize());
			this.add(this.text[i]);
		}
		
		text[0].color = selectColor;
		
		this.bkgColor = new Color(baseColor.getRed()+25,
							baseColor.getGreen()+25,
							baseColor.getBlue()+25 );
		
		this.items = items;
		this.results = sendTo;
		this.baseColor = baseColor;
		this.selectColor = Color.white;
	}
	
	public void update(){
		if(Keys.isKeyPressed(KeyEvent.VK_UP)) {
			this.currentSelection = (currentSelection-1+items.length)%items.length;
			changed = true;
		}
		else if(Keys.isKeyPressed(KeyEvent.VK_DOWN)){
			this.currentSelection = (currentSelection+1)%items.length;
			changed = true;
		}
		
		if(changed){
			for (int i=0; i<text.length;i++){
				if(i==currentSelection){text[i].color = selectColor;}
				else{text[i].color = baseColor;}
			}
			changed = false;
		}
		
		super.update();
		
		if(Keys.isKeyPressed(KeyEvent.VK_ENTER)){
			System.out.println(this.getParent());
			SwitchGameEvent e = new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED,this.results[currentSelection],endGameDelay);
			this.getParent().dispatchEvent(e);
			/*this should work but something else
			is intercepting/interpreting the event from the sysevent queue or
			it's not being dispatched to the correct queue
			that being said, it is getting into the system event queue
			*/
			TopFrame t = (TopFrame)(this.getUltimateFrame());
			t.actionPerformed(e);
			//posts event to the system event queue
		}
	}
}
