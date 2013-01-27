package actualgame.gamescreens;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import actualgame.EvolutionManager;
import actualgame.TouhouGlobal;
import framework.Game;
import framework.Keys;
import framework.SwitchGameEvent;
import framework.Text;
import framework.TopFrame;

public class BetweenScreen extends Game{
	
	Game next;
	
	public BetweenScreen(EvolutionManager manager){
		super();
		
		this.next=new TouhouGame(manager);
		
		
		
		
		Font f = Font.decode("123123-bold-12");
		this.add(
				new Text(
						"Enter to continue, esc to return to main menu.",
						new Color(255,255,255,180),f,
						30,
						f.getSize()+5
				));
	}
	
	@Override
	public void update(){
		super.update();
		Game targetGame = null;
		if(Keys.isKeyPressed(KeyEvent.VK_ENTER)){
			targetGame=next;
		}
		else if(Keys.isKeyPressed(KeyEvent.VK_ESCAPE)){
			targetGame=TouhouGlobal.mainMenu;
		}
		if(targetGame!=null){
			SwitchGameEvent e = new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED,targetGame,endGameDelay);
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
