package GoServer.GameSession;

import java.util.Random;

import GoServer.ClientHandler;

/** Klasa odpowiedzialna za blokowanie i udostepnianie okien graczy.
 * Implementuje wzorzec projektowy State. Mozliwe stany: 
 * 		GameAborted - gracz ktory wyszedl wraca do mainMenu, drugi wygrywa z info
 * 		WhiteToMove - gracz bialy wykonuje ruch, okno czarnego zablokowane.
 * 		BlackToMove - gracz czarny wykonuje ruch, okno bialego zablokowane.
 * 		GameFinished - gracze otrzymuja informacje o wyniku rozgrywki, plansze zablokowane. 
 * 		territoryBargain - gracze targuja sie o terytorium.  */
public class GameSession {
	
	private static Random RAND = new Random();
	/** Pole definiiujace obecny stan gry. */
	private GameSessionStateBehavior state;
	/** Klienci grajacy na danej planszy. */
	protected ClientHandler black;
	protected ClientHandler white;
	/** Pola do przechowania informacji o pasowaniu graczy. */
	private boolean whitePass, blackPass;

/*-------------------------------------------------------------------------------------------------------------------*/

	/** Publiczny konstruktor klasy */
	public GameSession(ClientHandler player_one, ClientHandler player_two){
		decideSides(player_one, player_two);
		setState(GameSessionState.BlackToMove);
		startGame();
	}// end GameSession constructor
	
	/** Metoda startuje gry w dla obu klientow - dla pierwszego odblokowuje plansze, dla drugiego uruchamia.*/
	private void startGame(){
		if(black != null && white !=null){
			black.startGame(white, 'B');
			white.startGame(black, 'W');
		}else{
			if(black == null){
				white.enemyLeftMatchmaking();
			}else{
				black.enemyLeftMatchmaking();
			}
		}
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
	}// end setState
	
	/** Metoda zwraca stan gry. 
	 * @return aktualny stan gry*/
	public GameSessionState getState(){
		return this.state.getState();
	}// end setState
	
	/** Metoda zwraca stan gry. 
	 * @return aktualny stan gry*/
	public GameSessionStateBehavior getStateBehavior(){
		return this.state.getState().getStateBehavior();
	}// end setState
	
	/** Metoda do obslugi komunikacji miedzy klientami.
	 *  Update planszy, blokowanie planszy itd. */
	public void doAction(String command){
		//System.out.println(command);//
		if(state.getState() == GameSessionState.BlackToMove){
			blackMoved(command);
		}else if(state.getState() == GameSessionState.WhiteToMove){
			whiteMoved(command);
		}else if(state.getState() == GameSessionState.TerritoryBargain){
			territoryBargain(command);
		}
	}//end doAction
	
	
	/** Metoda wykonuje akcje po ruchu czarnego gracza. */
	private void blackMoved(String command){
		if(command.contains("MOVE")){
			blackPass = false;
			int posXIndex = command.indexOf("POSX:");
			String posX = command.substring(posXIndex+5, posXIndex+5+2) ;
			int posYIndex = command.indexOf("POSY:");
			String posY = command.substring(posYIndex+5, posYIndex+5+2) ;
			String blackExecute = "GAME DISABLE BOARD";
			String whiteExecute = "GAME ENABLE BOARD NEWSTONE POSX:" + posX + " POSY:" + posY + " ";
			if(command.contains("KILL") ) whiteExecute += "KILL ";
			black.out.println(blackExecute);
			white.out.println(whiteExecute);
		}else if(command.contains("PASS")){
			blackPass = true;
			if(blackPass && whitePass){ // gra zakonczona
				startBargain();
			}else{
				white.out.println("GAME OPPONENT PASS");
			}
		}
		state = state.blackMoved();
	}// end blackMoved
	
	/** Metoda wykonuje akcje po ruchu bialego gracza. */
	private void whiteMoved(String command){
		if(command.contains("MOVE")){
			whitePass = false;
			int posXIndex = command.indexOf("POSX:");
			String posX = command.substring(posXIndex+5, posXIndex+5+2) ;
			int posYIndex = command.indexOf("POSY:");
			String posY = command.substring(posYIndex+5, posYIndex+5+2) ;
			String whiteExecute = "GAME DISABLE BOARD";
			String blackExecute = "GAME ENABLE BOARD NEWSTONE POSX:" + posX + " POSY:" + posY + " ";
			if(command.contains("KILL") ) blackExecute += "KILL ";
			white.out.println(whiteExecute);
			black.out.println(blackExecute);
			state = state.whiteMoved();
		}else if(command.contains("PASS")){
			whitePass = true;
			if(blackPass && whitePass){ // gra zakonczona
				startBargain();
			}else{
				black.out.println("GAME OPPONENT PASS");
			}
		}
		state = state.whiteMoved();
	}// end whiteMoved
	
	/** Metoda przerywa gre na żadanie jednego z graczy. */
	public void abortGame(ClientHandler abortingPlayer){
		state = state.gameAborted();
		String execute = "GAME ABORTED";
		if(abortingPlayer == white){ 
			white.setGameSession(null); 
			white = null;
			if(black != null) black.out.println(execute);
		}else if(abortingPlayer == black){ 
			black.setGameSession(null); 
			black = null;
			if(white != null) white.out.println(execute);
		}
	}// end abortGame
		
	/** Metoda przesyla graczowi ktory pozostal w grze informacje o wyjsciu z gry przeciwnika. */
	private void gameFinished(){
		state = state.gameFinished();
		String execute = "GAME FINISH";
		black.out.println(execute);
		white.out.println(execute);
	}// end gameFinished
	
	/** Metoda przesyla graczowi ktory pozostal w grze informacje o wyjsciu z gry przeciwnika. */
	private void startBargain(){
		state = state.territoryBargain();
		String execute = "GAME BARGAIN START";
		black.out.println(execute + " MARK");
		white.out.println(execute + " WAIT");
	}// end bstartBargain
	
	/** Metoda wykonuje akcje zwiazane z targowaniem, czeka az gracze ustala terytorium. 
	 * "GAME SEND BARGAIN " "GAME ACCEPT BARGAIN"; "GAME DECLINE BARGAIN"; + boardFrame.playerColor in all */
	private void territoryBargain(String command){
		if(command.contains("SEND")){
			String selectedStones = command.substring(18);
			if(command.contains("W")){
				String whiteExecute = "GAME BARGAIN WAIT";
				String blackExecute = "GAME BARGAIN CHOOSE";
				blackExecute += selectedStones;
				white.out.println(whiteExecute);
				black.out.println(blackExecute);
			}else{
				String blackExecute = "GAME BARGAIN WAIT";
				String whiteExecute = "GAME BARGAIN CHOOSE";
				whiteExecute += selectedStones;
				white.out.println(whiteExecute);
				black.out.println(blackExecute);
			}
		}else if(command.contains("ACCEPT")){
			gameFinished();
		}else{ //decline
			if(command.contains("W")){
				String blackExecute = "GAME BARGAIN DECLINE WAIT";
				String whiteExecute = "GAME BARGAIN MARK";
				white.out.println(whiteExecute);
				black.out.println(blackExecute);
			}else{
				String blackExecute = "GAME BARGAIN MARK";
				String whiteExecute = "GAME BARGAIN DECLINE WAIT";
				white.out.println(whiteExecute);
				black.out.println(blackExecute);
			}
		}
	}// end territoryBargain
	
}
