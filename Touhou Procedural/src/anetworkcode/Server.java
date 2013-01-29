package anetworkcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import atouhougame.BossSeed;
import atouhougame.LocalEvolutionManager;

public class Server extends Thread{
	
	ServerSocket serverSocket;
	static final int serverPort = 1337;
	boolean running = false;
	
	static final int ERROR = 0;
	
	static final int GET_BOSS = 1;
	static final int SUBMIT_SCORE = 2;
	static final int HANDSHAKE_SUCESS = 3;
	
	static final String handshake_1 = "Handshake_SI_COOL\n";
	static final String handshake_2 = "Handshake_YEAH_COOL\n";
	
	LocalEvolutionManager evoManager;
	
	@Override
	public void run(){
		 try{
			 serverSocket = new ServerSocket(serverPort);
		     serverSocket.setSoTimeout(10);
	     }catch (IOException e) {
	    	 System.err.println("Could not listen on port: "+serverPort+".");
	    	 e.printStackTrace();
	         System.exit(1);
	     }
	     
	     evoManager = new LocalEvolutionManager();
	     running=true;
	     
	     while(running){
	    	 try {
	             Socket clientSocket = serverSocket.accept();
	    		 System.out.println("accepting new client");
	             clientCommunications(clientSocket);
	         } catch (IOException e) {
	         }
	     }
	        
	}

	private void clientCommunications(Socket clientSocket) throws IOException {
		clientSocket.setSoTimeout(60000);
		
		try{
			PrintWriter out = 
				new PrintWriter(
						clientSocket.getOutputStream(),
						true
				);
	        BufferedReader in =
	        	new BufferedReader(
					new InputStreamReader(
					clientSocket.getInputStream()));
	        
			out.write(Server.handshake_1);
			out.flush();
			
	        String handshakeResponse=in.readLine();
	        if(Server.handshake_2.startsWith(handshakeResponse)){
	        	System.out.println("SERVER: correct handshake response!");
	        	out.write(HANDSHAKE_SUCESS);
	        	out.flush();
	        	switch(in.read()){
	        	case GET_BOSS:
	        		sendSeed(in,clientSocket.getOutputStream());
	        	case SUBMIT_SCORE:
	        		acceptScore(in,out);
	        	}
	        }
	        else{
	        	out.write(ERROR);
	        	out.write("Handshake Failed - Incorrect Version?\n");
	        }
	        
	        out.flush();
	        out.close();
	        in.close();
	        clientSocket.close();
		}catch ( java.net.SocketTimeoutException e ){
			System.out.println("you're too slow!");
		}
	}

	
	private void sendSeed(BufferedReader in, OutputStream out) throws IOException{
		System.out.println("SEVER: Client requested a boss ");
		BossSeed b = evoManager.getTestingSeed();
		
		ObjectOutputStream objectOut = new ObjectOutputStream(out);
		objectOut.writeObject(b);
		objectOut.flush();
		
		System.out.println("SEVER: Boss "+b.bossID+" sent to client");
	}
	
	 public void sendFile(OutputStream os, File file) throws IOException {
		 FileInputStream r = new FileInputStream(file);
		 while (r.available()>0){
			os.write(r.read()); 
		 }
	 }

	private void acceptScore(BufferedReader in, PrintWriter out) throws IOException{
		System.out.println("SERVER: accepting score from client");
		int score = in.read();
		int bossID = in.read();
		
		//scores seed
		if(evoManager.scoreSeed(bossID, score)){
			System.out.println("SERVER: scored boss "+bossID+" sucessfully "+score);
			out.write(Server.SUBMIT_SCORE);
			return;
		}
		else{//if the seed doesn't exist;
			out.write(Server.ERROR);
			out.write("Boss with given ID does not exist in this generation.\n");
			return;
		}
		
	}
	
}
