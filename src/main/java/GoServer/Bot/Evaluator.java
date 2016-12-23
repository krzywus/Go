package GoServer.Bot;

import GoClient.LibertyVisitor;
import GoClient.Stone;

/** Class with main algorithm to evaluate fields for bots. */
public class Evaluator {

	private Strategy strategy;
	private GoBot bot;
	private char botColor;
	private int gameSize;
	private Stone[][] board;
	private int[][] libertyBoard;
	
	Evaluator(GoBot bot){
		this.bot = bot;
		this.strategy = bot.currentStrategy;
		this.botColor = bot.botColor;
		this.gameSize = bot.gameSize;
		this.board = bot.getBoard();
		libertyBoard = new int[gameSize][gameSize];
		setLibertyBoard();
	}// end Evaluator constr
	
	/** Adds value based on diagonal stones owned by bot. */
	public void setDiagonalMovesValue(int x, int y) {
		int diagonalNeighbours = getDiagonalNeighbours(x, y, botColor);
		strategy.valuesBoard[x][y] += diagonalNeighbours * bot.DiagonalMoveValue;
	}// end setDiagonalMovesValue

	/** Adds value based on nearby stones owned by bot. */
	public void setDefenceStonesValue(int x, int y) {
		int stickingNeighbours = getStickingNeighbours(x, y, botColor);
		int diagonalNeighbours = getDiagonalNeighbours(x, y, botColor);
		if(stickingNeighbours == 4) strategy.valuesBoard[x][y] += bot.DefenceValue * 2;
		if(stickingNeighbours  > 2) strategy.valuesBoard[x][y] += bot.DefenceValue;
		if(diagonalNeighbours == 4) strategy.valuesBoard[x][y] += bot.DefenceValue * 3;
		if(diagonalNeighbours  > 2) strategy.valuesBoard[x][y] += bot.DefenceValue * 2;
	}// end setDefenceStonesValues

	
	public void setKillingStonesValue(int x, int y) {
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				if(board[i][j].liberty == 1){
					int killingStonePosition[] = getKillingPosition(i , j);
				}
		}}
	}

	/** Adds value based on enemy stones that can be attacked. 
	 * TODO: uzaleznic wartosc od dlugosci lancucha kamieni przeciwnika (atak na slabe grupy)*/
	public void setAttackStonesValue(int x, int y) {
		char enemyColor;
		if(botColor == 'W') enemyColor = 'B';
		else enemyColor = 'W';
		int stickingNeighbours = getStickingNeighbours(x, y, enemyColor);
		int diagonalNeighbours = getDiagonalNeighbours(x, y, enemyColor);
		if(stickingNeighbours < 3) strategy.valuesBoard[x][y] += bot.AttackValue * stickingNeighbours;
		if(diagonalNeighbours == 1) strategy.valuesBoard[x][y] += bot.AttackValue * 3;
		if(diagonalNeighbours == 2) strategy.valuesBoard[x][y] += bot.AttackValue * 2;	
	}

	/** Adds value based on sticking stones owned by bot. */
	public void setStickingStonesValue(int x, int y) {
		int stickingNeighbours = getStickingNeighbours(x, y, botColor);
		strategy.valuesBoard[x][y] += stickingNeighbours * bot.StickingStoneValue;
	} // end setStickingStonesValue

	/** Adds value to start points as its generaly good movement. */
	public void setStarPointsValue() {
		if(gameSize != 19){
			strategy.valuesBoard[2][2] += bot.StarPointValue;
			strategy.valuesBoard[2][gameSize-3] += bot.StarPointValue;
			strategy.valuesBoard[gameSize-3][2] += bot.StarPointValue;
			strategy.valuesBoard[gameSize-3][gameSize-3] += bot.StarPointValue;
			//punkt srodkowy
			if(gameSize == 9) strategy.valuesBoard[4][4] += bot.StarPointValue;
			else			  strategy.valuesBoard[6][6] += bot.StarPointValue;
		}else{
			strategy.valuesBoard[3][3] 					 += bot.StarPointValue;
			strategy.valuesBoard[3][gameSize-4]			 += bot.StarPointValue;
			strategy.valuesBoard[gameSize-4][3] 		 += bot.StarPointValue;
			strategy.valuesBoard[gameSize-4][gameSize-4] += bot.StarPointValue;
			//punkty srodkowe przy krawedziach
			strategy.valuesBoard[3][9] 			+= bot.StarPointValue;				
			strategy.valuesBoard[gameSize-4][9] += bot.StarPointValue;
			strategy.valuesBoard[9][3] 			+= bot.StarPointValue;
			strategy.valuesBoard[9][gameSize-4] += bot.StarPointValue;
			//punkt srodkowy
			strategy.valuesBoard[9][9] += bot.StarPointValue;
		}		
	}// end setStarPointsValue

	/** Adds value based on distance to nearby stones with specified color. */
	public void setLongMoveValue(int x, int y) {
		int secondTierNeighboursInColor = getSecondTierNeighbours(x,y,botColor);
		int stickingNeighboursInColor = getStickingNeighbours(x, y, botColor);
		int diagonalNeighboursInColor = getDiagonalNeighbours(x, y, botColor);
		char enemyColor;
		if(botColor == 'W') enemyColor = 'B';
		else enemyColor = 'W';
		int secondTierNeighboursInEnemyColor = getSecondTierNeighbours(x, y, enemyColor);
		int stickingNeighboursInEnemyColor = getStickingNeighbours(x, y, enemyColor);
		int diagonalNeighboursInEnemyColor = getDiagonalNeighbours(x, y, enemyColor);
		
		int value = ((secondTierNeighboursInColor-stickingNeighboursInColor-diagonalNeighboursInColor)+
				(secondTierNeighboursInEnemyColor-stickingNeighboursInEnemyColor-diagonalNeighboursInEnemyColor));
		if(value > 0)
			strategy.valuesBoard[x][y] += value * bot.LongMoveValue;
	} // end setStickingStonesValue
	
	
	/**Funkcje pomocnicze: */
	
	/** Metoda zwraca liczbe kamieni danego koloru dotykajacych kamienia na danej pozycji.*/
	public int getStickingNeighbours(int x, int y, char color){
		int ret = 0;
		if(x-1 >= 0) 	   if(board[x-1][y].color == color) ret++;
		if(x+1 < gameSize) if(board[x+1][y].color == color) ret++;
		if(y-1 >= 0) 	   if(board[x][y-1].color == color) ret++;
		if(y+1 < gameSize) if(board[x][y+1].color == color) ret++;
		return ret;
	}// end getStickingNeighbours
	
	/** Metoda zwraca liczbe kamieni danego koloru na skos(jedno pole) od danej pozycji.*/
	public int getDiagonalNeighbours(int x, int y, char color){
		int ret = 0;
		if(x-1 >= 0 && y-1 >= 0) 			 if(board[x-1][y-1].color == color) ret++;
		if(x+1 < gameSize && y-1 >= 0) 		 if(board[x+1][y-1].color == color) ret++;
		if(x-1 >= 0 && y+1 < gameSize ) 	 if(board[x-1][y+1].color == color) 	ret++;
		if(x+1 < gameSize && y+1 < gameSize) if(board[x+1][y+1].color == color) 	ret++;
		return ret;
	}// end getDiagonalNeighbours
	
	/** Metoda zwraca liczbe kamieni danego koloru w wiekszej odleglosci (2-3 pola) od danej pozycji.*/
	public int getSecondTierNeighbours(int x, int y, char color){
		int ret = 0;
		/* poruszanie sie po kwadracie [(x-2,y-2),(x+2,y-2),(x+2,y+2),(x-2,y+2)]
		   bez wierzcholkow*/
		int posX = x-1; int posY = y-2;
		for(int i = 0; i < 3; i++)
			if(posX+i >= 0 && posX+i < gameSize && posY >= 0 && posY < gameSize)
				if(board[posX+i][posY].color == color) ret++;
		posX = x+2; posY = y-1;
		for(int i = 0; i < 3; i++)
			if(posX >= 0 && posX < gameSize && posY+i >= 0 && posY+i < gameSize)
				if(board[posX][posY+i].color == color) ret++;
		posX = x+1; posY = y+2;
		for(int i = 0; i < 3; i++)
			if(posX-i >= 0 && posX-i < gameSize && posY >= 0 && posY < gameSize)
				if(board[posX-i][posY].color == color) ret++;
		posX = x-2; posY = y+1;
		for(int i = 0; i < 3; i++)
			if(posX >= 0 && posX < gameSize && posY-i >= 0 && posY-i < gameSize)
				if(board[posX][posY-i].color == color) ret++;
		// dodanie 4 sasiadow oddalonych o 3 na tych samych koordynatach
		if(x-3 >= 0 ) if(board[x-3][y].color == color) ret++;
		if(y-3 >= 0 ) if(board[x][y-3].color == color) ret++;
		if(x+3 < gameSize ) if(board[x+3][y].color == color) ret++;
		if(y+3 < gameSize ) if(board[x][y+3].color == color) ret++;
		return ret;
	}// end getDiagonalNeighbours
			
	/** Metoda zwraca pole ktore zabiera ostatni oddech grupie kamieni. */
	private int[] getKillingPosition(int x, int y){
		int position[] = new int[2];
		// TODO:
		return position;
	}
	
	/** Metoda oblicza oddechy kazdego kamienia na mapie.*/
	private void setLibertyBoard(){
		LibertyVisitor visitor = new LibertyVisitor(board, gameSize);
		for(int i = 0; i < gameSize; i++){
			for(int j = 0; j < gameSize; j++){
				if(board[i][j].color != 'N'){
					board[i][j].accept(visitor);
					libertyBoard[i][j] = board[i][j].liberty;
				}
		}}
	}



	
}
