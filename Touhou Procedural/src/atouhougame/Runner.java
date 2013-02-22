package atouhougame;

import java.io.IOException;

import anetworkcode.Client;
import anetworkcode.ClientEvolutionManager;
import anetworkcode.Server;
import atouhougame.gamescreens.BossRushRouter;
import atouhougame.gamescreens.ExitGame;
import atouhougame.gamescreens.FavoritesScreen;
import atouhougame.gamescreens.GalleryScreen;
import atouhougame.gamescreens.TMenu;
import atouhougame.gamescreens.TextForwardScreen;
import framework.Game;
import framework.Global;
import framework.TopFrame;

public class Runner  {
	
    public static void main(String[] args){
    	
    	if(args.length==0){
    		args = new String[] {"-sc"};
    	}
    	
    	if( args[0].equals("-s") || args[0].equals("-server") ){
    		int port;
    		if(args.length>=2){ port=Integer.parseInt(args[2]); } else{ port=1337;} //pull port from args, else use default.

    		System.out.println("Running as Server "+port);
    		
    		Server threadServer= new Server(port);//set server port
    		threadServer.start();
    	}
    	else if( args[0].equals("-c") || args[0].equals("-client") ){
    		System.out.println("Running as Client");
    		int port;
    		String address;
    		
    		if(args.length>=2){ address=args[1]; } else{ address="localhost";} //TODO make redirect to some set web server
    		if(args.length>=3){ port=Integer.parseInt(args[2]); } else{ port=1337;} //pull port from args, else use default.
    		
    		Client.setCommunicationConstants(address,port);
    		
    		Game g = makeTheGame();
        	TopFrame t = new TopFrame(g,Global.width,Global.height);
        	g.start();
    	} else if( args[0].equals("-sc") || args[0].equals("-both") ){
    		System.out.println("Running as Server-Client Hybrid");
    		int port;
    		if(args.length>=2){ port=Integer.parseInt(args[2]); } else{ port=1337;} //pull port from args, else use default.
    		
    		//server make thread go
    		Server threadServer= new Server(port);//set server port
    		threadServer.start();
    		
    		//client make thread go
    		Client.setCommunicationConstants("localhost",port);
    		Game g = makeTheGame();
        	TopFrame t = new TopFrame(g,Global.width,Global.height);
        	g.start();
    	}
    }
    
    private static boolean serverExists(){
    	try {
			if(Client.serverExists()){
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
    }
    
	private static Game makeTheGame(){
		TGlobal.playNetworked = serverExists();
		Game galleryScreen;
		if(TGlobal.playNetworked){
			TGlobal.evolutionManager = new ClientEvolutionManager();
			//galleryScreen = new TextForwardScreen("Sorry.",new String[] {"The gallery function has not been implemented for networked games."});
		} else{
			TGlobal.evolutionManager = new LocalEvolutionManager();
			//galleryScreen = new GalleryScreen(TGlobal.evolutionManager);
		}
		
		TGlobal.bossRushRouter=new BossRushRouter(TGlobal.evolutionManager);
		
		TGlobal.mainMenu = new TMenu(
				new String[] {
						"Boss Rush",
						"Gallery",
						"Favorites",
						"About",
						"Exit"},
				new Game[] {
						TGlobal.bossRushRouter,
						new GalleryScreen(TGlobal.evolutionManager),
						new FavoritesScreen(),
						new TextForwardScreen(TGlobal.aboutScreenTitle,TGlobal.aboutScreenText),
						new ExitGame()},
				TGlobal.greyBack,
				TGlobal.fbig
			);
		
		if(TGlobal.playNetworked){
			return TGlobal.mainMenu;
		}
		else{
			return new TextForwardScreen("Fuck.",new String[] {
					"Cannot connect to server at "+Client.serverAddr+".",
					"Playing with locally generated content instead."});
		}
	}
    
}