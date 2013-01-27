package actualgame.gamescreens;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import actualgame.Boss;
import actualgame.BossSeed;
import actualgame.EvolutionManager;
import actualgame.TouhouGlobal;
import framework.BakedGameComponent;
import framework.Game;
import framework.Global;
import framework.Keys;
import framework.SwitchGameEvent;
import framework.Text;

public class BetweenScreen extends Game{
	
	Game next;
	
	public BetweenScreen(EvolutionManager manager, double score){
		super();
		
		//declare fonts
		Font fsmall = Font.decode("123123-bold-12");
		Font fbig = Font.decode("123123-bold-60");
		

		//#######################################################
		BufferedImage previousBoss = new Boss(0,0,manager.currentSeed()).image();
		this.add(new BakedGameComponent(200-previousBoss.getWidth()/2,200-previousBoss.getHeight()/2,previousBoss));
		Color c = manager.currentSeed().color;
		this.bkgColor=new Color(c.getRed()+70, c.getGreen()+70, c.getBlue()+70);
		
		this.add(
				new Text(
						manager.currentSeed().getName(),
						new Color(255,255,255,180),fsmall,
						30,200
				));
		
		manager.scoreLastSeed(score);//score
		
		this.add(
				new Text(
						"Times Tested: "+manager.currentSeed().timesTested,
						new Color(255,255,255,180),fsmall,
						30,200+fsmall.getSize()+8
				));
		this.add(
				new Text(
						"Overall Score: "+manager.currentSeed().score,
						new Color(255,255,255,180),fsmall,
						30,200+(fsmall.getSize()+8)*2
				));

		this.add(
				new Text(
						"Vs You: "+score,
						new Color(255,255,255,180),fsmall,
						30,200+(fsmall.getSize()+8)*3
				));
		
		

		//#######################################################
		manager.advanceSeed();//advance to next boss
		BossSeed nextSeed = manager.currentSeed();
		//adding content from of next boss
		BufferedImage nextBoss = new Boss(0,0,manager.currentSeed()).image();
		this.add(new BakedGameComponent(700-nextBoss.getWidth()/2,200-nextBoss.getHeight()/2,nextBoss));
		
		this.add(
				new Text(
						manager.currentSeed().getName(),
						new Color(255,255,255,180),fsmall,
						500,200
				));
		
		this.add(
				new Text(
						"Times Tested: "+manager.currentSeed().timesTested,
						new Color(255,255,255,180),fsmall,
						500,200+fsmall.getSize()+8
				));
		this.add(
				new Text(
						"Overall Score: "+manager.currentSeed().score,
						new Color(255,255,255,180),fsmall,
						500,200+(fsmall.getSize()+8)*2
				));
		
		//#######################################################
		//put text on top of everything else.
		this.add(
				new Text(
						"Enter to continue, esc to return to main menu.",
						new Color(255,255,255,180),fsmall,
						30,
						Global.height-35
				));
		
		this.add(
				new Text(
						"Previous",
						new Color(255,255,255,180),fbig,
						30,
						fbig.getSize()+30
				));
		
		this.add(
				new Text(
						"Next",
						new Color(255,255,255,180),fbig,
						500,
						fbig.getSize()+30
				));
		
		
		this.next=new TouhouGame(manager);
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
			switchGame(e);
		}
	}

}
