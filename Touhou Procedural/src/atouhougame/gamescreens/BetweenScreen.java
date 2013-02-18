package atouhougame.gamescreens;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import atouhougame.Boss;
import atouhougame.BossSeed;
import atouhougame.TGlobal;
import framework.BakedGameComponent;
import framework.Game;
import framework.Global;
import framework.Keys;
import framework.ParagraphText;
import framework.SwitchGameEvent;
import framework.Text;

public class BetweenScreen extends Game{
	
	
	public BetweenScreen(BossSeed lastSeed, BossSeed nextSeed, double score){
		super();

		buildGUI(lastSeed, nextSeed, score);
	}
	
	public void buildGUI(BossSeed lastSeed, BossSeed nextSeed, double score){
		//#######################################################
		BufferedImage previousBoss = Boss.makeImage(lastSeed);
		this.add(new BakedGameComponent(200-previousBoss.getWidth()/2,200-previousBoss.getHeight()/2,previousBoss));
		Color c = lastSeed.color;
		this.bkgColor=new Color(c.getRed()+70, c.getGreen()+70, c.getBlue()+70);
		
		this.add(
			new ParagraphText(
				new String[]{
					lastSeed.getName(),
					"Times Tested:  "+lastSeed.timesTested,
					"Overall Score: "+(int)lastSeed.score,
					"Vs You:        "+(int)score
				},
				TGlobal.textTrans,TGlobal.fsmall,
				30,200,8
			));

		
		BufferedImage nextBoss = Boss.makeImage(nextSeed);
		this.add(new BakedGameComponent(700-nextBoss.getWidth()/2,200-nextBoss.getHeight()/2,nextBoss));
		
		this.add(
			new ParagraphText(
				new String[]{
					nextSeed.getName(),
					"Times Tested:  "+lastSeed.timesTested,
					"Overall Score: "+(int)nextSeed.score,
				},
				TGlobal.textTrans,TGlobal.fsmall,
				500,200,8
		));
		
		//#######################################################
		//put text on top of everything else.
		this.add(
				new Text(
						"Enter to continue, esc to return to main menu.",
						TGlobal.textTrans,TGlobal.fsmall,
						30,
						Global.height-35
				));
		
		this.add(
				new Text(
						"Previous",
						TGlobal.textTrans,TGlobal.fbig,
						30,
						TGlobal.fbig.getSize()+30
				));
		
		this.add(
				new Text(
						"Next",
						TGlobal.textTrans,TGlobal.fbig,
						500,
						TGlobal.fbig.getSize()+30
				));
	}
	
	@Override
	public void update(){
		super.update();
		Game targetGame = null;
		
		
		
		
		if(Keys.isKeyPressed(KeyEvent.VK_ENTER)){
			targetGame=TGlobal.bossRushRouter;
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
