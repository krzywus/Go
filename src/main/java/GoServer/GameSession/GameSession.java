package GoServer.GameSession;

import java.util.Random;

import GoServer.ClientHandler;

/** Klasa odpowiedzialna za blokowanie i udostepnianie okien graczy.
 *	TODO: nie wiem czy nie powinna byc watkiem.?
 * Implementuje wzorzec state - mozliwe stany: 
 * 		GameAborted - gracz ktory wyszedl wraca do mainMenu, drugi wygrywa z info
 * 		WhiteToMove - gracz bialy wykonuje ruch, okno czarnego zablokowane.
 * 		BlackToMove - gracz czarny wykonuje ruch, okno bialego zablokowane.
 * 		GameFinished - gracze otrzymuja informacje o wyniku rozgrywki, plansze zablokowane.  */
public class GameSession {
	
	private static Random RAND = new Random();
	/** Pole definiiujace obecny stan gry. */
	private GameSessionStateBehavior state;
	/** Klienci grajacy na danej planszy. */
	protected ClientHandler black;
	protected ClientHandler white;

/*-------------------------------------------------------------------------------------------------------------------*/

	/** Publiczny konstruktor klasy */
	public GameSession(ClientHandler player_one, ClientHandler player_two){
		decideSides(player_one, player_two);
		setState(GameSessionState.BlackToMove);
		startGame();
	}// end GameSession constructor
	
	/** Metoda startuje gry w dla obu klientow - dla pierwszego odblokowuje plansze, dla drugiego uruchamia.*/
	private void startGame(){
		black.startGame(white, 'B');
		white.startGame(black, 'W');
	}// end startGame
	
	/** Metoda losuje, ktory gracz zaczyna jako czarny a ktory jako bialy.
	 * Dodatkowo ustawia klientom aktywna sesje gry. */
	private void decideSides(ClientHandler player_one, ClientHandler player_two){
		if(RAND.nextInt(2) == 0){
			white =  player_two; black =  player_one; 
			black.setGameSession(this); white.setGameSession(this);
		}else{
			black =  player_two; white =  player_one; 
			black.setGameSession(this); white.setGameSession(this);}
	}// end decideSides
	
	/** Metoda ustawia stan gry. */
	public void setState(GameSessionState newState){
		this.state = newState.getStateBehavior();
	}
	
}
