package atouhougame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LocalEvolutionManager extends EvolutionManager{

	Generation currentGeneration;
	
	static final int trials = 3;
	
	int currentBoss, generationNumber;
	
	//makes a new EvolutionManager, w/ seed generation an all, from scratch
	public LocalEvolutionManager() throws IOException, ClassNotFoundException{
		generationNumber=getNumGenerations();
		if(generationNumber==-1){
			this.currentGeneration= new Generation(new ArrayList<BossSeed>(0),0);
			currentBoss=0;
			for(int i=0; i<Generation.generationsize; i++){
				currentGeneration.add(new BossSeed(System.currentTimeMillis()+i));
			}
			generationNumber=0;
		}
		else{
			this.currentGeneration = getGeneration(new File("generation_"+generationNumber+".gen"));
			currentBoss=this.currentGeneration.size()-1;
			advanceSeed();
		}
	}
		
	@Override
	public int getNumGenerations() {
		File[] genFiles = new File(System.getProperty("user.dir")).listFiles(new GenFilter());
		int genNumber = -1;
		
		for (File f:genFiles){
			genNumber++;
		}
		
		return genNumber;
	}

	BossSeed currentSeed=new BossSeed(System.currentTimeMillis());
	
	@Override
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
		if(currentSeed.bossID==bossID){
			advanceSeed();
		}
		return false;
	}
	
	@Override
	public BossSeed getTestingSeed(){
		BossSeed b =  currentGeneration.get(currentBoss);
		return b;
	}
	
	@Override
	public void advanceSeed(){
		BossSeed leastTested = this.getTestingSeed();
		for (int i=this.currentGeneration.size()-1; i>=0;i--){
			if(this.currentGeneration.get(i).timesTested<=leastTested.timesTested){
				leastTested=this.currentGeneration.get(i);
				this.currentBoss=i;
			}
		}
		System.out.println(leastTested);
		
		//advance
		if(leastTested.timesTested>=trials){
			System.out.println("making new genration");
			makeNextGeneration();
			archiveCurrentGeneration();
			this.currentBoss=0;
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
		archiveGeneration(currentGeneration, new File(currentGeneration.getFileName()));
		 
	}

	@Override
	public Generation getGeneration(int n) throws IOException, ClassNotFoundException {
		return getGeneration(new File("generation_"+n+".gen"));
	}

	@Override
	public boolean hasGeneration(int n) {
		return new File("generation_"+n+".gen").exists();
	}
	
}
