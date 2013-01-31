package anetworkcode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import atouhougame.BossSeed;

public class Client{
	//not even close to thread safe
	
	private static BufferedReader r;
	private static OutputStream os;
	private static PrintWriter w;
	
	private static final String serverAddr = "localhost";
	
	public static BossSeed requestBoss(){
		try{
			Socket s = makeHandshake(serverAddr, Server.serverPort);

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
	
	public static void submitScore(double score, long bossID){
		try{
			Socket s = makeHandshake(serverAddr, Server.serverPort);
			
			System.out.println("CLIENT: now attempting to submit score");
			

			w.write(Server.SUBMIT_SCORE);
			w.flush();
			DataOutputStream dataOut = new DataOutputStream(os);
			dataOut.writeDouble(score);
			dataOut.writeLong(bossID);
			dataOut.flush();
			
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
	
	public static boolean serverExists() throws IOException{
		try{
			Socket s = makeHandshake(serverAddr, Server.serverPort);
			
			r =
	        	new BufferedReader(
					new InputStreamReader(
							s.getInputStream()));
			
			w = 
				new PrintWriter(
						s.getOutputStream(),
						true
				);
			
			w.write(Server.CHECK_EXISTS);
			w.flush();
			
			int conf = r.read();
			if(conf==Server.CHECK_EXISTS){
				return true;
			}
			else{
				System.out.println("Incorrect response from server. GGOUT!");
				return false;
			}
		} catch (ConnectException ce){
			return false;
		}
		
	}
	
	private static Socket makeHandshake(String address, int port) throws IOException{
		Socket s = new Socket(address,port);
		s.setSoTimeout(1000);
		
		r =
        	new BufferedReader(
				new InputStreamReader(
						s.getInputStream()));
		
		os = s.getOutputStream();
		
		w = 
			new PrintWriter(
					os,
					true
			);
		
		System.out.println("CLIENT: Initiating handshake");
		
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
