package actualgame.gamescreens;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import actualgame.TouhouGlobal;
import framework.Game;
import framework.Keys;
import framework.SwitchGameEvent;

public class GalleryScreen extends Game{

	
	public void loadGenerations(){
		this.content.clear();
	}
	
	@Override
	public void update(){
		super.update();
		
		if(Keys.isKeyPressed(KeyEvent.VK_ESCAPE)){
			switchGame(
					new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED,TouhouGlobal.mainMenu,endGameDelay)
					);
		}
	}
	
}
