package framework;

import java.awt.AWTEvent;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class TopFrame extends JFrame implements ActionListener, AWTEventListener{
	
	public static Game game;
	
    public TopFrame(Game startGame, int width, int height) {

        setResizable(false);
        
    	this.game=startGame;
    	this.getContentPane().add(game);
    	this.addKeyListener(new KAdapter());
    	Toolkit.getDefaultToolkit().addAWTEventListener(this,AWTEvent.MOUSE_EVENT_MASK+AWTEvent.MOUSE_MOTION_EVENT_MASK);
        this.pack();
        setTitle("Procedural Touhou");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Insets insets = this.getInsets();
        
        setSize(insets.left+insets.right+width, insets.top+insets.bottom+height);
		
        setLocationRelativeTo(null);

        
        this.setVisible(true);
    }
    
    private class KAdapter extends KeyAdapter {

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
	public void eventDispatched(AWTEvent e) {
		MouseEvent me = (MouseEvent)e;
		switch(e.getID()){
			case MouseEvent.MOUSE_PRESSED:
				Mouse.mouseClicked(me);
				break;
			case MouseEvent.MOUSE_RELEASED:
				Mouse.mouseReleased(me);
				break;
			case MouseEvent.MOUSE_MOVED:
				Mouse.mouseMoved(me);
				break;
		}
	}
    
	@Override
	public void actionPerformed(ActionEvent evt) {
		System.out.println("TopFrame got ActionEvent: "+evt);
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