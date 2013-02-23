package atouhougame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class EvolutionManager {
	
	public abstract int getNumGenerations();
	
	public abstract boolean scoreSeed(long bossID, double score);//scores a seed
	public abstract BossSeed getTestingSeed(); // gets the seed that is currently being tested
	public abstract void advanceSeed();// advances the testing seed to the next seed
	public abstract Generation getGeneration(int n) throws IOException, ClassNotFoundException;
	public abstract boolean hasGeneration(int n);
	
	//###########HELPER METHODS###############
	
	protected class GenFilter implements FilenameFilter{
		@Override
		public boolean accept(File file, String name) {
			return name.matches(".*generation_[0,1,2,3,4,5,6,7,8,9]*.gen");
		}
	}
	
	public void refreshCache(){}//DOES NOTHING :< only exists for the purposes of the Networked subclass. oh I feel so dirty...
	
	//fileIO methods
	
	public static void archiveGeneration(Generation gen, File f){
		try{
			archiveGeneration(gen, new FileOutputStream(f));
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void archiveGeneration(Generation gen, FileOutputStream fileOut) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(gen);
		out.close();
		System.err.println("cannot save generation");
	}
	
	public static Generation getGeneration(File f)throws IOException, ClassNotFoundException{
	    FileInputStream fileIn = new FileInputStream(f);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		Generation seeds = (Generation) in.readObject();
		in.close();
		fileIn.close();
		return seeds;
	}
	
}
