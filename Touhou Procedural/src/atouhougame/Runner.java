package atouhougame;

import java.io.IOException;

import anetworkcode.Client;
import anetworkcode.ClientEvolutionManager;
import anetworkcode.Server;
import atouhougame.gamescreens.BossRushRouter;
import atouhougame.gamescreens.ExitGame;
import atouhougame.gamescreens.GalleryScreen;
import atouhougame.gamescreens.TextForwardScreen;
import framework.Game;
import framework.Global;
import framework.Menu;
import framework.TopFrame;

public class Runner  {
	
    public static void main(String[] args){
    	//TODO separate server and client stuff, also not so hardcoded localhost.
		Server threadServer= new Server();
		threadServer.start();
    	Game g = makeTheGame();
    	TopFrame t = new TopFrame(g,Global.width,Global.height);
    	g.start();
    }
    
    private static boolean serverExists(){
    	try {
			if(Client.serverExists()){
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
    }
    
	private static Game makeTheGame(){
		TGlobal.playNetworked = serverExists();
		Game galleryScreen;
		if(TGlobal.playNetworked){
			TGlobal.evolutionManager = new ClientEvolutionManager();
			//galleryScreen = new TextForwardScreen("Sorry.",new String[] {"The gallery function has not been implemented for networked games."});
		} else{
			TGlobal.evolutionManager = new LocalEvolutionManager();
			//galleryScreen = new GalleryScreen(TGlobal.evolutionManager);
		}
		galleryScreen = new GalleryScreen(TGlobal.evolutionManager);
		TGlobal.bossRushRouter=new BossRushRouter(TGlobal.evolutionManager);
		
		TGlobal.mainMenu = new Menu(
				new String[] {
						"Boss Rush",
						"Gallery",
						"About",
						"Exit"},
				new Game[] {
						TGlobal.bossRushRouter,
						galleryScreen,
						new TextForwardScreen(TGlobal.aboutScreenTitle,TGlobal.aboutScreenText),
						new ExitGame()},
				TGlobal.greyBack,
				TGlobal.fbig
			);
    	
		if(TGlobal.playNetworked){
			return TGlobal.mainMenu;
		}
		else{
			return new TextForwardScreen("Fuck.",new String[] {
					"Cannot connect to server.",
					"Playing with locally generated content instead."});
		}
	}
    
}