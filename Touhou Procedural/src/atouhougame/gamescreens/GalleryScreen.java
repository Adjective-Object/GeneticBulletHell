package atouhougame.gamescreens;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import atouhougame.Boss;
import atouhougame.BossSeed;
import atouhougame.Generation;
import atouhougame.LocalEvolutionManager;
import atouhougame.TGlobal;
import framework.BakedGameComponent;
import framework.Game;
import framework.GameComponent;
import framework.Global;
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
	
	int generationHeight = (boxsize+textheight)+margin;
	
	int xoff,yoff, startingGeneration=0, loadedGenerations = 0;
	
	boolean hasBosses=true;
	
	LocalEvolutionManager manager;
	
	Group<Text> lockX = new Group<Text>();
	Group<GameComponent> images = new Group<GameComponent>();
	Group<GameComponent> text = new Group<GameComponent>();
	
	public GalleryScreen(LocalEvolutionManager evomanager){
		super();
		this.manager = evomanager;
		this.bkgColor=TGlobal.greyBack;

		this.add(images);
		this.add(text);
		
		if(!TGlobal.playNetworked){
			ArrayList<Generation> generations = manager.loadGenerations();
			if (generations.size()<=0){
				makeDefaultScreen();
				hasBosses=false;
			}
		}
	}
	
	@Override
	public void onSwitch(){
		for (GameComponent g: this.images.content){
			g.x-=xoff;
			g.y-=yoff;
		}
		for (GameComponent g: this.text.content){
			g.x-=xoff;
			g.y-=yoff;
		}
		xoff=0;
		yoff=0;
	}
	
	private void makeRowFromGeneration(Generation gen){
		int effGenNumber = gen.generationNumber-startingGeneration;
		Text t = new Text(
				"Generation "+gen.generationNumber,
				TGlobal.textLight,TGlobal.fmed,
				margin-xoff,
				(effGenNumber)*generationHeight+TGlobal.fmed.getSize()-yoff+margin
			);
		lockX.add(t);
		text.add(t);
		for(int y=0; y< gen.size(); y++){
			BossSeed s = gen.get(y);
			BufferedImage d = Boss.makeImage(s);
			images.add(new BakedGameComponent(
					y*boxsize+margin+boxsize/2-d.getWidth()/2-xoff,
					effGenNumber*generationHeight+boxsize/2-d.getHeight()/2-yoff+margin,
					d)
			);
		}
		for(int y=0; y< gen.size(); y++){
			BossSeed s = gen.get(y);
			text.add(
				new Text(
					s.getName(),
					TGlobal.textLight,TGlobal.fsmall,
					y*boxsize+margin-xoff,
					(1+effGenNumber)*generationHeight-textheight-yoff+margin
				)
			);
			
			text.add(
				new ParagraphText(
					new String[]{
						"Times Tested: "+s.timesTested,
						"Overall Score: "+(int)s.score
					},
					TGlobal.textTrans,TGlobal.fsmall,
					y*boxsize+margin+20-xoff,
					(1+effGenNumber)*generationHeight-textheight+TGlobal.fsmall.getSize()+8-yoff+margin,
					8
				)
			);
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
		
		//calculating object move
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
		//tracking object move
		xoff+=offx;
		yoff+=offy;
		
		//implementing object move
		for (GameComponent g: this.images.content){
			g.x+=offx;
			g.y+=offy;
		}
		for (GameComponent g: this.text.content){
			g.x+=offx;
			g.y+=offy;
		}
		
		//locking X value on certain objects
		for(GameComponent g:this.lockX.content){
			g.x=margin;
		}
		
		//loading more rows
		if(loadedGenerations*generationHeight-offx<Global.height){
			if(manager.hasGeneration(loadedGenerations)){
				makeRowFromGeneration(manager.getGeneration(loadedGenerations));
				loadedGenerations++;
			}
		}

	}
	
}
