package framework;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class TopFrame extends JFrame implements ActionListener{
	
	public static Game game;
	
    public TopFrame(Game startGame, int width, int height) {

        setResizable(false);
        
    	this.game=startGame;
    	this.addKeyListener(new TAdapter());
    	this.getContentPane().add(game);
		this.pack();
        setTitle("Procedural Touhou");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setLocationRelativeTo(null);
        setVisible(true);

        Insets insets = this.getInsets();
        
        setSize(insets.left+insets.right+width, insets.top+insets.bottom+height);
        
        this.setVisible(true);
        this.setResizable(false);
    }
    
    private class TAdapter extends KeyAdapter {

        @Override
		public void keyReleased(KeyEvent e) {
            Keys.keyReleased(e);
        }

        @Override
		public void keyPressed(KeyEvent e) {
            Keys.keyPressed(e);
        }
    }

	@Override
	public void actionPerformed(ActionEvent evt) {
		
	}
    
	public void actionPerformed(SwitchGameEvent evt){
		this.game.stop();
		this.getContentPane().removeAll();
		
		this.game=evt.targetGame; //TODO fix the god damn evt. apparently DispatchEvent doesn't do what I think it does.
		
		this.getContentPane().add(game);
		this.getContentPane().doLayout();
		
		this.game.onSwitch();
		this.game.start();
	}
	
}