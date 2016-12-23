package GoServer.Bot;

import GoClient.Stone;

public abstract class Strategy {

	/** Pole bota. */
	protected GoBot bot;

	/** Pola do przechowywania informacji o planszy. */
	protected Stone[][] board;
	protected int[][] libertyBoard;
	protected int gameSize;
	protected char botColor;
	/** Tablica przechowujaca wartosci kazdego pola. */
	protected int[][] valuesBoard = null;
	
/*-------------------------------------------------------------------------------------------------------------------*/

	Strategy(GoBot bot){
		this.bot = bot;
		this.gameSize = bot.gameSize;
		this.botColor = bot.botColor;
		valuesBoard = new int[gameSize][gameSize];
		setValues();
	}// end Strategy constr
	
	
	abstract void setValues();
	
	/** Method gives every game field value based on current strategy.*/
	public void evaluateFields() {
		this.board = bot.getBoard();
		this.libertyBoard = bot.getLibertyBoard();
		resetValuesBoard();
		Evaluator eval = new Evaluator(bot);
		eval.setStarPointsValue();
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				if(board[i][j].color != 'N') valuesBoard[i][j] = 0;
				else{
					eval.setStickingStonesValue(i, j);
					eval.setAttackStonesValue(i, j);
					eval.setKillingStonesValue(i, j);
					eval.setDefenceStonesValue(i, j);
					eval.setDiagonalMovesValue(i, j);
					eval.setLongMoveValue(i, j);
				}
			}
		}
		bot.setValuesBoard(this.valuesBoard);
	}// endevaluateFields
	
	public void resetValuesBoard() {
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				valuesBoard[i][j] = 0;
		}}
	}//end resetValuesBoard
}
