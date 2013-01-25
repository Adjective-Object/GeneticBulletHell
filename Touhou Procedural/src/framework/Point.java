package framework;

public class Point{
	public double x,y;
	
	public Point(double x, double y){
		this.x=x;
		this.y=y;
	}
	
	
	public String toString(){
		return("Point("+x+","+y+")");
	}


	public Point scale(double factor) {
		Point p = new Point(
				this.x*factor,
				this.y*factor);
		return p;
	}
}	
