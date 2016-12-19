package GoClient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/** Plansza do gry. Obsluguje poprawnosc ruchow zglaszanych przez graczy. Rysuje kamienie na planszy. */
public class GameBoard extends JPanel {

	/** Pola do obslugi grafiki.  */
	protected Graphics2D g2d;
	protected BufferedImage boardBI, BStoneBI, alphaBStoneBI, WStoneBI, alphaWStoneBI;	
	protected Rectangle2D.Float rect;
	/** Klasa tworzaca grafike do gry. */
	GameBoardBuilder gameBuilder;
	
	/** Ustawienia planszy. */
	protected int gameSize;	// size of game {9x9, 13x13, 19x19}
	protected int stoneSize;
	private Stone BOARD[][]; /* Każde pole jest kamieniem. Pola odpowiadaja przecieciom na planszy. */
	/** Pola do obslugi zasady Ko. */
	private int koStone[];
	private boolean koMark;
	/** Pole do przechowania nowego kamienia. */
	protected int newStone[];
	
	/** Tablica kanalow alpha. */
	private boolean alphaBoard[][];
	
	/** Okno panelu. */
	private BoardFrame frame;
	
/*-------------------------------------------------------------------------------------------------------------------*/
	
	/** Konstruktor. Tworzy plansze. */
	GameBoard(BoardFrame frame, int gameSize, int opponentType){
		super();
		this.frame = frame;
		this.gameSize = gameSize;
		gameBuilder = new GameBoardBuilder(this);
		gameBuilder.setStoneSize();
		setDoubleBuffered(true);
		setFocusable(true);
		init();
	} // end GameBoard constructor

	/** Metoda inicjujaca obiekt. */
	private void init(){
		setSize(frame.windowHeight, frame.windowHeight);
		BOARD	  = new Stone[gameSize][gameSize];
		newStone = new int[2]; koStone = new int[2];
		alphaBoard = new boolean[gameSize][gameSize];
		resetAlphaBoard();
		for(int i = 0; i <  gameSize; i++){
			for(int j = 0; j <  gameSize; j++){
				BOARD[i][j] = new Stone('N', i, j);
			}
		}
		gameBuilder.drawBoard();	// narysuj plansze w buforze boardBI
		gameBuilder.drawStones();
		repaint();
	} // end init
	
	
	/** Metoda rysujaca na obiekcie. */			// MOZE wzorzec DECORATOR?
	protected void paintComponent(Graphics g){
		super.paintComponent(g);	
		g.setColor(new Color(220,179,92));			// color planszy w RGB
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(boardBI, 40, 40, null); // narysuj plansze
		drawStonesToBoard(g);
	} // end paintComponent
	
	/** Metoda sprawdza czy na danym polu znajduje sie kamien i rysuje go. */
	private void drawStonesToBoard(Graphics g){
		int translationX, translationY; 
		for(int i = 0; i <  gameSize; i++){
			for(int j = 0; j <  gameSize; j++){
				if(BOARD[i][j].color != 'N'){
					translationX = 40-(stoneSize/2)+stoneSize*i; 
					translationY = 40-(stoneSize/2)+stoneSize*j;
					// zmiany pozycyjne (korekta bledu z braku mozliwosci przesuwania o czesci dziesietne)
					if(gameSize != 13){ // dla plansz 9x9 i 19x19
						if( i > 6 ) translationX += 2; else if(i > 4) translationX += 1;
						if( j > 6 ) translationY += 2; else if(j > 4) translationY += 1;
					}else{	translationX -= i/2; translationY -= j/2;	} // dla planszy 13x13
					g.translate( translationX, translationY);	// przesuniecie grafiki w odpowiednie miejsce
										
					if( BOARD[i][j].color == 'B'){
						if(!alphaBoard[i][j]) g.drawImage(BStoneBI,0,0, null);		// rysowanie kamienia
						else 				  g.drawImage(alphaBStoneBI,0,0, null);
					}else{
						if(!alphaBoard[i][j]) g.drawImage(WStoneBI,0,0, null);		// rysowanie kamienia
						else 				  g.drawImage(alphaWStoneBI,0,0, null);
					}
					g.translate( -translationX, -translationY);	// wrocenie grafika do punktu (0,0)
				}
			}// end for j
		}//end for i
	} // end drawStonesToBoard
	
	
	/** Metoda sprawdza czy ruch jest poprawny (pole wolne, nie samobojczy, nie ko). 
	 * @return true if move is valid, false otherwise */
	protected boolean checkMoveValidity(int x, int y){
		int stonePositionX = (x-40+stoneSize/2)/(stoneSize);
		int stonePositionY = (y-40+stoneSize/2)/(stoneSize);
		if(BOARD[stonePositionX][stonePositionY].color == 'N'){
			Stone tempBoard[][] = new Stone[gameSize][gameSize]; 
			for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){	// skopiuj poprzednia plansze
				tempBoard [i][j] = BOARD[i][j];
			}}
			tempBoard[stonePositionX][stonePositionY] = new Stone(frame.playerColor, stonePositionX, stonePositionY);
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

			BOARD[stonePositionX][stonePositionY] = new Stone(frame.playerColor, stonePositionX, stonePositionY);
			newStone[0] = stonePositionX; newStone[1] = stonePositionY;
			deleteDeadStones(stonePositionX, stonePositionY);

					
			repaint();
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
			if(BOARD[stonePositionX-1][stonePositionY].color == 'N'){ koStone[0] = stonePositionX-1; koStone[1] = stonePositionY; }
			if(BOARD[stonePositionX+1][stonePositionY].color == 'N'){ koStone[0] = stonePositionX+1; koStone[1] = stonePositionY; }
			if(BOARD[stonePositionX][stonePositionY-1].color == 'N'){ koStone[0] = stonePositionX; koStone[1] = stonePositionY-1; }
			if(BOARD[stonePositionX][stonePositionY+1].color == 'N'){ koStone[0] = stonePositionX; koStone[1] = stonePositionY+1; }
		}
	} // end deleteDeadStone

	/**Metoda zwracajaca pozycje ostatniego polozonego kamienia. */
	protected int[] getBoardChange() {
		return newStone;
	}// end getBoardChange
	
	/** Metoda wykonujaca ruch przeciwnika na planszy. */
	protected void putOpponentStone(int x, int y) {
		if(frame.playerColor == 'W') BOARD[x][y]=new Stone('B', x, y);
		else  						 BOARD[x][y]=new Stone('W', x, y);
		//backupBoard();
	}// end putOpponentStone

	/** Metoda zwraca aktualna plansze. */
	protected Stone[][] getBoard(){
		return BOARD;
	}// end getBoard
	
	/** Metoda zwraca kamien na danej pozycji. */
	protected Stone getStone(int x, int y){
		int stonePositionX = (x-40+stoneSize/2)/(stoneSize);
		int stonePositionY = (y-40+stoneSize/2)/(stoneSize);
		return BOARD[stonePositionX][stonePositionY];
	}// end getStone
	
	/** Metoda resetuje kanaly alpha na planszy. */
	protected void resetAlphaBoard(){
		for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){
			this.alphaBoard[i][j] = false;
		}}
	}// end resetAlphaBoard
	
	/** Metoda zmienia kanaly alpha na planszy. */
	protected void setAlphaBoard(boolean[][] alphaBoard){
		for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){
			this.alphaBoard[i][j] = alphaBoard[i][j];
		}}
	}// end setAlphaBoard
	
		
}
