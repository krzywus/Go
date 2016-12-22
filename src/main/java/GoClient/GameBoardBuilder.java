package GoClient;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/** Klasa tworzy grafike dla klasy GameBoard. */
public class GameBoardBuilder {

	/** Pola do obslugi grafiki.  */
	private final static String boardImgPath = "src/graphics/Board";
	private final static String BstoneImgPath = "src/graphics/BlackStone.png";
	private final static String WstoneImgPath = "src/graphics/WhiteStone.png";
	private TexturePaint boardTexture, WStoneTexture, BStoneTexture;	
	private Rectangle2D.Float rect;
	
	/** Plansza do gry. */
	private GameBoard board;
/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstruktor klasy*/
	GameBoardBuilder(GameBoard board){
		this.board = board;
		setStoneSize();		
		drawBoard();	
		drawStones();
	}//end GameBoardBuilder constr
	
	/** Metoda ustawia wielkosc kamieni do rysowania.*/
	private void setStoneSize(){
		if(board.gameSize == 9 ) board.stoneSize = 56;
		else if(board.gameSize == 13 ) board.stoneSize = 42;
		else if(board.gameSize == 19 ) board.stoneSize = 33;
	} // end setStoneSize
	
	
	/** Metoda wczytujaca obraz. */
	private BufferedImage loadImage(String imageURL){ 
		Image objectImage = Toolkit.getDefaultToolkit().getImage(imageURL);		
		//dopoki obraz nie bedzie calkowicie zaladowany
		while(!Toolkit.getDefaultToolkit().prepareImage(objectImage, -1, -1, null)){}
		
		BufferedImage tempBI = new BufferedImage(objectImage.getWidth(null), objectImage.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);;
		tempBI.getGraphics().drawImage(objectImage, 0, 0, null);
		return tempBI;
	} // end loadImage
	
	/** Metoda rysujaca obraz w boardBI. */
	private void drawBoard(){
		rect = new Rectangle2D.Float(0,0, board.getWidth()-80, board.getHeight()-80);
		board.boardBI = loadImage(boardImgPath + board.gameSize + "x" + board.gameSize + ".png");
		boardTexture = new TexturePaint(board.boardBI, rect);
		board.boardBI = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		board.g2d = (Graphics2D) board.boardBI.getGraphics();					
		board.g2d.setPaint(boardTexture);
		board.g2d.fill(rect);	
	} // end init
	

	
	/** Metoda tworzaca tekstury kamieni. */
	private void drawStones(){
		rect = new Rectangle2D.Float(0,0, board.stoneSize, board.stoneSize);
		board.WStoneBI = loadImage(WstoneImgPath);
		board.BStoneBI = loadImage(BstoneImgPath);
		WStoneTexture = new TexturePaint(board.WStoneBI, rect);
		BStoneTexture = new TexturePaint(board.BStoneBI, rect);
		board.WStoneBI = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		board.BStoneBI = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		board.alphaWStoneBI = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		board.alphaBStoneBI = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		board.g2d = (Graphics2D) board.WStoneBI.getGraphics();					
		board.g2d.setPaint(WStoneTexture);
		board.g2d.fill(rect);
		board.g2d.translate(board.stoneSize,board.stoneSize);		board.g2d.fill(rect);
		board.g2d = (Graphics2D) board.BStoneBI.getGraphics();					
		board.g2d.setPaint(BStoneTexture);
		board.g2d.fill(rect);
		AlphaComposite alphaCom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
		board.g2d = (Graphics2D) board.alphaBStoneBI.getGraphics();
		board.g2d.setComposite(alphaCom);					
		board.g2d.setPaint(BStoneTexture);
		board.g2d.fill(rect);		
		board.g2d = (Graphics2D) board.alphaWStoneBI.getGraphics();
		board.g2d.setComposite(alphaCom);					
		board.g2d.setPaint(WStoneTexture);
		board.g2d.fill(rect);
	} // end drawStone
}
