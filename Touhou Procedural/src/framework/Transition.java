package framework;

import java.awt.image.BufferedImage;

public abstract class Transition extends Game{
	
	protected Game startGame, endGame;
	
	/**
	 * meant to transition between games
	 * assumes neither game is currently running, but it shouldn't really have any effect
	 * @param startGame: the game environment to start with
	 * @param endGame: the game environment to end with
	 */
	public Transition(Game startGame,Game endGame){
		super();
		this.startGame = startGame;
		this.endGame = endGame;
	}
	
	public void update(){
		super.update();
		advanceTransition(elapsedTime);
	}
	
	/**
	 * abstract method called to advance the scene transition
	 * @param elapsedTime: milliseconds since last update
	 * @return the boolean meant to be returned by update() (true/false to kill game or not)
	 */
	public abstract void advanceTransition(long elapsedTime);
	
	
}
