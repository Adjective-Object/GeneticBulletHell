package actualgame.gamescreens;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import actualgame.Boss;
import actualgame.BossSeed;
import actualgame.EvolutionManager;
import actualgame.TGlobal;
import framework.BakedGameComponent;
import framework.Game;
import framework.Global;
import framework.Keys;
import framework.ParagraphText;
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
		
		manager.scoreLastSeed(score);//score
		
		this.add(
			new ParagraphText(
				new String[]{
					manager.currentSeed().getName(),
					"Times Tested: "+manager.currentSeed().timesTested,
					"Overall Score: "+manager.currentSeed().score,
					"Vs You: "+score
				},
				TGlobal.textTrans,fsmall,
				30,200,8
			));

		
		

		//#######################################################
		manager.advanceSeed();//advance to next boss
		BossSeed nextSeed = manager.currentSeed();
		//adding content from of next boss
		BufferedImage nextBoss = new Boss(0,0,manager.currentSeed()).image();
		this.add(new BakedGameComponent(700-nextBoss.getWidth()/2,200-nextBoss.getHeight()/2,nextBoss));
		
		this.add(
			new ParagraphText(
				new String[]{
					manager.currentSeed().getName(),
					"Times Tested: "+manager.currentSeed().timesTested,
					"Overall Score: "+manager.currentSeed().score
				},
				TGlobal.textTrans,fsmall,
				500,200,8
		));
		
		//#######################################################
		//put text on top of everything else.
		this.add(
				new Text(
						"Enter to continue, esc to return to main menu.",
						TGlobal.textTrans,fsmall,
						30,
						Global.height-35
				));
		
		this.add(
				new Text(
						"Previous",
						TGlobal.textTrans,fbig,
						30,
						fbig.getSize()+30
				));
		
		this.add(
				new Text(
						"Next",
						TGlobal.textTrans,fbig,
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
			targetGame=TGlobal.mainMenu;
		}
		if(targetGame!=null){
			SwitchGameEvent e = new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED,targetGame,endGameDelay);
			switchGame(e);
		}
	}

}
