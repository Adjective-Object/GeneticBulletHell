package anetworkcode;

import atouhougame.TGlobal;
import atouhougame.gamescreens.AboutScreen;
import atouhougame.gamescreens.BossRushRouter;
import atouhougame.gamescreens.ExitGame;
import atouhougame.gamescreens.GalleryScreen;
import framework.Game;
import framework.Menu;
import framework.TopFrame;

public class NetworkRunner {
	public static void main(String[] args){
		Server threadServer= new Server();
		threadServer.start();
		Game g = makeTheGame();
		TopFrame t = new TopFrame(g,800,600);
	    g.start();
	    }
	
	public static Game makeTheGame(){
		TGlobal.evolutionManager = new ClientEvolutionManager();
		TGlobal.bossRushRouter=new BossRushRouter(TGlobal.evolutionManager);
		
		TGlobal.mainMenu = new Menu(
					new String[] {
						"Boss Rush",
						"Gallery",
						"About",
						"Exit"},
				new Game[] {
						TGlobal.bossRushRouter,
						new GalleryScreen(TGlobal.evolutionManager),
						new AboutScreen(),
						new ExitGame()},
				TGlobal.greyBack,
				TGlobal.fbig
				);
	    
		return TGlobal.mainMenu;
	}
	
}
