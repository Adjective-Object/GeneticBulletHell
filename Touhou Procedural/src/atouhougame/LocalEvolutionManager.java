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

	Generation currentGeneration;
	
	static final int trials = 3;
	
	int currentBoss,generationNumber;
	
	//makes a new EvolutionManager, w/ seed generation an all, from scratch
	public LocalEvolutionManager(){
		generationNumber=getNumGenerations();
		if(generationNumber==-1){
			this.currentGeneration= new Generation(new ArrayList<BossSeed>(0),0);
			currentBoss=0;
			for(int i=0; i<Generation.generationsize; i++){
				currentGeneration.add(new BossSeed(System.currentTimeMillis()));
			}
			generationNumber=0;
		}
		else{
			this.currentGeneration = getGeneration(new File("generation_"+generationNumber+".gen"));
			currentBoss=this.currentGeneration.size()-1;
			advanceSeed();
		}
	}
		
	private int getNumGenerations() {
		File[] genFiles = new File(System.getProperty("user.dir")).listFiles(new GenFilter());
		int genNumber = -1;
		
		for (File f:genFiles){
			genNumber++;
		}
		
		return genNumber;
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
				b.timesTested++;
				archiveCurrentGeneration();
				return true;
			}
		}
		return false;
	}
	
	public BossSeed getTestingSeed(){
		advanceSeed();
		BossSeed b =  currentGeneration.get(currentBoss);
		return b;
	}
	
	public void advanceSeed(){
		int maxTrials = 0,fails=-1;
		while(this.currentGeneration.get(currentBoss).timesTested>=maxTrials && fails<Generation.generationsize*2){
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
		
		BossSeed[] winners = currentGeneration.getWinners();
		
		//mating
		currentGeneration = new Generation(generationNumber+1);
		for (int i=0; i<Generation.generationsize-1; i++){
			currentGeneration.add(winners[0].breedWith(winners[1]));
			System.out.println("breeding: "+winners[0]+" "+winners[1]);
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
	
	public static Generation getGeneration(File f){
		Generation seeds=null;
		try {
	        FileInputStream fileIn = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			seeds = (Generation) in.readObject();
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
	
	private class GenFilter implements FilenameFilter{
		@Override
		public boolean accept(File file, String name) {
			return name.matches(".*generation_[0,1,2,3,4,5,6,7,8,9]*.gen");
		}
	}
	
	public ArrayList<Generation> loadGenerations() {
		File[] genFiles = new File(System.getProperty("user.dir")).listFiles(new GenFilter());
	
		ArrayList<Generation> seeds = new ArrayList<Generation>(0);
		
		for (File f:genFiles){
			seeds.add(getGeneration(f));
		}
		
		return seeds;
	}

	public Generation getGeneration(int n) {
		return getGeneration(new File("generation_"+n+".gen"));
	}

	public boolean hasGeneration(int n) {
		return new File("generation_"+n+".gen").exists();
	}
	
	public void refreshCache(){}//DOES NOTHING :< only exists for the purposes of the Networked subclass. oh I feel so dirty...

	
}
