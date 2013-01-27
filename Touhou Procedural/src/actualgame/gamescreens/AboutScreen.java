package actualgame.gamescreens;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import actualgame.TouhouGlobal;
import framework.Game;
import framework.Keys;
import framework.ParagraphText;
import framework.SwitchGameEvent;
import framework.Text;

public class AboutScreen extends Game{
	
	public AboutScreen(){
		super();
		
		this.bkgColor = new Color(25,25,25);

		
		Font fbig = Font.decode("123123-bold-60");
		Font fsmall = Font.decode("123123-bold-12");
		int height = fbig.getSize()+5;
		this.add(
				new Text(
						"This is an Experiment.",
						new Color(255,255,255,180),fbig,
						30,
						height
				));
		height+=fsmall.getSize()+15;
		this.add(
				new ParagraphText(
						new String[] {
							"This is a different take on the Bullet Hell genre.",
							"When you play this game, you won't be following the paths of any spaceships or magical girls.",
							"There are no levels, no monsters, no ______.",
							"",
							"Just bosses.",
							"",
							"...And bosses...",
							"",
							"...And bosses.",
							"","",
							"This is an experiment in evolving boss patterns. As you play, the bosses will adapt.",
							"Those that survive the best will reproduce, until optimal solutions are reached.",
							"",
							"Or at least that's the hope.",
							"",
							"",
							"Z to shoot, X to bomb, Shift to move slowly, Space to move quickly.",
							"",
							"",
							"(Press any button to return to the main menu)"
						},
						Color.white,fsmall,
						45,
						height,
						8
				));
	}
	
	@Override
	public void update(){
		super.update();
		if (Keys.anyKeyPressed()){
			Keys.clearpressedButtons();
			switchGame(new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED,TouhouGlobal.mainMenu,endGameDelay));
		}
	}
	
	
	

}
