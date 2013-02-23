package atouhougame;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		volleySize = (int)(volleySize/2+volleySize/2*r.nextDouble());
		double d = r.nextDouble();
		if(d<0.3){
			return new SleepCommand();
		}
		else if(d<0.5){
			return new MoveCommand(
					(int)(TouhouGame.playFieldLeft.x+r.nextDouble()*(TouhouGame.playFieldRight.x-TouhouGame.playFieldLeft.x)),
					(int)(TouhouGame.playFieldLeft.y+r.nextDouble()*(TouhouGame.playFieldRight.y-TouhouGame.playFieldLeft.y)),
					r.nextDouble(),
					r.nextBoolean());
		}
		else if(d<0.7){
			ArrayList<BulletSeed> seeds = new ArrayList<BulletSeed>(0);
			seeds.add(new BulletSeed());
			return new FireLaserCommand(seeds);
		}
		else{
			return new FireCommand(getRandBulletArr(1+r.nextInt(100)),
					r.nextBoolean(),
					r.nextBoolean(),
					r.nextBoolean(),
					r.nextBoolean(),
					r.nextBoolean(),
					r.nextBoolean());
		}
	}
	
	public static ArrayList<BulletSeed> getRandBulletArr(int numBullets){
		ArrayList<BulletSeed> seed = new ArrayList<BulletSeed>(0);
		for(int d=0; d<numBullets;d++){
			seed.add(new BulletSeed());
		}
		return seed;
	}
	
	@Override
	public String toString(){
		return "<Boss name = "+this.getName()+" id="+this.bossID+" >";
	}
	
	public static final double mutationRate = 0.5;
	private static int recentOffspring=0;
	
	public BossSeed breedWith(BossSeed seed){//TODO mixing and mutating actual bullets/commands
		Random r = new Random(System.currentTimeMillis()+recentOffspring);
		recentOffspring++;
		
		//generate new set of attack patterns
		ArrayList<AttackPattern> patterns = new ArrayList<AttackPattern>(3);
		for(int pat=0; pat<3; pat++){
			//determine number of commands in pattern
			ArrayList<Command> commandList = new ArrayList<Command>(0);
			int numCommands = this.patterns.get(pat).commands.size();
			if(seed.patterns.get(pat).commands.size()>numCommands){
				numCommands=seed.patterns.get(pat).commands.size();
			}
			
			//generating splice together the lists of commands
			ArrayList<Command> a = seed.patterns.get(pat).commands;
			ArrayList<Command> b = this.patterns.get(pat).commands;
			for (int i=0; i< numCommands; i++){
				if(r.nextDouble()<=mutationRate){//if <random> then mutate
					commandList.add(randomCommand(100));
				} else{//elsepull from parents
					if(r.nextBoolean()){//swap if randomly
						ArrayList<Command> c=b;
						b = a;
						a = c;
					}
					
					if(a.size()>i){//pull from other list if list is too short
						commandList.add(a.get(i));
					}
					else{
						commandList.add(b.get(i));
					}
				}
			}
			patterns.add(new AttackPattern(commandList));
		}
		
		//generate the stats of the new Boss
		double[] stats = null;
		double someFactor = r.nextDouble();
		stats = new double[]{
			this.STR*(1-someFactor)+seed.STR*someFactor,
			this.CON*(1-someFactor)+seed.CON*someFactor,
			this.INT*(1-someFactor)+seed.INT*someFactor,
			this.WIS*(1-someFactor)+seed.WIS*someFactor,
			this.DEX*(1-someFactor)+seed.DEX*someFactor,
			this.LUK*(1-someFactor)+seed.LUK*someFactor};

		
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
	
	static final String[] humanNames= getHumanNames("names.txt");
	private static String[] getHumanNames(String yeah){
		int lengthfile = 665;//pre-known length of list of names
		
		String[] names = new String[lengthfile];
		BufferedReader r;
		try {
			r = new BufferedReader( new InputStreamReader( ClassLoader.getSystemClassLoader().getResourceAsStream(yeah) ));

			for(int i=0; i<lengthfile; i++){
				names[i] = r.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return names;
	}
	
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
