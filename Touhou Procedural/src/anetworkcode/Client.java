package anetworkcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client{

	
	public static void getGeneration(int gen){
		try{
			Socket s = makeHandshake("localhost", Server.serverPort);
			
			BufferedReader r =
	        	new BufferedReader(
					new InputStreamReader(
					s.getInputStream()));
			PrintWriter w = 
				new PrintWriter(
						s.getOutputStream(),
						true);
			
			w.write(Server.GET_GENERATION);
			w.write(gen);
			
			int response=r.read();
			if(response==Server.GET_GENERATION){
				//TODO read generation
			}	
			else if(response==Server.ERROR){
				System.err.println("SERVER reported error:");
				System.err.println(r.readLine());
				return;
			}
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void submitScore(int score, int bossID){
		try{
			Socket s = makeHandshake("localhost", Server.serverPort);
			
			BufferedReader r =
	        	new BufferedReader(
					new InputStreamReader(
					s.getInputStream()));
			PrintWriter w = 
				new PrintWriter(
						s.getOutputStream(),
						true);
			
			w.write(Server.SUBMIT_SCORE);
			w.write(score);
			w.write(bossID);
			
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
		
		BufferedReader r =
        	new BufferedReader(
				new InputStreamReader(
						s.getInputStream()));
		PrintWriter w = 
			new PrintWriter(
					s.getOutputStream(),
					true
			);
		
		String handshake = r.readLine();
		if(handshake.equals(Server.handshake_1)){
			w.write(Server.handshake_2);
		} else{
			w.write("That's not my handshake!");
			abortConnection(r,w,s);
		}
		
		int responseCode = r.read();
		
		if(responseCode==Server.HANDSHAKE_SUCESS){
			
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
