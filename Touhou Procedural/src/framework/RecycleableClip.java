package framework;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class RecycleableClip{
	
	byte[] audio;
	DataLine.Info info;
	AudioFormat audioFormat;
	
	int numThreads=0;
	
	static int maxThreads=10000;
	
	public RecycleableClip(InputStream is) {
		try {
			//cacheing audio data into memory
			AudioInputStream ais = AudioSystem.getAudioInputStream(is);//get audioinputstream from IS
			audioFormat = ais.getFormat();
			int size = (int) (audioFormat.getFrameSize() * ais.getFrameLength());//get size of clip
	        audio = new byte[size];
	        info = new DataLine.Info(Clip.class, audioFormat, size);
	        ais.read(audio, 0, size);
	        ais.close();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play(){
		new SoundSpawner().start();
	}
	
	private class SoundSpawner extends Thread implements LineListener{
		
		@Override
		public void run(){
			if(numThreads<maxThreads){
		        try {
		    		Clip clip = (Clip) AudioSystem.getLine(info);
					clip.open( audioFormat , audio, 0, audio.length);
			        clip.start();
			        clip.addLineListener(this);
			        numThreads++;
			        
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void update(LineEvent evt){
			if (evt.getType() == LineEvent.Type.STOP) {
	  	      evt.getLine().close();
	  	      numThreads--;
	  	    }
		}
		
	}

	
}

