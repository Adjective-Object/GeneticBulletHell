package framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class RecycleableClip{
	
	byte[] audio;
	DataLine.Info info;
	AudioFormat audioFormat;
	
	ArrayList<Clip> concurrentClips = new ArrayList<Clip>(0);
	int bufferSize, curClip=0;
	
	public RecycleableClip(InputStream is) {
		this(is,10);
	}
	
	public RecycleableClip(InputStream is, int bufferSize) {
		try {
			//caching audio data into memory
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
		
		this.bufferSize=bufferSize;
		
		for (int i=0; i<bufferSize; i++){
			Clip c;
			try {
				c = (Clip) AudioSystem.getLine(info);
				c.open( audioFormat , audio, 0, audio.length);
				concurrentClips.add(c);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void play(){
		new SoundSpawner().start();
	}
	
	private class SoundSpawner extends Thread{
		
		@Override
		public void run(){
			Clip clip = concurrentClips.get(curClip);
			clip.setFramePosition(0);
			clip.start();
			curClip=(curClip+1)%bufferSize;
		}
		
	}

	
}

