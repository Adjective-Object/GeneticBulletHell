package actualgame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EvolutionManager {

	ArrayList<BossSeed> currentGeneration;
	
	static final int generationsize = 3, trials = 2;
	
	int currentBoss,generationNumber;
	
	//makes a new EvolutionManager, w/ seed generation an all, from scratch
	public EvolutionManager(){
		this.currentGeneration= new ArrayList<BossSeed>(0);
		currentBoss=0;
		for(int i=0; i<generationsize; i++){
			currentGeneration.add(new BossSeed(System.currentTimeMillis()));
		}
	}
	
	public EvolutionManager(File generationFile){
		this.currentGeneration = getGeneration(generationFile);
		//finds the last boss tested
		int failcount=0, maxTrials=0;
		while(currentGeneration.get(currentBoss).timesTested>=maxTrials && failcount<generationsize){
			maxTrials=currentGeneration.get(currentBoss).timesTested;
			failcount++;
			currentBoss++;
		}
		//and tests the one after that
		currentBoss=(currentBoss+1)%currentGeneration.size();
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
	}
	
	public void advanceSeed(){
		currentBoss=(currentBoss+1)%currentGeneration.size();
		
		//advance
		if(currentGeneration.get(currentBoss).timesTested>=trials){
			System.out.println("archiving generation");
			archiveGeneration();
			System.out.println("generating new generation");
			makeNextGeneration();
		}
	}
	
	//makes a new generation
	private void makeNextGeneration(){
		BossSeed best = currentGeneration.get(0), secondBest= null;//find the best two
		for(BossSeed seed:currentGeneration){
			if (best.score<seed.score){
				secondBest=best;
				best=seed;
			}
		}
		//mating
		currentGeneration = new ArrayList<BossSeed>(0);
		for (int i=0; i<generationsize-1; i++){
			currentGeneration.add(best.breedWith(secondBest));
		}
		//add one more random for faster solution finding.
		currentGeneration.add(new BossSeed(System.currentTimeMillis()));
		
	}
	
	private void archiveGeneration(){
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
	
	public ArrayList<ArrayList<BossSeed>> getGenerationArchive(){
		ArrayList<ArrayList<BossSeed>> archive = new ArrayList<ArrayList<BossSeed>>(0);
		//TODO loading generations
		return archive;
	}
	
	public ArrayList<BossSeed> getGeneration(File f){
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

		System.out.println(genFiles.length);
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
