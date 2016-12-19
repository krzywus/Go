package GoClient;

import java.util.ArrayList;

public class BargainHandler {

	/** Plansza do gry. */
	private Stone board[][];
	/** Tablica odwiedzonych pol planszy. */
	private boolean visitBoard[][];
	/** Rozmiar planszy. */
	private int gameSize;
	/** Plansza kanalow alpha. */
	private boolean alphaBoard[][];
	/** Lista do wyslania drugiemu graczowi poprzez server*/
	protected ArrayList<int[]> selectedStones;
	
/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstruktor klasy. */
	BargainHandler(Stone[][] board, int gameSize){
		this.board = board;
		this.gameSize = gameSize;
		visitBoard = new boolean[gameSize][gameSize];
		resetSelectedStones();
		selectedStones = new ArrayList<int[]>();
		resetAlphaBoard();
	}// end BargainHandler constr
	
	/** Metoda resetuje wysylane kamienie.*/
	protected void resetSelectedStones() {
		selectedStones = new ArrayList<int[]>();	
	} // end resetSelecedStones

	/** Metoda inicjuje lub resetuje plansze kanalow alpha. */
	protected void resetAlphaBoard(){
		alphaBoard = new boolean[gameSize][gameSize];
		for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){	
			alphaBoard[i][j] = false;	}}
	} // end initAlpha
	
	/** Metoda zeruje tablice wizyt. */
	private void resetVisitBoard(){
		for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){	
			visitBoard[i][j] = false;
		}}
	}//end resetVisitBoard

	/** Metoda tworzy lancuch kamieni jednego koloru */
	protected void getChain(Stone stone, ArrayList<Stone> neighbours) {
		char playerColor = stone.color;
		int posX = stone.positionX;
		int posY = stone.positionY;
		visitBoard[posX][posY] = true;
		if( posX-1 >= 0 		  && visitBoard[posX-1][posY] == false && board[posX-1][posY].color == playerColor){
			if(!neighbours.contains(board[posX-1][posY])){ 
				neighbours.add(board[posX-1][posY]);
				getChain(board[posX-1][posY], neighbours);}
		}
		if( posY-1 >= 0 		  && visitBoard[posX][posY-1] == false && board[posX][posY-1].color == playerColor){
			if(!neighbours.contains(board[posX][posY-1])){ 
				neighbours.add(board[posX][posY-1]);
				getChain(board[posX][posY-1], neighbours);}
		}
		if( posX+1 < gameSize && visitBoard[posX+1][posY] == false && board[posX+1][posY].color == playerColor){
			if(!neighbours.contains(board[posX+1][posY])){ 
				neighbours.add(board[posX+1][posY]);
				getChain(board[posX+1][posY], neighbours);}
		}
		if( posY+1 < gameSize && visitBoard[posX][posY+1] == false && board[posX][posY+1].color == playerColor){
			if(!neighbours.contains(board[posX][posY+1])){ 
				neighbours.add(board[posX][posY+1]);
				getChain(board[posX][posY+1], neighbours);}
		}
	}// end getNeightbours
	
	/** Metoda zwraca plansze kanalow alpha z zaznaczonycmi kamieniami. */
	protected boolean[][] markStones(Stone stone){
		if(stone.color != 'N'){
			int temp[] = new int[2];
			temp[0] = stone.positionX; temp[1] = stone.positionY;
			if(!alphaBoard[stone.positionX][stone.positionY]){
				selectedStones.add(temp);
			}else{
				int[] stoneToRemove = new int[2];
				for(int[] s: selectedStones){
					if(s[0] == stone.positionX && s[1] == stone.positionY)
						stoneToRemove = s;
				}
				selectedStones.remove(stoneToRemove);
			}
		}
		ArrayList<Stone> stoneChain = new ArrayList<Stone>();
		resetVisitBoard();
		stoneChain.add(stone);
		getChain(stone, stoneChain);
		for(Stone s: stoneChain){
			if(!alphaBoard[s.positionX][s.positionY]) alphaBoard[s.positionX][s.positionY] = true;
			else alphaBoard[s.positionX][s.positionY] = false;
		}
		return alphaBoard;
	}// end markStones
	
	
}
