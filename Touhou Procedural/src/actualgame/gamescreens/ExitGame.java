package actualgame.gamescreens;

import framework.Game;

public class ExitGame extends Game {
	
	public ExitGame(){}
	
	public void update(){
		this.getUltimateFrame().dispose();
		System.exit(0);
	}
}
