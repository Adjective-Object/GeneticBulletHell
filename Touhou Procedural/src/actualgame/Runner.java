package actualgame;

import actualgame.gamescreens.AboutScreen;
import actualgame.gamescreens.BossRushRouter;
import actualgame.gamescreens.ExitGame;
import actualgame.gamescreens.GalleryScreen;
import framework.Game;
import framework.Menu;
import framework.TopFrame;

public class Runner  {
    
    public static void main(String[] args){
    	Runner r = new Runner();
    	Game g = makeTheGame();
    	TopFrame t = new TopFrame(g,800,600);
    	g.start();
    }
    
	public static Game makeTheGame(){
		TGlobal.evolutionManager = new EvolutionManager();
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