package GoClient;

import java.util.ArrayList;

/** Class counting liberties of stones. Design Pattern: Visitor*/
public class LibertyVisitor {

	/** Aktualna plansza. */
	private Stone board[][];
	/** Tablica z informacja ktore pola byly odwiedzane przy liczeniu oddechow. */
	private boolean visitBoard[][];
	/** Wielkosc planszy. */
	private int gameSize;
	
	
/*-------------------------------------------------------------------------------------------------------------------*/
	
	LibertyVisitor(Stone board[][], int gameSize){
		this.board = board;
		this.gameSize = gameSize;
		visitBoard = new boolean[gameSize][gameSize];
		resetVisitBoard();
	}//end LibertyVisitor constr

	/** Metoda zeruje tablice wizyt. */
	private void resetVisitBoard(){
		for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){	
			visitBoard[i][j] = false;
		}}
	}//end resetVisitBoard
	
	
	/** Metoda liczaca ile oddechow ma dany kamien. */
	public synchronized void visit(Stone stone){
		ArrayList<Stone> stoneChain = new ArrayList<Stone>();
		resetVisitBoard();
		stoneChain.add(stone);
		getNeighbours(stone, stoneChain);
		resetVisitBoard();
		int chainLiberty = getLiberty(stoneChain);
		resetVisitBoard();
		for(Stone s: stoneChain){
			s.liberty = chainLiberty;
		}
	} // end visit
	

	/** Metoda tworzy lancuch kamieni jednego koloru */
	private synchronized void getNeighbours(Stone stone, ArrayList<Stone> neighbours) {
		char playerColor = stone.color;
		int posX = stone.positionX;
		int posY = stone.positionY;
		visitBoard[posX][posY] = true;
		if( posX-1 >= 0 		  && visitBoard[posX-1][posY] == false && board[posX-1][posY].color == playerColor){
			if(!neighbours.contains(board[posX-1][posY])){ 
				neighbours.add(board[posX-1][posY]);
				getNeighbours(board[posX-1][posY], neighbours);}
		}
		if( posY-1 >= 0 		  && visitBoard[posX][posY-1] == false && board[posX][posY-1].color == playerColor){
			if(!neighbours.contains(board[posX][posY-1])){ 
				neighbours.add(board[posX][posY-1]);
				getNeighbours(board[posX][posY-1], neighbours);}
		}
		if( posX+1 < gameSize && visitBoard[posX+1][posY] == false && board[posX+1][posY].color == playerColor){
			if(!neighbours.contains(board[posX+1][posY])){ 
				neighbours.add(board[posX+1][posY]);
				getNeighbours(board[posX+1][posY], neighbours);}
		}
		if( posY+1 < gameSize && visitBoard[posX][posY+1] == false && board[posX][posY+1].color == playerColor){
			if(!neighbours.contains(board[posX][posY+1])){ 
				neighbours.add(board[posX][posY+1]);
				getNeighbours(board[posX][posY+1], neighbours);}
		}
	}// end getNeightbours

	/** Metoda zlicza oddechy grupy kamieni. */
	private int getLiberty(ArrayList<Stone> stoneChain) {
		int liberty = 0;
		for(Stone stone: stoneChain){
			int posX = stone.positionX;
			int posY = stone.positionY;
			if( posX-1 >= 0 		  && visitBoard[posX-1][posY] == false && board[posX-1][posY].color == 'N'){
				visitBoard[posX-1][posY] = true;
				liberty++;
			} 
			if( posY-1 >= 0 		  && visitBoard[posX][posY-1] == false && board[posX][posY-1].color == 'N'){
				visitBoard[posX][posY-1] = true;
				liberty++;	
			}
			if( posX+1 < gameSize && visitBoard[posX+1][posY] == false && board[posX+1][posY].color == 'N'){
				visitBoard[posX+1][posY] = true;
				liberty++;
			}
			if( posY+1 < gameSize && visitBoard[posX][posY+1] == false && board[posX][posY+1].color == 'N'){
				visitBoard[posX][posY+1] = true;
				liberty++;
			}
		}
		return liberty;
	} // end getLiberty
	
	/** Metoda sprawdza czy ruch samobojczy jest dozwolony - czy kamien zabiera ostatni oddech innemu.
	 * TODO: gdyby cos nie dzialalo, sprobowac z zakomentowanymi*/
	public boolean isSuicidal(Stone stone){
		int posX = stone.positionX;
		int posY = stone.positionY;
		char playerColor = stone.color;
		if(posX-1 >= 0 && board[posX-1][posY].color != playerColor){
			/*Stone tempBoard[][] = getTempBoard(); 
			tempBoard[posX][posY].color = playerColor;
			LibertyVisitor visitor = new LibertyVisitor(tempBoard, gameSize);
			tempBoard[posX-1][posY].accept(visitor);
			if(tempBoard[posX-1][posY].liberty == 0) return false;*/
			board[posX-1][posY].accept(this); // kamien w lewo
			if(board[posX-1][posY].liberty == 0) return false;
		}
		if(posY-1 >= 0 && board[posX][posY-1].color != playerColor){
			/*Stone tempBoard[][] = getTempBoard(); 
			tempBoard[posX][posY].color = playerColor;
			LibertyVisitor visitor = new LibertyVisitor(tempBoard, gameSize);
			tempBoard[posX][posY-1].accept(visitor);
			if(tempBoard[posX][posY-1].liberty == 0) return false;*/
			board[posX][posY-1].accept(this);  //kamien w gore
			if(board[posX][posY-1].liberty == 0) return false;
		}
		if(posX+1 < gameSize && board[posX+1][posY].color != playerColor){
			/*Stone tempBoard[][] = getTempBoard(); 
			tempBoard[posX][posY].color = playerColor;
			LibertyVisitor visitor = new LibertyVisitor(tempBoard, gameSize);
			tempBoard[posX+1][posY].accept(visitor);
			if(tempBoard[posX+1][posY].liberty == 0) return false;*/
			board[posX+1][posY].accept(this); ; //kamien w prawo
			if(board[posX+1][posY].liberty == 0) return false;
		}
		if(posY+1 < gameSize && board[posX][posY+1].color != playerColor){
			/*Stone tempBoard[][] = getTempBoard(); 
			tempBoard[posX][posY].color = playerColor;
			LibertyVisitor visitor = new LibertyVisitor(tempBoard, gameSize);
			tempBoard[posX][posY+1].accept(visitor);
			if(tempBoard[posX][posY+1].liberty == 0) return false;*/
			board[posX][posY+1].accept(this);  //kamien w dol
			if(board[posX][posY+1].liberty == 0) return false;
		}
		return true;	
	}// end is Suicidal
	
	/** Metoda usuwa martwe kamienie z planszy. */
	public int deleteDeadStones(int newStonePositionX, int newStonePositionY){
		int retVal = 0;
		boolean deadVisitBoard[][] = new boolean[gameSize][gameSize];
		for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){	
			deadVisitBoard[i][j] = false;
		}}
		deadVisitBoard[newStonePositionX][newStonePositionY] = true; // nowy kamien moze miec 0 oddechow, ale moze zabijac inne i zyskac oddech
		for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){	// dla kazdego pola na planszy
			if(!deadVisitBoard[i][j]){
				deadVisitBoard[i][j] = true;
				if(board[i][j].color != 'N'){
					Stone tempBoard[][] = getTempBoard(); 
					LibertyVisitor visitor = new LibertyVisitor(tempBoard, gameSize);
					tempBoard[i][j].accept(visitor);
						
					if(tempBoard[i][j].liberty == 0){
						ArrayList<Stone> stoneChain = new ArrayList<Stone>();
						stoneChain.add(board[i][j]);
						resetVisitBoard();
						getNeighbours(board[i][j], stoneChain);
						retVal = stoneChain.size();
						for(Stone s: stoneChain){
							s.color = 'N';
							s.liberty = -1;
						}//end for each
					}
				}//end if color
			}
		}}//end for i,j
		return retVal;
	}//end deleteDeadStones
	
	
	/** Metoda tworzy tymczasowa kopie planszy. */
	private Stone[][] getTempBoard(){
		Stone tempBoard[][] = new Stone[gameSize][gameSize]; 
		for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){	// skopiuj poprzednia plansze
			tempBoard [i][j] = board[i][j];
		}}
		return tempBoard;
	}// end getTempBoard
	
}
