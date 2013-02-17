package atouhougame.bullets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import atouhougame.TGlobal;
import framework.GameComponent;
import framework.Global;
import framework.Point;

public class Laser extends Bullet{
	
	static int waittime = 500, firetime = 500;
	
	double slope;
	int laserWidth;
	int elapsed;
	double originalPower;
	Point destination;
	boolean playedSound=false;
	
	public Laser(double x, double y, double angle, double width, double power, Color color) {
		super(x, y, width, power/100, color);
		this.destination=Global.rotate(0, Global.width, Math.PI/2-angle%(Math.PI));//TODO not this.
		int red =  color.getRed()+(255-color.getRed())/2;
		int green =  color.getGreen()+(255-color.getGreen())/2;
		int blue =  color.getBlue()+(255-color.getBlue())/2;
		this.color = new Color(red,green,blue);
		slope=destination.y/destination.x;
		this.destination.x+=x;
		this.destination.y+=y;
		this.originalPower=power;
		this.laserWidth=1;
	}
	
	@Override
	public void update(long elapsedMillis){
		if(elapsed==0){
			TGlobal.sound_Laser_warn.play();
		}
		this.elapsed+=elapsedMillis;
		if(this.elapsed>waittime && !playedSound){
			playedSound=true;
			TGlobal.sound_Laser_fire.play();
		}
		
		if(this.elapsed>waittime && this.elapsed<waittime+firetime && this.laserWidth<=this.bulletSize){
			this.laserWidth++;
		} else if(this.elapsed>waittime){
			this.laserWidth--;
			if(this.laserWidth<=0){
				this.kill();
			}
		}
		
		this.power = this.originalPower*100*elapsedMillis/1000;
	}
	
	@Override
	public boolean collide(GameComponent obj){
		if(this.elapsed<this.waittime || this.elapsed>this.waittime+this.firetime){
			return false;
		}
		for(int x=0; x<obj.size.x; x++){
			if(obj.pointWithin(new Point(obj.x + x, this.y+(obj.x-this.x+x)*this.slope )) ){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean killOnCollide(){
		return false;
	}
	
	/**
	 * draws the laser onto the given buffered image at the images X, Y
	 * @param bi: the target bufferedImage
	 * @return : returns the altered BufferedImage
	 */
	@Override
	public BufferedImage render(BufferedImage bi){
		Graphics2D g = (Graphics2D) bi.getGraphics();
		g.setColor(this.color);
		g.setStroke(new BasicStroke(laserWidth));
		g.fillOval((int)x-laserWidth, (int)y-laserWidth,laserWidth*2, laserWidth*2);
		g.drawLine((int)x, (int)y, (int)destination.x, (int)destination.y);
		g.setStroke(new BasicStroke(1));
		return bi;
	}

}
