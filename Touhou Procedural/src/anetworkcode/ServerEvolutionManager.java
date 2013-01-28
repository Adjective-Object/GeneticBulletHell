package anetworkcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import atouhougame.BossSeed;

public class ServerEvolutionManager{

	ArrayList<BossSeed> currentGeneration;
	
	static final int generationsize = 5, trials = 10;
	
	int generationNumber;
	
	//makes a new EvolutionManager, w/ seed generation an all, from scratch
	public ServerEvolutionManager(){
		currentGeneration = loadLatestGeneration();
	}
		
	BossSeed currentSeed=new BossSeed(System.currentTimeMillis());
	
	//advances to next boss seed
	public boolean scoreSeed(double score, int ID){
		for(BossSeed b:currentGeneration){
			if(b.bossID==ID){
				b.score+=(score-b.score)/b.timesTested;
				//weighted average of scores
				b.timesTested++;
				return true;
			}
		}
		return false;
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
		System.out.println("archiving generation");
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

	public static ArrayList<BossSeed> loadLatestGeneration() {
		File[] genFiles = new File(System.getProperty("user.dir")).listFiles(new GenFilter());
		ArrayList<ArrayList<BossSeed>> seeds = new ArrayList<ArrayList<BossSeed>>(0);
		if(genFiles.length>0){
			return getGeneration(genFiles[genFiles.length-1]);
		} else{
			ArrayList<BossSeed> s = new ArrayList<BossSeed>(0);
			for(int i=0; i<generationsize; i++){
				s.add(new BossSeed(System.currentTimeMillis()));
			}
			return s;
		}
	}
}

class GenFilter implements FilenameFilter{
	@Override
	public boolean accept(File file, String name) {
		return name.matches(".*generation_[0,1,2,3,4,5,6,7,8,9]*.gen");
	}
}
