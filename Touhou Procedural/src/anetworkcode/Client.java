package anetworkcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import atouhougame.BossSeed;

public class Client{
	//not even close to thread safe
	
	private static BufferedReader r;
	private static PrintWriter w;
	
	
	public static BossSeed requestBoss(){
		try{
			Socket s = makeHandshake("localhost", Server.serverPort);

			System.out.println("CLIENT: now attempting to download a boss");
			
			w.write(Server.GET_BOSS);
			w.flush();
			
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			BossSeed seed = (BossSeed) in.readObject();
			in.close();
			return seed;
			
		} catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static File getGenFile(BufferedReader r, int gen) throws IOException{
		File f = new File("generation_"+gen+".gen");
		FileOutputStream fout = new FileOutputStream(f);
		int p = r.read();
		while (p != -1){
			fout.write(p);
			p = r.read();
		}
		fout.close();
		return f;
	}
	
	public static void submitScore(int score, int bossID){
		try{
			Socket s = makeHandshake("localhost", Server.serverPort);
			
			System.out.println("CLIENT: now attempting to submit score");
			
			w.write(Server.SUBMIT_SCORE);
			w.write(score);
			w.write(bossID);
			w.flush();
			
			int response=r.read();
			if(response==Server.SUBMIT_SCORE){
				return;//success
			}	
			else if(response==Server.ERROR){
				System.err.println("SERVER reported error:");
				System.err.println(r.readLine());
				return;//failure
			}
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static Socket makeHandshake(String address, int port) throws IOException{
		Socket s = new Socket(address,port);
		
		r =
        	new BufferedReader(
				new InputStreamReader(
						s.getInputStream()));
		
		w = 
			new PrintWriter(
					s.getOutputStream(),
					true
			);
		
		String handshake = r.readLine();
		
		if(Server.handshake_1.startsWith(handshake)){
			System.out.println("CLIENT: Correct handshake!");
			w.write(Server.handshake_2);
			w.flush();
		} else{
			System.out.println("CLIENT: Wrong handshake, aborting");
			w.write("That's not my handshake!");
			w.flush();
			abortConnection(r,w,s);
			return null;
		}
		
		int responseCode = r.read();
		
		if(responseCode==Server.HANDSHAKE_SUCESS){
			System.out.println("CLIENT: Got handshake confirmation");
			return s;
			
		} else if (responseCode==Server.ERROR){
			System.err.println("SERVER reported error:");
			System.err.println(r.readLine());
			return null;
		}
		return null;
	}
	
	private static void abortConnection(BufferedReader r,PrintWriter w,Socket s) throws IOException{
		r.close();
		w.flush();
		w.close();
		s.close();
	}
	
}
