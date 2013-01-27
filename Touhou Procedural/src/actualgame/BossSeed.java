package actualgame;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import actualgame.gamescreens.TouhouGame;
import actualgame.patterncommands.AttackPattern;
import actualgame.patterncommands.BulletSeed;
import actualgame.patterncommands.Command;
import actualgame.patterncommands.FireCommand;
import actualgame.patterncommands.MoveCommand;


public class BossSeed implements Serializable{
	public static int nutrients = 500;
	public Color color;
	protected ArrayList<AttackPattern> patterns = new ArrayList<AttackPattern> (0);
	
	//stat priorities
	double STR,CON,WIS,INT,DEX,LUK;
	
	//stat priorities
	
	public int timesTested=0;
	public double score;
	
	public BossSeed(){
		
		this.STR=Math.abs( 100 );
		this.CON=Math.abs( 100 );
		this.WIS=Math.abs( 100 );
		this.INT=Math.abs( 100 );
		this.DEX=Math.abs( 100 );
		
		
		this.color=new Color(
				4,
				4,
				4
				);
		
		for(int i=0; i<3 ; i++){
			ArrayList<Command> c = new ArrayList<Command> (0);
			for(int v=0; v<20 ; v++){
				c.add(randomCommand(100));
			}
			patterns.add(new AttackPattern(c));
		}
	}
	
	public BossSeed(long seed){
		Random r = new Random(seed);
		
		double str = r.nextDouble(),
				con = r.nextDouble(),
				wis = r.nextDouble(),
				inte = r.nextDouble(),
				dex = r.nextDouble(),
				luk = r.nextDouble(),
				sum = str+con+wis+inte+dex+luk;
		
		this.STR= str/sum*nutrients ;
		this.CON= con/sum*nutrients ;
		this.WIS= wis/sum*nutrients ;
		this.INT= inte/sum*nutrients ;
		this.DEX= dex/sum*nutrients ;
		this.LUK= luk/sum*nutrients ;
		
		
		this.color=new Color(
				(int)((str+con)/(2*sum)*255),
				(int)((dex+luk)/(2*sum)*255),
				(int)((inte+wis)/(2*sum)*255)
				);
		
		for(int i=0; i<3 ; i++){
			ArrayList<Command> c = new ArrayList<Command> (0);
			for(int v=0; v<20 ; v++){
				c.add(randomCommand(100));
			}
			patterns.add(new AttackPattern(c));
		}
	}
	
	public BossSeed(double str, double con, double wis, double inte, double dex, double luk, ArrayList<AttackPattern> patterns){
		double sum = str+con+wis+inte+dex+luk;
		
		this.STR= str/sum*nutrients ;
		this.CON= con/sum*nutrients ;
		this.WIS= wis/sum*nutrients ;
		this.INT= inte/sum*nutrients ;
		this.DEX= dex/sum*nutrients ;
		this.LUK= luk/sum*nutrients ;
		
		this.color=new Color(
				(int)((str+con)/(2*sum)*255),
				(int)((dex+luk)/(2*sum)*255),
				(int)((inte+wis)/(2*sum)*255)
				);
		
		this.patterns = patterns;
	}
	
	public static Color getTColor(){
	    Random  r = new Random();
	    return new Color(
	    		(int)(5+r.nextDouble()*175),
	    		(int)(5+r.nextDouble()*175),
	    		(int)(5+r.nextDouble()*175)
	    		);
	}
	
	public Command randomCommand(int volleySize){
		Random r = new Random();
		double d = r.nextDouble();
		if(d<0.5){
			return new MoveCommand((int)(TouhouGame.playFieldLeft.x+r.nextDouble()*(TouhouGame.playFieldRight.x-TouhouGame.playFieldLeft.x)),
					(int)(TouhouGame.playFieldLeft.y+r.nextDouble()*(TouhouGame.playFieldRight.y-TouhouGame.playFieldLeft.y)),
					r.nextDouble());
		}
		else{
			return new FireCommand(getRandBulletArr(volleySize));
		}
	}
	
	public static ArrayList<BulletSeed> getRandBulletArr(int numBullets){
		ArrayList<BulletSeed> seed = new ArrayList<BulletSeed>(0);
		for(int d=0; d<numBullets;d++){
			seed.add(new BulletSeed());
		}
		return seed;
	}
	
	public BossSeed breedWith(BossSeed seed){
		/*
		ArrayList<AttackPattern> pattern = new ArrayList<AttackPattern>(0);
		Random r = new Random();
		for (int i=0; i< this.patterns.size(); i++){
			if(r.nextBoolean()){
				pattern.append(this.pattrens.get(i).){
					
				}
			}
		}
		*/
		return this;	
	}
	
	static final String[] humanNames= new String[]{
		"Melissa",
		"Harold",
		"Archer",
		"Michael",
	};
	
	public String getName(){
		String name="";
		char[] chars = new char[]{
				(char)((int)(this.STR)),
				(char)((int)(this.CON)),
				(char)((int)(this.INT)),
				(char)((int)(this.LUK)),
				(char)((int)(this.WIS)),
				(char)((int)(this.DEX)),
		};
		for (char c:chars){
			name+=c;
		}
		name+="-";
		name+=humanNames[(int)(this.CON+this.LUK+this.DEX)%humanNames.length];
		return name;
		
	}
}
