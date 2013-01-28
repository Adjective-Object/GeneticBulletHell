package anetworkcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
	
	ServerSocket serverSocket;
	static final int serverPort = 1337;
	boolean running = false;
	
	static final int ERROR = 0;
	
	static final int GET_GENERATION = 1;
	static final int SUBMIT_SCORE = 2;
	static final int HANDSHAKE_SUCESS = 3;
	
	static final String handshake_1 = "Handshake_SI_COOL\n";
	static final String handshake_2 = "Handshake_YEAH_COOL\n";
	
	ServerEvolutionManager evoManager;
	
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
	     
	     evoManager = new ServerEvolutionManager();
	     running=true;
	     
	     while(running){
	    	 try {
	             Socket clientSocket = serverSocket.accept();
	             clientCommunications(clientSocket);
	         } catch (IOException e) {
	             System.err.println("Accept failed.");
	             System.exit(1);
	         }
	     }
	        
	}

	private void clientCommunications(Socket clientSocket) throws IOException {
		clientSocket.setSoTimeout(1000);
		
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
        String handshakeResponse=in.readLine();
        
        if(handshakeResponse.equals(handshake_2)){
        	out.write(HANDSHAKE_SUCESS);
        	switch(in.read()){
        	case GET_GENERATION:
        		sendGeneration(in,out);
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
	}

	
	private void sendGeneration(BufferedReader in, PrintWriter out) throws IOException{
		int gen = in.read();
		File f = new File("generation_"+gen+".gen");
		if(f.exists()){
			System.out.println("sending generation "+gen+" to client ");
			out.write(GET_GENERATION);
			//TODO send generation
		} else{
			out.write(ERROR);
			out.write("Cannot send generation "+gen+": no such file\n");
			System.err.println("cannot send generation "+gen+" to client: no such file");
		}
		
		
		
	}
	
	private void acceptScore(BufferedReader in, PrintWriter out) throws IOException{
		System.out.println("getting score");
		int score = in.read();
		int bossID = in.read();
		
		//scores seed
		if(evoManager.scoreSeed(score, bossID)){
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
