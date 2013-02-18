package framework;

import java.awt.event.MouseEvent;

public class Mouse {
	
	public static Point position = new Point(0,0);
	public static int x,y;
	public static boolean left, right, middle, updated;
	
	
	
	public static void mouseMoved(MouseEvent e) {
		position.x = e.getX();
		position.y = e.getY();
		x=(int)position.x;
		y=(int)position.y;
		updated=true;
	}
	
	public static void mouseClicked(MouseEvent e) {
		switch (e.getButton()){
			case MouseEvent.BUTTON1:
				left=true;
			case MouseEvent.BUTTON2:
				middle=true;
			case MouseEvent.BUTTON3:
				right=true;
		}
		updated=true;
	}
	
	public static void mouseReleased(MouseEvent e) {
		switch (e.getButton()){
			case MouseEvent.BUTTON1:
				left=true;
			case MouseEvent.BUTTON2:
				middle=true;
			case MouseEvent.BUTTON3:
				right=true;
		}
		updated=true;
	}
	
}
