package framework;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Keys{
	
	protected static ArrayList<Integer> downKeys = new ArrayList<Integer>(0);//keys just pressed
	protected static ArrayList<Integer> heldKeys = new ArrayList<Integer>(0);//keys held
	
	/**
	 * handles key presses
	 * mainly just for recording what keys are being held down / pressed.
	 **/
	public static void keyPressed(KeyEvent e) {
		downKeys.add( e.getKeyCode() );
		heldKeys.add( e.getKeyCode() );
	}
	
	/**
	 * handles key releases
	 * mainly just for recording what keys are being held down / pressed.
	 **/
	public static void keyReleased(KeyEvent e) {
		while (heldKeys.contains(e.getKeyCode())){
			heldKeys.remove(new Integer(e.getKeyCode()));
		}
	}
	
	/**
	 * checks for a certain key being held
	 * @param keycode : the key, taken form KeyEvent.whatever
	 */
	public static boolean isKeyDown(int keycode){
		return heldKeys.contains(keycode);
	}
	
	/**
	 * checks if a key was just pressed
	 * due to limitations in how it's designed
	 * only the first reference to it being pressed is counted
	 * In other words, only one thing can be checking for each keypress
	 * @param keycode : the key code in KeyEvent.VK_Whatever
	 * @return if the button was just pressed
	 */
	public static boolean isKeyPressed(int keycode){
		if(downKeys.contains(keycode)){
			downKeys.remove((Integer)(keycode));
			if(heldKeys.contains(keycode)){
				return true;
			}
		}
		return false;
	}

	public static boolean anyKeyPressed() {
		return(downKeys.size()>0);
	}

	public static void clearpressedButtons() {
		downKeys.clear();
	}
	
}
