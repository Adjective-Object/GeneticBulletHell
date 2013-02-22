package framework;

import java.awt.event.MouseEvent;

public class Mouse {
	
	public static Point position = new Point(0,0);
	public static int x,y;
	public static boolean left, right, middle, updated;
	public static boolean leftPressed, rightPressed, middlePressed;
	
	
	
	public static void mouseMoved(MouseEvent e) {
		position.x = e.getX();
		position.y = e.getY();
		x=(int)position.x;
		y=(int)position.y;
	}
	
	public static void mouseClicked(MouseEvent e) {
		switch (e.getButton()){
			case MouseEvent.BUTTON1:
				left=true;
				leftPressed=true;
			case MouseEvent.BUTTON2:
				middle=true;
				middlePressed=true;
			case MouseEvent.BUTTON3:
				right=true;
				rightPressed=true;
		}
	}
	
	public static void mouseReleased(MouseEvent e) {
		switch (e.getButton()){
			case MouseEvent.BUTTON1:
				rightPressed=false;
			case MouseEvent.BUTTON2:
				rightPressed=false;
			case MouseEvent.BUTTON3:
				rightPressed=false;
		}
	}

	public static boolean anyButtonPressed() {
		return left || right || middle;
	}
	
	public static void resetMouse(){
		leftPressed=false;
		rightPressed=false;
		middlePressed=false;
	}
	
}
