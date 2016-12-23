package GoServer.Bot;

import java.util.ArrayList;
import java.util.Random;

import GoClient.Stone;

public class GoBot {

	/** Pola do przechowywania informacji o planszy. */
	private Stone[][] board;
	private int[][] libertyBoard;
	protected char botColor;
	protected int gameSize;
	
	/** Pola wartosci dla kamieni w poszczegolnych pozycjach. 
	 *  Kazda wartosc: 1...10*/
	protected int StarPointValue;
	protected int StickingStoneValue;
	protected int AttackValue;
	protected int DefenceValue;
	protected int KillingStoneValue;
	protected int DiagonalMoveValue;
	protected int LongMoveValue;

	/** Pola do przechowywania informacji o planszy. */
	private int[][] valuesBoard;
	/** Aktualnie wybrana strategia. */
	protected Strategy currentStrategy;
	/** Generator losowosci. */
	private static Random rand;
	
/*-------------------------------------------------------------------------------------------------------------------*/

	GoBot(int gameSize, char botColor){
		this.gameSize = gameSize;
		this.botColor = botColor;
		rand = new Random();
		init();
	} // ned GoBot constr
	
	private void init(){
		board = new Stone[gameSize][gameSize];
		libertyBoard = new int[gameSize][gameSize];
		valuesBoard = new int[gameSize][gameSize];
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				board[i][j] = new Stone('N', j, j);
				libertyBoard[i][j] = 0;
				valuesBoard[i][j] = 0;
		}}
		/**TODO: delete*/
		int a = 2; int b = 2;
		board[a][b] = new Stone('W', a, b); 
		a = 2;  b = 3;
		board[a][b] = new Stone('W', a, b); 
		a = 3;  b = 3;
		board[a][b] = new Stone('W', a, b); 
		a = 2;  b = 6;
		board[a][b] = new Stone('W', a, b); 
		a = 5;  b = 6;
		board[a][b] = new Stone('W', a, b); 
		a = 6;  b = 6;
		board[a][b] = new Stone('B', a, b); 
		a = 6;  b = 8;
		board[a][b] = new Stone('B', a, b); 
		a = 5;  b = 7;
		board[a][b] = new Stone('B', a, b); 
		a = 7;  b = 7;
		board[a][b] = new Stone('B', a, b); 
	}
	
	/** Method chooses strategy for current move. */
	private void chooseStrategy(){
		currentStrategy = new TerritoryGainStrategy(this);
		currentStrategy.evaluateFields();
	}// end chooseStrategy
	
	/** Method decides where to move.*/
	protected void makeMove(){
		chooseStrategy();
		removeSuicidalMoves();	
		int move[] = chooseMove();
		board[move[0]][move[1]] = new Stone('X', move[0], move[1]);
	}// end makeMove
	

	/** Do testowania algorytmu bota*/
	protected void simulation(){
		chooseStrategy();
		removeSuicidalMoves();	
		System.out.println("Stones board ( by color): ");
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				System.out.print(board[i][j].color + " ");
		}System.out.println();}
		System.out.println("Values board: ");
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				System.out.print(valuesBoard[i][j] + " ");
				if(valuesBoard[i][j] < 10) System.out.print(" ");
		}System.out.println();}
		
		int move[] = chooseMove();	
		
		System.out.println("Choosed move: " + move[0] + " " + move[1]); 
		
		board[move[0]][move[1]] = new Stone('X', move[0], move[1]);
		
		System.out.println();

		System.out.println("Stones board with marked movement by X: ");
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				System.out.print(board[i][j].color + " ");
		}System.out.println();}
	} // end simulation
	
	/** Do testowania algorytmu bota*/
	public static void main(String[] args) {
		GoBot bot = new GoBot(9, 'W');
		bot.simulation();
	}// end main
	
	/** Method removes from valuesBoard suicidal or ko rule moves. */
	private void removeSuicidalMoves() {
		BotGameBoard gameBoard = new BotGameBoard(this, gameSize, board);
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				if( !gameBoard.checkMoveValidity(i, j) ) {
					valuesBoard[i][j] = 0;
				}
				gameBoard = new BotGameBoard(this, gameSize, board);
		}}
	}// end removeSuicidalMoves

	/** Method chooses field to place stone in this turn.*/
	private int[] chooseMove(){
		int ret[] = new int[2];
		int max = 0;
		for(int i = 0; i < gameSize; i++){
			for(int j =0 ; j < gameSize; j++){
				if( valuesBoard[i][j] > max) max = valuesBoard[i][j];
			}
		}
		ArrayList<int[]> bigValueList = new ArrayList<int[]>();
		if (max > 0){
		int margin = max - max/5;
		if ( margin < 1) margin = 1;
		for(int i = 0; i < gameSize; i++){
			for(int j =0 ; j < gameSize; j++){
				if( valuesBoard[i][j] - margin > 0) {
					int newValue[] = new int[2];
					newValue[0] = i; newValue[1] = j;
					bigValueList.add(newValue);
				}
			}
		}
		int listSize = bigValueList.size();

		System.out.println("Highest value fields: ");
		for(int[] val: bigValueList){
			System.out.println(val[0] + " " + val[1] + "  val: " + valuesBoard[val[0]][val[1]]);
		}
		ret = bigValueList.get(rand.nextInt(listSize));
		}//end if max > 0
		
		return ret;
	}// end chooseMove
	
	/** Getter for board. */
	public Stone[][] getBoard() {
		return this.board;
	}// end getBoard
	
	/** Getter for libertyBoard. */
	public int[][] getLibertyBoard() {
		return this.libertyBoard;
	}// end getLibertyBoard

	/** Setter for board. */
	public void setBoard(Stone[][] board) {
		this.board = board;
	}// end setBoard

	/** Setter for libertyBoard. */
	public void setLibertyBoard(int[][] libertyBoard) {
		this.libertyBoard = libertyBoard;
	} // end setLibertyBoard


	/** Setter for valuesBoard. */
	public void setValuesBoard(int[][] valuesBoard) {
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				this.valuesBoard[i][j] = valuesBoard[i][j];
		}}		
	}// end setValuesBoard
	
}
