package anetworkcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
	
	ServerSocket serverSocket;
	static final int serverPort = 1337;
	boolean running = false;
	
	static final int GET_GENERATION = 0;
	static final int SUBMIT_SCORE = 1;
	
	
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
        BufferedReader in = new BufferedReader(
				new InputStreamReader(
				clientSocket.getInputStream()));
        switch(in.read()){
        	case GET_GENERATION:
        		sendGeneration(clientSocket);
        	case SUBMIT_SCORE:
        		acceptScore(in);
        }
	}
	
	private void sendGeneration(Socket clientSocket){
		System.out.println("sending new generation")
	}
	
	private void getScore(BufferedReader in){
		System.out.println("getting score")
	}
	
}
