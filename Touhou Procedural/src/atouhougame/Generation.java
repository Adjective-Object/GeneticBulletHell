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
	
}
