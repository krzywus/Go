package GoServer.Bot;

import java.util.Arrays;

import GoClient.LibertyVisitor;
import GoClient.Stone;

public class BotGameBoard {

	
	/** Ustawienia planszy. */
	protected int gameSize;	// size of game {9x9, 13x13, 19x19}
	private Stone BOARD[][]; /* Każde pole jest kamieniem. Pola odpowiadaja przecieciom na planszy. */
	/** Pola do obslugi zasady Ko. */
	private int koStone[];
	private boolean koMark;
	/** Pole do przechowania nowego kamienia. */
	protected int newStone[];

	/** Bot*/
	private GoBot bot;
	
/*-------------------------------------------------------------------------------------------------------------------*/
	
	/** Konstruktor. Tworzy plansze. */
	BotGameBoard(GoBot bot, int gameSize, Stone[][] board){
		this.bot = bot;
		this.gameSize = gameSize;
		newStone = new int[2]; 
		koStone = new int[2];
		BOARD = new Stone[gameSize][gameSize];
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				BOARD[i][j] = board[i][j];
		}}

	} // end GameBoard constructor


	/** Metoda sprawdza czy ruch jest poprawny (pole wolne, nie samobojczy, nie ko). 
	 * @return true if move is valid, false otherwise */
	protected boolean checkMoveValidity(int stonePositionX, int stonePositionY ){
		if(BOARD[stonePositionX][stonePositionY].color == 'N'){
			Stone tempBoard[][] = new Stone[gameSize][gameSize]; 
			for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){	// skopiuj poprzednia plansze
				tempBoard [i][j] = BOARD[i][j];
			}}
			tempBoard[stonePositionX][stonePositionY] = new Stone(bot.botColor, stonePositionX, stonePositionY);
			LibertyVisitor visitor = new LibertyVisitor(tempBoard, gameSize);
			tempBoard[stonePositionX][stonePositionY].accept(visitor);;
			
			
			if(tempBoard[stonePositionX][stonePositionY].liberty == 0 ){
				if( visitor.isSuicidal(tempBoard[stonePositionX][stonePositionY]) ){
					return false;		// ruch samobojczy
				}
			}
			if(koMark){ if(stonePositionX == koStone[0] && stonePositionY == koStone[1]){
					return false;		// zasada ko, ruch niedozwolony
				}
			}

			BOARD[stonePositionX][stonePositionY] = new Stone(bot.botColor, stonePositionX, stonePositionY);
			newStone[0] = stonePositionX; newStone[1] = stonePositionY;
			deleteDeadStones(stonePositionX, stonePositionY);
				
			return true;
		}else return false;
	} // checkMoveValidity

	/** Metoda usuwa z planszy martwe kamienie oraz sprawdza czy ko jest aktywne.*/
	protected void deleteDeadStones(int stonePositionX, int stonePositionY) {
		LibertyVisitor visitor = new LibertyVisitor(BOARD, gameSize);
		int deadStonesNumber; koMark = false;
		deadStonesNumber = visitor.deleteDeadStones(stonePositionX, stonePositionY);
		if(deadStonesNumber == 1){ 
			koMark = true;
			if(stonePositionX-1 >= 0)
				if(BOARD[stonePositionX-1][stonePositionY].color == 'N'){ koStone[0] = stonePositionX-1; koStone[1] = stonePositionY; }
			if(stonePositionX+1 < gameSize)
				if(BOARD[stonePositionX+1][stonePositionY].color == 'N'){ koStone[0] = stonePositionX+1; koStone[1] = stonePositionY; }
			if(stonePositionY-1 >= 0)
				if(BOARD[stonePositionX][stonePositionY-1].color == 'N'){ koStone[0] = stonePositionX; koStone[1] = stonePositionY-1; }
			if(stonePositionY+1 < gameSize)
				if(BOARD[stonePositionX][stonePositionY+1].color == 'N'){ koStone[0] = stonePositionX; koStone[1] = stonePositionY+1; }
		}
	} // end deleteDeadStone

	/** Metoda zwracajaca pozycje ostatniego polozonego kamienia. */
	protected int[] getBoardChange() {
		return newStone;
	}// end getBoardChange
	
	/** Metoda wykonujaca ruch przeciwnika na planszy. */
	protected void putOpponentStone(int x, int y) {
		if(bot.botColor == 'W') BOARD[x][y]=new Stone('B', x, y);
		else  						 BOARD[x][y]=new Stone('W', x, y);
	}// end putOpponentStone

	/** Metoda zwraca aktualna plansze. */
	protected Stone[][] getBoard(){
		return Arrays.copyOf( BOARD , BOARD.length);
	}// end getBoard
	
	/** Metoda zwraca kamien na danej pozycji. */
	protected Stone getStoneFromBoard(int x, int y){
		return BOARD[x][y];
	}// end getStone
	

}
