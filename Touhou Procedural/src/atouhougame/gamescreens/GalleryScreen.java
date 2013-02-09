package atouhougame.gamescreens;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import atouhougame.Boss;
import atouhougame.BossSeed;
import atouhougame.LocalEvolutionManager;
import atouhougame.TGlobal;
import framework.BakedGameComponent;
import framework.Game;
import framework.GameComponent;
import framework.Group;
import framework.Keys;
import framework.ParagraphText;
import framework.SwitchGameEvent;
import framework.Text;

public class GalleryScreen extends Game{
	
	static final int boxsize = 150;
	static final int textheight = 50;
	static final int margin = 10;
	static final int speed = 500;
	
	LocalEvolutionManager manager;
	
	Group<Text> lockX = new Group<Text>();
	
	public GalleryScreen(LocalEvolutionManager evomanager){
		super();
		this.manager = evomanager;
		this.bkgColor=TGlobal.greyBack;
	}
	
	@Override
	public void onSwitch(){
		this.content.clear();
		ArrayList<ArrayList<BossSeed>> generations = manager.loadGenerations();
		if(generations.size()>0){
			makeFromBosses(generations);
		}
		else{
			makeDefaultScreen();
		}
	}
	
	private void makeFromBosses(ArrayList<ArrayList<BossSeed>> generations){
		for(int i=0; i< generations.size(); i++){
			Text t = new Text(
					"Generation "+i,
					TGlobal.textLight,TGlobal.fmed,
					margin,
					(i)*(boxsize+textheight)+margin+TGlobal.fmed.getSize()
				);
			lockX.add(t);
			this.add(t);
			for(int y=0; y< generations.get(i).size(); y++){
				BossSeed s = generations.get(i).get(y);
				BufferedImage d = Boss.makeImage(s);
				this.add(new BakedGameComponent(
						y*boxsize+margin+boxsize/2-d.getWidth()/2,
						i*(boxsize+textheight+margin)+boxsize/2-d.getHeight()/2,
						d)
				);
				this.add(
					new Text(
						s.getName(),
						TGlobal.textLight,TGlobal.fsmall,
						y*boxsize+margin,
						(1+i)*(boxsize+textheight)-textheight+margin
					)
				);
				
				this.add(
					new ParagraphText(
						new String[]{
							"Times Tested: "+s.timesTested,
							"Overall Score: "+(int)s.score
						},
						TGlobal.textTrans,TGlobal.fsmall,
						y*boxsize+margin+20,
						(1+i)*(boxsize+textheight)-textheight+margin+TGlobal.fsmall.getSize()+8,
						8
					)
				);
			}
		}
	}
	
	private void makeDefaultScreen(){
		
		this.add(new Text("No Bosses Generated.", TGlobal.textLight ,TGlobal.fbig,margin, margin+TGlobal.fbig.getSize()));
		this.add(new Text("Play some Boss Rush mode to understand what the fuck is going on.",
				TGlobal.textTrans,TGlobal.fsmall,margin, margin+8+TGlobal.fbig.getSize()+TGlobal.fsmall.getSize()));
	}
	
	@Override
	public void update(){
		super.update();
		
		if(Keys.isKeyPressed(KeyEvent.VK_ESCAPE)){
			switchGame(
					new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED,TGlobal.mainMenu,endGameDelay)
					);
		}
		
		int offx=0, offy=0;
		double spd = speed*this.elapsedTime/1000;
		
		if(Keys.isKeyDown(KeyEvent.VK_LEFT)){
			offx+=spd;
		}
		if(Keys.isKeyDown(KeyEvent.VK_RIGHT)){
			offx-=spd;
		}
		if(Keys.isKeyDown(KeyEvent.VK_UP)){
			offy+=spd;
		}
		if(Keys.isKeyDown(KeyEvent.VK_DOWN)){
			offy-=spd;
		}
		
		for (GameComponent g: this.content.content){
			g.x+=offx;
			g.y+=offy;
		}
		
		for(GameComponent g:this.lockX.content){
			g.x=margin;
		}

	}
	
}
