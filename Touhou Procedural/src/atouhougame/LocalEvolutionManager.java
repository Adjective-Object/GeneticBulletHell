package atouhougame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LocalEvolutionManager{

	ArrayList<BossSeed> currentGeneration;
	
	static final int generationsize = 10, trials = 10;
	
	int currentBoss,generationNumber=0;
	
	//makes a new EvolutionManager, w/ seed generation an all, from scratch
	public LocalEvolutionManager(){
		ArrayList<ArrayList<BossSeed>> seeds = loadGenerations();
		if(seeds.size()==0){
			this.currentGeneration= new ArrayList<BossSeed>(0);
			currentBoss=0;
			generationNumber=0;
			for(int i=0; i<generationsize; i++){
				currentGeneration.add(new BossSeed(System.currentTimeMillis()));
			}
		}
		else{
			this.currentGeneration = seeds.get(seeds.size()-1);
			generationNumber=seeds.size()-1;
			currentBoss=0;
			advanceSeed();
		}
	}
		
	BossSeed currentSeed=new BossSeed(System.currentTimeMillis());
	
	public BossSeed currentSeed(){
		return currentGeneration.get(currentBoss);
	}
	
	//advances to next boss seed
	public void scoreLastSeed(double score){
		currentGeneration.get(currentBoss).score+=(score-currentGeneration.get(currentBoss).score)
			/(1+currentGeneration.get(currentBoss).timesTested);
		//weighted average of scores
		currentGeneration.get(currentBoss).timesTested++;
		archiveCurrentGeneration();
	}
	
	public boolean scoreSeed(long bossID, double score){
		for(BossSeed b:currentGeneration){
			if(b.bossID==bossID){
				b.score+=(score-b.score)
				/(1+b.timesTested);
				archiveCurrentGeneration();
				return true;
			}
		}
		return false;
	}
	
	public BossSeed getTestingSeed(){
		BossSeed b =  currentGeneration.get(currentBoss);
		advanceSeed();
		return b;
	}
	
	public void advanceSeed(){
		int maxTrials = 0,fails=-1;
		while(this.currentGeneration.get(currentBoss).timesTested>=maxTrials && fails<generationsize*2){
			maxTrials=this.currentGeneration.get(currentBoss).timesTested;
			currentBoss=(currentBoss+1)%currentGeneration.size();
			fails++;
		}
		
		//advance
		if(currentGeneration.get(currentBoss).timesTested>=trials){
			System.out.println("making new genration");
			makeNextGeneration();
			archiveCurrentGeneration();
		}
		System.out.println(this.currentGeneration);
		System.out.println(this.currentGeneration.get(currentBoss).bossID);
	}
	
	//makes a new generation
	private void makeNextGeneration(){
		System.out.println("generating new generation");
		
		BossSeed best = currentGeneration.get(0), secondBest= null;//find the best two
		for(BossSeed seed:currentGeneration){
			if (best.score<seed.score){
				secondBest=best;
				best=seed;
			}
			else if( (secondBest==null || seed.score>secondBest.score) && seed.score<best.score){
				secondBest=seed;
			}
		}
		//mating
		currentGeneration = new ArrayList<BossSeed>(0);
		for (int i=0; i<generationsize-1; i++){
			currentGeneration.add(best.breedWith(secondBest));
			System.out.println("breeding: "+best+" "+secondBest);
		}
		//add one more random for faster solution finding.
		currentGeneration.add(new BossSeed(System.currentTimeMillis()));
		generationNumber++;
		
	}
	
	public void archiveCurrentGeneration(){
		System.out.println("archiving generation to generation_"+generationNumber+".gen");
		try {
			FileOutputStream fileOut = new FileOutputStream("generation_"+generationNumber+".gen");
			ObjectOutputStream out;
			out = new ObjectOutputStream(fileOut);
			out.writeObject(currentGeneration);
			out.close();
		} catch (IOException e) {
			System.err.println("cannot save generation");
			e.printStackTrace();
		}
		 
	}
	
	public static ArrayList<BossSeed> getGeneration(File f){
		ArrayList<BossSeed> seeds=null;
		try {
	        FileInputStream fileIn = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			seeds = (ArrayList<BossSeed>) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException io) {
			System.err.println("File of unreadable format.");
			io.printStackTrace();
		}catch(ClassNotFoundException c){
			System.err.println("class not found.");
	        c.printStackTrace();
		}
		return seeds;
	}

	public ArrayList<ArrayList<BossSeed>> loadGenerations() {
		File[] genFiles = new File(System.getProperty("user.dir")).listFiles(new GenFilter());
		
		ArrayList<ArrayList<BossSeed>> seeds = new ArrayList<ArrayList<BossSeed>>(0);
		
		for (File f:genFiles){
			seeds.add(getGeneration(f));
		}
		
		return seeds;
	}
	
	private class GenFilter implements FilenameFilter{
		@Override
		public boolean accept(File file, String name) {
			return name.matches(".*generation_[0,1,2,3,4,5,6,7,8,9]*.gen");
		}
	}
	
}
