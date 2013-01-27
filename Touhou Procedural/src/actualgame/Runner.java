package actualgame;

import java.awt.Color;
import java.awt.Font;

import actualgame.gamescreens.AboutScreen;
import actualgame.gamescreens.ExitGame;
import actualgame.gamescreens.TouhouGame;
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
		TouhouGame g = new TouhouGame(new EvolutionManager());
		
		Game m = new Menu(
				new String[] {
						"Boss Rush",
						"Gallery",
						"About",
						"Exit"},
				new Game[] {
						g,
						new AboutScreen(),
						new AboutScreen(),
						new ExitGame()},
				new Color(25,25,25),
				Font.decode("123123-bold-60")
			);

    	TouhouGlobal.mainMenu = m;
    	
		return m;
	}
    
}