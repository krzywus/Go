package GoServer;

import GoServer.GameSession.GameSession;

public class Matchmaker{
	
	/** Singleton */
	
	private ClientHandler player1 = null, player2 = null;
	
	
/*-------------------------------------------------------------------------------------------------------------------*/
	
	Matchmaker(){
		
	}// end Matchmaker constr
	
	
	/**TODO: 
	 * musza sie zgadzac ustawienia!
	 * jak AI -> zacznij od razu */
	public synchronized boolean addPlayer(ClientHandler player){
		if(player1 == null){
			player1 = player;
			return false;
		}else if(player2 == null){
			player2=player;
			if(player1 != null && player2 != null){
				GameSession session = new GameSession(player1, player2);
			}
			player1 = null;
			player2 = null;
			return true;
		}else {System.out.println("ERROR IN MATCHMAKING."); return false;}
	}// end addPlayer
	
}
