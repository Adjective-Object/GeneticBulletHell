package anetworkcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;

import atouhougame.BossSeed;
import atouhougame.Generation;

public class Client{
	
	private static String serverAddr = "localhost";
	private static int serverPort;
	
	public static void setCommunicationConstants(String addr, int port){
		Client.serverPort = port;
		Client.serverAddr=addr;
	}
	
	public static BossSeed requestBoss(){
		try{
			Socket s = makeHandshake(serverAddr, serverPort);

			System.out.println("CLIENT: now attempting to download a boss");
			
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream())); 
			
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
			Socket s = makeHandshake(serverAddr, serverPort);
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			System.out.println("CLIENT: now attempting to submit score");
			
			w.write(Server.SUBMIT_SCORE);
			w.flush();
			DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
			dataOut.writeDouble(score);
			dataOut.writeLong(bossID);
			dataOut.flush();
			
			int response=r.read();

			w.close();
			if(response==Server.SUBMIT_SCORE){
				r.close();
				return;//success
			}	
			else if(response==Server.ERROR){
				System.err.println("SERVER reported error:");
				System.err.println(r.readLine());
				r.close();
				return;//failure
			}
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static boolean serverExists() throws IOException{
		try{
			Socket s = makeHandshake(serverAddr, serverPort);
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			w.write(Server.CHECK_EXISTS);
			w.flush();
			
			int conf = r.read();
			w.close();
			r.close();
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
	
	public static Generation getGeneration(int generationNumber){	
		try{
			Socket s = makeHandshake(serverAddr, serverPort);
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			System.out.println("CLIENT: now attempting to download generaton "+generationNumber);
			
			w.write(Server.SEND_GENERATION);
			w.write(generationNumber);
			w.flush();

			ObjectInputStream objin = new ObjectInputStream(s.getInputStream());
			
			if(objin.readBoolean()){//if the file exists
				System.out.println("CLIENT: Server reported generation exists.");
				Generation gen = (Generation) objin.readObject();
				return gen;
			} else{
				System.out.println("CLIENT: Server reported no such generation.");
			}
			objin.close();
			w.close();
			r.close();
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean checkGenerationExists(int generationNumber) {
		try{
			Socket s = makeHandshake(serverAddr, serverPort);
			BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			System.out.println("CLIENT: now checking for existance of generation "+generationNumber);
			
			w.write(Server.CHECK_GENERATION);
			w.write(generationNumber);
			w.flush();
			
			if(r.read()==1){//if the file exists
				return true;
			}
			
			w.close();
			r.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	private static Socket makeHandshake(String address, int port) throws IOException{
		Socket s = new Socket(address,port);
		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
		s.setSoTimeout(1000);
		
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
	
}
