import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;


public class BossSeed {
	public static int nutrients = 500;
	protected Color color;
	protected ArrayList<AttackPattern> patterns = new ArrayList<AttackPattern> (0);
	
	//stat priorities
	double STR,CON,WIS,INT,DEX,LUK;
	
	//stat priorities
	
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
	
	public static Color getTColor(){
	    Random  r = new Random();
	    return new Color(
	    		(int)(5+r.nextDouble()*175),
	    		(int)(5+r.nextDouble()*175),
	    		(int)(5+r.nextDouble()*175)
	    		);
	}
	
	public Boss makeBoss(int x, int y){
		
		int totalPrior = (int) (STR+CON+WIS+INT+DEX);
		
		double damage = 	STR / totalPrior * nutrients;
		double weight = 	(STR/5.0+CON)/2;
		int HP = (int)		(CON / totalPrior * nutrients);
		int MP = (int)		(INT / totalPrior * nutrients);
		double manaRegen = 	WIS / totalPrior * nutrients;
		double moveSpeed = DEX / totalPrior * nutrients;
		double reactionRate= DEX / totalPrior * nutrients;
		int volleySize= (int)((INT+STR)/2 / totalPrior * nutrients);
		double bulletSpeed =	DEX / totalPrior * nutrients;
		double torquePower =(STR+DEX)/2 / totalPrior * nutrients;
		double dodgeChance =	DEX / totalPrior * nutrients;
		
		return new Boss(x,y,color,patterns,
				damage*0.5,
				weight/120 * 30 + 30,
				HP*2,
				MP/4,
				manaRegen/8000,
				moveSpeed,
				1000-2*reactionRate,
				volleySize,
				bulletSpeed/2,
				torquePower,
				dodgeChance);
	}
	
	public Command randomCommand(int volleySize){
		Random r = new Random();
		double d = r.nextDouble();
		if(d<0.01){
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
}
