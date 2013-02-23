package atouhougame.gamescreens;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
import framework.Keys;
import framework.Mouse;
import framework.ParagraphText;
import framework.SwitchGameEvent;
import framework.Text;

public class FavoritesScreen extends Game{

	Generation fav;
	
	int yoff=0, maxoffy=0;
	static final int viewWidth=5;
	
	ArrayList<BakedGameComponent> bgc = new ArrayList<BakedGameComponent>(0);
	
	public FavoritesScreen() {
		this.bkgColor = TGlobal.greyBack;
		this.rebake();
	}
	
	public void rebake(){
		bgc.clear();
		File genFile = new File("favorites.gen");
		if(genFile.exists()){
			try {
				this.fav = LocalEvolutionManager.getGeneration(new File("favorites.gen"));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else{
			fav = new Generation(0);
			LocalEvolutionManager.archiveGeneration(fav, genFile);
		}
		
		if(fav.size()>0){
			for(int i=0; i<fav.size(); i++){
				makeTile(fav.get(i),i%viewWidth, i/viewWidth);
			}
			maxoffy=GalleryScreen.generationHeight*(fav.size()/viewWidth)-Global.height+GalleryScreen.margin*2;
			if(maxoffy<0){maxoffy=0;}
		} else{
			makeDefaultScreen();
		}
		yoff=0;
	}
	
	private void makeDefaultScreen(){
		this.add(new Text("No Favorites.", TGlobal.textLight ,TGlobal.fbig,GalleryScreen.margin, GalleryScreen.margin+TGlobal.fbig.getSize()-10));
		this.add(new ParagraphText(
				new String[]{
						"What's the matter, don't you like anything?",
						"Go Fight some bosses, and then favorite them here to play again later.",
						"You know you want to!",
				},
				TGlobal.textTrans,
				TGlobal.fsmall,
				GalleryScreen.margin+5,
				GalleryScreen.margin+8+TGlobal.fbig.getSize()+TGlobal.fsmall.getSize()-10
				,8));
		
	}
	
	private void makeTile(BossSeed b, int x, int y){
		
		BufferedImage d = Boss.makeImage(b);
		BakedGameComponent bgc = new BakedGameComponent(
				(x+0.5)*GalleryScreen.boxsize-d.getWidth()/2,
				(y+0.5)*GalleryScreen.boxsize-d.getHeight()/2,
				d);
		this.add(bgc);
		this.bgc.add(bgc);
		this.add(
			new Text(
				b.getName(),
				TGlobal.textLight,TGlobal.fsmall,
				GalleryScreen.margin+x*GalleryScreen.boxsize,
				GalleryScreen.margin+y*GalleryScreen.generationHeight+GalleryScreen.generationHeight-GalleryScreen.textheight
			)
		);
		
		this.add(
			new ParagraphText(
				new String[]{
					"Times Tested:  "+b.timesTested,
					"Overall Score: "+(int)b.score
				},
				TGlobal.textTrans,TGlobal.fsmall,
				GalleryScreen.margin+x*GalleryScreen.boxsize+20+x,
				GalleryScreen.margin+y*GalleryScreen.generationHeight+GalleryScreen.generationHeight-GalleryScreen.textheight+TGlobal.fsmall.getSize()+8,
				8
			)
		);
	}
	
	
	@Override
	public void onSwitch(){
		for (GameComponent g: this.content){
			g.y-=yoff;
		}
		yoff=0;
	}
	
	@Override
	public void update(){
		//camera handling
		int offy=0;
		double spd = GalleryScreen.speed*this.elapsedTime/1000;
		if(Keys.isKeyDown(KeyEvent.VK_UP)){
			offy+=spd;
		}
		if(Keys.isKeyDown(KeyEvent.VK_DOWN)){
			offy-=spd;
		}
		
		//tracking object move
		yoff-=offy;
		if(yoff<0){
			offy+=yoff;
			yoff=0;
		} else if(yoff>maxoffy){
			offy+=(yoff-maxoffy);
			yoff=maxoffy;
		}
		
		for (GameComponent g: this.content){
			g.y+=offy;
		}
		
		for(GameComponent gameComponent:bgc){
			if(gameComponent.containsPoint(Mouse.position)){
				//TODO
			}
		}
		
		//returning to the menu
		if (Keys.isKeyPressed(KeyEvent.VK_ESCAPE)){
			TGlobal.sound_menu_escape.play();
			Keys.clearpressedButtons();
			switchGame(new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED,TGlobal.mainMenu,endGameDelay));
		}
		
		
	}

}
