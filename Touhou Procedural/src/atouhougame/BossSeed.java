package atouhougame;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import atouhougame.bullets.BulletSeed;
import atouhougame.gamescreens.TouhouGame;
import atouhougame.patterncommands.AttackPattern;
import atouhougame.patterncommands.Command;
import atouhougame.patterncommands.FireCommand;
import atouhougame.patterncommands.FireLaserCommand;
import atouhougame.patterncommands.MoveCommand;
import atouhougame.patterncommands.SleepCommand;


public class BossSeed implements Serializable{
	public static int nutrients = 500;
	public Color color;
	protected ArrayList<AttackPattern> patterns = new ArrayList<AttackPattern> (0);
	
	//stat priorities
	double STR,CON,WIS,INT,DEX,LUK;
	
	//stat priorities
	
	public int timesTested=0;
	public long bossID=UUID.randomUUID().getMostSignificantBits();
	//because chance of collision is low and I don't want to send UUID over network
	//may fix if it becomes more than a minor issue in the future.
	//but it shouldn't.
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
	
	public void setID(int ID){
		this.bossID=ID;
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
		if(d<0.3){
			return new SleepCommand();
		}
		else if(d<0.5){
			return new MoveCommand(
					(int)(TouhouGame.playFieldLeft.x+r.nextDouble()*(TouhouGame.playFieldRight.x-TouhouGame.playFieldLeft.x)),
					(int)(TouhouGame.playFieldLeft.y+r.nextDouble()*(TouhouGame.playFieldRight.y-TouhouGame.playFieldLeft.y)),
					r.nextDouble());
		}
		else if (d<0.8){
			ArrayList<BulletSeed> seeds = new ArrayList<BulletSeed>(0);
			seeds.add(new BulletSeed());
			return new FireLaserCommand(seeds);
		}
		else if (d<0.9){
			return new FireCommand(getRadialBulletArr(volleySize/10));
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
	public static ArrayList<BulletSeed> getRadialBulletArr(int numBullets){
		ArrayList<BulletSeed> seed = new ArrayList<BulletSeed>(0);
		BulletSeed b = new BulletSeed();
		for(int d=0; d<numBullets;d++){
			seed.add( b.rotatedCopy(2*Math.PI*d/numBullets) );
		}
		return seed;
	}
	
	@Override
	public String toString(){
		return "<Boss id="+this.bossID+" >";
	}
	
	public double mutationRate = 0.1;
	
	public BossSeed breedWith(BossSeed seed){//TODO mixing and mutating actual bullets/commands
		Random r = new Random(System.currentTimeMillis());
		
		//generate new set of attack patterns
		ArrayList<AttackPattern> patterns = new ArrayList<AttackPattern>(3);
		for(int pat=0; pat<3; pat++){
			ArrayList<Command> commandList = new ArrayList<Command>(0);
			int numCommands = this.patterns.get(pat).commands.size();
			if(seed.patterns.get(pat).commands.size()>numCommands){
				numCommands=seed.patterns.get(pat).commands.size();
			}
			
			//generating new attack pattern
			ArrayList<Command> a = seed.patterns.get(pat).commands;
			ArrayList<Command> b = this.patterns.get(pat).commands;
			for (int i=0; i< numCommands; i++){
				if(r.nextBoolean()){
					ArrayList<Command> c=b;
					b = a;
					a = c;
				}
				
				if(a.size()>i){
					commandList.add(a.get(i));
				}
				else{
					commandList.add(b.get(i));
				}
			}
			
			//mutating attack pattern
			while(r.nextDouble()<=mutationRate){
				commandList.add(r.nextInt(commandList.size()), randomCommand(100));
			}
			
			patterns.add(new AttackPattern(commandList));
		}
		
		//generate the stats of the new Boss
		double[] stats = null;
		double someFactor = r.nextDouble();
		switch(r.nextInt(5)){
			case 0:
				stats = new double[]{
					this.STR+seed.STR,
					this.CON+seed.CON,
					this.INT+seed.INT,
					this.WIS+seed.WIS,
					this.DEX+seed.DEX,
					this.LUK+seed.LUK};
			break;
			case 1:
				stats = new double[]{
						this.STR+seed.STR*someFactor,
						this.CON+seed.CON*someFactor,
						this.INT+seed.INT*someFactor,
						this.WIS+seed.WIS*someFactor,
						this.DEX+seed.DEX*someFactor,
						this.LUK+seed.LUK*someFactor};
				break;
			case 2:
				stats = new double[]{
						seed.STR,
						seed.CON,
						seed.INT,
						seed.WIS,
						seed.DEX,
						seed.LUK};
				break;
			case 3:
				stats = new double[]{
						this.STR,
						this.CON,
						this.INT,
						this.WIS,
						this.DEX,
						this.LUK};
				break;
			case 4:
				stats = new double[]{
						this.STR*someFactor+seed.STR,
						this.CON*someFactor+seed.CON,
						this.INT*someFactor+seed.INT,
						this.WIS*someFactor+seed.WIS,
						this.DEX*someFactor+seed.DEX,
						this.LUK*someFactor+seed.LUK};
				break;
		}
		
		//new stats w/ mutation
		for(int i=0; i<stats.length; i++){
			if(r.nextDouble()<=mutationRate){
				stats[i]=stats[i]+r.nextInt((int)(stats[i]*0.1))-r.nextInt((int)(stats[i]*0.1));
			}
		}
		
		return new BossSeed(
				stats[0],stats[1],
				stats[2],stats[3],
				stats[4],stats[5],
				patterns);
					
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
