package atouhougame.gamescreens;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import atouhougame.LocalEvolutionManager;
import atouhougame.TGlobal;
import framework.Game;
import framework.GameComponent;
import framework.Global;
import framework.Group;
import framework.Keys;
import framework.SwitchGameEvent;
import framework.Text;

public class GalleryScreen extends Game{
	
	static final int boxsize = 150;
	static final int textheight = 50;
	static final int margin = 10;
	static final int speed = 500;
	
	int maxoffx = 0, maxoffy=0;
	
	boolean doneCheck=false;
	
	int generationHeight = (boxsize+textheight)+margin;
	
	int xoff,yoff, startingGeneration=0, loadedGenerations = 0;
	
	boolean hasBosses=true;
	
	LocalEvolutionManager manager;
	
	Group<Text> lockX = new Group<Text>();
	Group<GameComponent> images = new Group<GameComponent>(true);
	Group<GameComponent> text = new Group<GameComponent>(true);
	
	public GalleryScreen(LocalEvolutionManager evomanager){
		super();
		this.manager = evomanager;
		this.bkgColor=TGlobal.greyBack;

		this.add(images);
		this.add(text);
	}
	
	@Override
	public void onSwitch(){
		this.lockX.clear();
		this.images.clear();
		this.text.clear();
		loadedGenerations=0;
		doneCheck=false;
		xoff=0;
		yoff=0;
		manager.refreshCache();
		
	}
	
	private void makeDefaultScreen(){
		if(TGlobal.playNetworked){
			this.text.add(new Text("No Bosses On Server.", TGlobal.textLight ,TGlobal.fbig,margin, margin+TGlobal.fbig.getSize()));
			this.text.add(new Text("Don't know what just happened. Try playing some Boss Rush mode?",
					TGlobal.textTrans,TGlobal.fsmall,margin, margin+8+TGlobal.fbig.getSize()+TGlobal.fsmall.getSize()));
		}
		else{
			this.text.add(new Text("No Bosses Generated.", TGlobal.textLight ,TGlobal.fbig,margin, margin+TGlobal.fbig.getSize()));
			this.text.add(new Text("Play some Boss Rush mode to understand what the fuck is going on.",
					TGlobal.textTrans,TGlobal.fsmall,margin, margin+8+TGlobal.fbig.getSize()+TGlobal.fsmall.getSize()));
		}
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
		xoff-=offx;
		yoff-=offy;
		
		
		if(xoff<0){
			offx+=xoff;
			xoff=0;
		}
		if(yoff<0){
			offy+=yoff;
			yoff=0;
		}
		
		if(xoff>maxoffx){
			offx+=(xoff-maxoffx);
			xoff=maxoffx;
		}
		if(yoff>maxoffy){
			offy+=(yoff-maxoffy);
			yoff=maxoffy;
		}
		
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
		if(!doneCheck){
			if(loadedGenerations*generationHeight-yoff<Global.height){
				if(manager.hasGeneration(loadedGenerations)){
					this.images.add(new ThreadedGenerationLoader(
							(double)margin-xoff,
							(double)margin+generationHeight*loadedGenerations-yoff,
							this,
							loadedGenerations));
					loadedGenerations++;
					maxoffy=generationHeight*loadedGenerations-Global.height+margin*2;
					if(maxoffy<0){maxoffy=0;}
				} else{
					doneCheck=true;
					if (loadedGenerations==0){
						makeDefaultScreen();
					}
				}
			}
		}
	}
	
}
