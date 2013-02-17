package atouhougame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Generation implements Serializable, Iterable<BossSeed>{
	
	ArrayList<BossSeed> bosses;
	public int generationNumber;
	
	static final int generationsize = 4;
	

	public Generation(int number){
		this( new ArrayList<BossSeed>(generationsize), number);
	}
	
	public Generation(ArrayList<BossSeed> generation, int number){
		this.bosses=generation;
		this.generationNumber = number;
	}

	@Override
	public Iterator<BossSeed> iterator() {
		return bosses.iterator();
	}

	public void add(BossSeed bossSeed) {
		bosses.add(bossSeed);
	}

	public BossSeed get(int i) {
		return bosses.get(i);
	}

	public int size() {
		return bosses.size();
	}
	
	public static String getFileName(int number){
		return "generation_"+number+".gen";
	}

	public BossSeed[] getWinners() {
		BossSeed best = this.get(0), secondBest= null;//find the best two
		for(BossSeed seed:this){
			if (best.score<seed.score){
				secondBest=best;
				best=seed;
			}
			else if( (secondBest==null || seed.score>secondBest.score) && seed.score<best.score){
				secondBest=seed;
			}
		}
		return new BossSeed[] {best, secondBest};
	}
	
	public long[] getWinnersID() {
		BossSeed best = this.get(0), secondBest= null;//find the best two
		for(BossSeed seed:this){
			if (best.score<seed.score){
				secondBest=best;
				best=seed;
			}
			else if( (secondBest==null || seed.score>secondBest.score) && seed.score<best.score){
				secondBest=seed;
			}
		}
		return new long[] {best.bossID, secondBest.bossID};
	}
	
}
