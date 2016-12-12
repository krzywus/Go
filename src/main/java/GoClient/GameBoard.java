package GoClient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/** Plansza do gry. */
public class GameBoard extends JPanel {

	/** Pola do obslugi grafiki.  */
	private final static String boardImgPath = "src/graphics/Board";
	private final static String BstoneImgPath = "src/graphics/BlackStone.png";
	private final static String WstoneImgPath = "src/graphics/WhiteStone.png";
	public Graphics2D g2d;
	protected BufferedImage boardBI, BStoneBI, WStoneBI;	
	private TexturePaint boardTexture, WStoneTexture, BStoneTexture;
	public Rectangle2D.Float rect;
	
	/** Ustawienia planszy. */
	private int gameSize;	// size of game € {9x9, 13x13, 19x19}
	private int stoneSize;
	private Stone BOARD[][]; /* Każde pole jest kamieniem. Pola, odpowiadaja przecieciom na planszy. */
	private Stone boardCopy[][]; /* Kopia planszy do pamietania poprzedniego ruchu. ( do zasady Ko ) */
	
	/** Okno panelu. */
	private BoardFrame frame;
	
/*-------------------------------------------------------------------------------------------------------------------*/
	
	/** Konstruktor. Tworzy plansze. */
	GameBoard(BoardFrame frame, int gameSize, int opponentType){
		super();
		this.frame = frame;
		this.gameSize = gameSize;
		setStoneSize();
		setDoubleBuffered(true);
		setFocusable(true);
		init();
	} // end GameBoard constructor

	/** Metoda inicjujaca obiekt. */
	private void init(){
		setSize(frame.windowHeight, frame.windowHeight);
		BOARD	  = new Stone[gameSize][gameSize];
		boardCopy = new Stone[gameSize][gameSize];
		for(int i = 0; i <  gameSize; i++){
			for(int j = 0; j <  gameSize; j++){
				BOARD[i][j] = new Stone('N', i, j, this);
				boardCopy[i][j] = new Stone('N', i, j, this);
			}
		}
		drawBoard();	// narysuj plansze w buforze boardBI
		drawStones();
		repaint();
	} // end init
	
	/** Metoda ustawia wielkosc kamieni do rysowania.*/
	private void setStoneSize(){
		if(gameSize == 9 ) stoneSize = 56;
		else if(gameSize == 13 ) stoneSize = 42;
		else if(gameSize == 19 ) stoneSize = 33;
	} // end setStoneSize
	
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
						g.drawImage(BStoneBI,0,0, null);		// rysowanie kamienia
					}else{
						g.drawImage(WStoneBI,0,0,null);
					}
					g.translate( -translationX, -translationY);	// wrocenie grafika do punktu (0,0)
				}
			}// end for j
		}//end for i
	} // end drawStonesToBoard
	
	/** Metoda rysujaca obraz w boardBI. */
	private void drawBoard(){
		rect = new Rectangle2D.Float(0,0, getWidth()-80, getHeight()-80);
		boardBI = loadImage(boardImgPath + gameSize + "x" + gameSize + ".png");
		boardTexture = new TexturePaint(boardBI, rect);
		boardBI = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		g2d = (Graphics2D) boardBI.getGraphics();					
		g2d.setPaint(boardTexture);
		g2d.fill(rect);	
	} // end init
	
	/** Metoda wczytujaca obraz. */
	private BufferedImage loadImage(String imageURL){ 
		Image objectImage = Toolkit.getDefaultToolkit().getImage(imageURL);		
		//dopoki obraz nie bedzie calkowicie zaladowany
		while(!Toolkit.getDefaultToolkit().prepareImage(objectImage, -1, -1, null)){}
		
		BufferedImage tempBI = new BufferedImage(objectImage.getWidth(null), objectImage.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);;
		tempBI.getGraphics().drawImage(objectImage, 0, 0, null);
		return tempBI;
	} // end loadImage
	
	/** Metoda tworzaca tekstury kamieni. */
	private void drawStones(){
		rect = new Rectangle2D.Float(0,0, stoneSize, stoneSize);
		WStoneBI = loadImage(WstoneImgPath);
		BStoneBI = loadImage(BstoneImgPath);
		WStoneTexture = new TexturePaint(WStoneBI, rect);
		BStoneTexture = new TexturePaint(BStoneBI, rect);
		WStoneBI = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		BStoneBI = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		g2d = (Graphics2D) WStoneBI.getGraphics();					
		g2d.setPaint(WStoneTexture);
		g2d.fill(rect);
		g2d.translate(stoneSize,stoneSize);		g2d.fill(rect);
		g2d = (Graphics2D) BStoneBI.getGraphics();					
		g2d.setPaint(BStoneTexture);
		g2d.fill(rect);		
	} // end drawStone
	
	
	/** Metoda sprawdza czy ruch jest poprawny (pole wolne + nie samobojczy). 
	 * @return true if move is valid, false otherwise
	 * TODO: implement*/
	protected boolean checkMoveValidity(int x, int y){
		int stonePositionX = (x-40+stoneSize/2)/(stoneSize);
		int stonePositionY = (y-40+stoneSize/2)/(stoneSize);
		//System.out.println(stonePositionX + " " + stonePositionY);
		if(BOARD[stonePositionX][stonePositionY].color == 'N'){
			for(int i = 0; i <  gameSize; i++){	for(int j = 0; j <  gameSize; j++){	// skopiuj poprzednia plansze
					boardCopy[i][j] = BOARD[i][j];
			}}
			BOARD[stonePositionX][stonePositionY] = new Stone(frame.playerColor, stonePositionX, stonePositionY, this);
			repaint();
			return true;
		}else return false;

	} // checkMoveValidity

	/**Metoda zwracajaca pozycje ostatniego polozonego kamienia. */
	public int[] getBoardChange() {
		int newStone[] = new int[2];
		for(int i = 0; i <  gameSize; i++){
			for(int j = 0; j <  gameSize; j++){
				if(BOARD[i][j].color != boardCopy[i][j].color){
					newStone[0] = i;
					newStone[1] = j;
		}}}
		return newStone;
	}// end getBoardChange
	
	/** Metoda wykonujaca ruch przeciwnika na planszy. */
	public void putOpponentStone(int x, int y) {
		if(frame.playerColor == 'W') BOARD[x][y]=new Stone('B', x, y, this);
		else  BOARD[x][y]=new Stone('W', x, y, this);
	}// end putOpponentStone
	
}
