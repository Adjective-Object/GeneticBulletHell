package framework;

import java.awt.Graphics;
import java.awt.event.ActionEvent;

public class LeftWipe extends Transition{
	
	protected int xposition=0, moveSpeed;
	
	public LeftWipe(Game startGame, Game endGame, int moveSpeed) {
		super(startGame,endGame);
		this.moveSpeed = moveSpeed;
	}

	@Override
	public void advanceTransition(long elapsedTime) {
		this.xposition += elapsedTime/1000 * moveSpeed;
		if(xposition>=Global.width){this.getParent().dispatchEvent( new SwitchGameEvent(this,ActionEvent.ACTION_PERFORMED,endGame,endGameDelay) );}
	}
	
	public void renderToBuffer(){
		Graphics bufferGraphics = bufferImage.getGraphics();
		
		bufferGraphics.setColor(bkgColor);
		bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		startGame.renderToBuffer();
		endGame.renderToBuffer();
		
		bufferGraphics.drawImage(startGame.bufferImage,0,0,null);
		bufferGraphics.drawImage(endGame.bufferImage,xposition,0,null);
	}

}
