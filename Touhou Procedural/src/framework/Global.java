package framework;

import java.awt.Color;
import java.util.Random;

public class Global {
	public static int width=794;
	public static int height=572;
	
	public static Point rotate (double x, double y,double angle){
		return new Point(x*Math.cos(angle)-y*Math.sin(angle) , x*Math.sin(angle)+y*Math.cos(angle));
	}
	
	//this is derptacular and doesn't really work, but it's ok for now
	//it's meant to find the angle in radians from the origin (x,y) to the destination(destx,desty)
	public static double findAngle(double x, double y, double destx, double desty){
		double anglepart= Math.atan( Math.abs(x-destx)/Math.abs(y-desty) );
		if(y<=desty){
			if(x<=destx){}
			else{anglepart=anglepart+1.5*Math.PI;}
		}
		else{
			if(x<=destx){anglepart=anglepart+0.5*Math.PI;}
			else{anglepart=anglepart+Math.PI;}
		}
		return anglepart;
	}
	
	public static Group createSimpleExplosion(GameComponent b){
		int particleSize = 5;
		int numParts = Math.round((float)(b.getWidth()*b.getHeight())/(particleSize*particleSize));
		if (numParts<5){
			numParts=5;
		}
		return createSimpleExplosion(particleSize,numParts,b.color,new Point(b.getCenter().x,b.getCenter().y),b.velocity,0,1000,(b.getWidth()+b.getHeight())/4,false);
	}
	
	public static Group createSimpleExplosion(int particleSize, int numParticles, Color color, Point location, Point initialVelocity, int particleTime, int fadeTime, int blastRadius, boolean fadeout){
		Random r = new Random();
		Group toRet = new Group(false);
		for(int i=0; i<numParticles;i++){
			SimpleFadeoutComponent comp = new SimpleFadeoutComponent((int)location.x, (int)location.y,particleSize,particleSize, color, particleTime,fadeTime,GameComponent.BOUNDARY_KILL_ON_CROSS,fadeout);
			comp.velocity=Global.rotate((initialVelocity.x + initialVelocity.y)*r.nextDouble(),0, i*(2*Math.PI/numParticles)+r.nextDouble()-r.nextDouble());
			comp.velocity.x+=initialVelocity.x/2+ initialVelocity.x/2*r.nextDouble() + r.nextDouble()*blastRadius*2-r.nextDouble()*blastRadius*2;
			comp.velocity.y+=initialVelocity.y/2+ initialVelocity.y/2*r.nextDouble() + r.nextDouble()*blastRadius-r.nextDouble()*blastRadius*2;
			toRet.add(comp);
		}	
		return toRet;
	}
}
