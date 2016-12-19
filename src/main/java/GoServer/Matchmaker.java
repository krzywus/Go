package GoServer;

import GoServer.GameSession.GameSession;

public class Matchmaker{
	
	/** TODO: Singleton? */
	
	/** Tablica do laczenia klientow w pary*/
	private ClientHandler smallBoardPlayer[];
	private ClientHandler mediumBoardPlayer[];
	private ClientHandler bigBoardPlayer[];
	
	
/*-------------------------------------------------------------------------------------------------------------------*/
	
	/**Konstruktor klasy, inicjuje obiekty.*/
	Matchmaker(){
		smallBoardPlayer	= new ClientHandler[2];
		mediumBoardPlayer 	= new ClientHandler[2];
		bigBoardPlayer 		= new ClientHandler[2];
	}// end Matchmaker constr
	
	
	/** Metoda laczy graczy w pary na podstawie ustawien. Kiedy para jest polaczona rozpoczyna gre dla tej pary. */
	public synchronized boolean addPlayer(ClientHandler player, int boardSize, int opponentType){
		if(opponentType == 1){
			return false;/*TODO: Uruchom gre przeciwko AI*/
		} else{
		switch(boardSize){
		case 9: { 
			if(smallBoardPlayer[0] == null){
				smallBoardPlayer[0] = player;
				return false;
			}else if(smallBoardPlayer[1] == null){
				smallBoardPlayer[1]=player;
				if(smallBoardPlayer[0] != null && smallBoardPlayer[1] != null){
					GameSession session = new GameSession(smallBoardPlayer[0], smallBoardPlayer[1]);
				}
				smallBoardPlayer[0] = null;
				smallBoardPlayer[1] = null;
				return true;
			}else {System.out.println("ERROR IN MATCHMAKING."); return false;} } // end case 9
		case 13:{ 
			if(mediumBoardPlayer[0] == null){
				mediumBoardPlayer[0] = player;
				return false;
			}else if(mediumBoardPlayer[1] == null){
				mediumBoardPlayer[1]=player;
				if(mediumBoardPlayer[0] != null && mediumBoardPlayer[1] != null){
					GameSession session = new GameSession(mediumBoardPlayer[0], mediumBoardPlayer[1]);
				}
				mediumBoardPlayer[0] = null;
				mediumBoardPlayer[1] = null;
				return true;
			}else {System.out.println("ERROR IN MATCHMAKING."); return false;} } // end case 13
		case 19:{ 
			if(bigBoardPlayer[0] == null){
				bigBoardPlayer[0] = player;
				return false;
			}else if(bigBoardPlayer[1] == null){
				bigBoardPlayer[1]=player;
				if(bigBoardPlayer[0] != null && bigBoardPlayer[1] != null){
					GameSession session = new GameSession(bigBoardPlayer[0], bigBoardPlayer[1]);
				}
				bigBoardPlayer[0] = null;
				bigBoardPlayer[1] = null;
				return true;
			}else {System.out.println("ERROR IN MATCHMAKING."); return false;} } // end case 19
		default:{System.out.println("Error in Matchmaking."); System.exit(0);}
		}//end switch
		}//end if(opponentType)
		System.out.println("Something wrong in matchmaking"); return false;
	}// end addPlayer
	
}
