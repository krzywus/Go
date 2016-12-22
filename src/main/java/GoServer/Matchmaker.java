package GoServer;

import GoServer.GameSession.GameSession;
/** Class join players into games. Design pattern: Singleton. */
public class Matchmaker{
	
	/** Singleton. */
	private static volatile Matchmaker instance;
	/** Tablica do laczenia klientow w pary*/
	private ClientHandler smallBoardPlayer[];
	private ClientHandler mediumBoardPlayer[];
	private ClientHandler bigBoardPlayer[];
	
	
/*-------------------------------------------------------------------------------------------------------------------*/
	
	/**Konstruktor klasy, inicjuje obiekty.*/
	private Matchmaker(){
		smallBoardPlayer	= new ClientHandler[2];
		mediumBoardPlayer 	= new ClientHandler[2];
		bigBoardPlayer 		= new ClientHandler[2];
	}// end Matchmaker constr
	
	/** Metoda implementujaca wzorzezc singleton. Zwraca instancje klasy. */
	public static Matchmaker getInstance() {
		Matchmaker result = instance;
		if (result == null) {
			synchronized(Matchmaker.class){
				result = instance;
				if(result == null){
					instance = result = new Matchmaker();
				}
			}
		}
		return result;
	}// end getInstance
	
	
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
					System.out.println("New 9x9 Game Started.");
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
					System.out.println("New 13x13 Game Started.");
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
					System.out.println("New 19x19 Game Started.");
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
