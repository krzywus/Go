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

	/** Pola do obslugi grafiki. 
	 * TODO: dodac grafiki dla czenych i bialych kamieni zgodnie ze sciezka ponizej. */
	private final static String boardImgPath = "src/graphics/Board";
	//private final static String BstoneImgPath = "src/graphics/WhiteStone.png";
	//private final static String WstoneImgPath = "src/graphics/BlackStone.png";
	public Graphics2D g2d;
	protected BufferedImage boardBI;	
	private TexturePaint boardTexture;
	public Rectangle2D.Float rect;
	
	/** Ustawienia planszy. */
	private int gameSize;	// size of game € {9x9, 13x13, 19x19}
	
/*-------------------------------------------------------------------------------------------------------------------*/
	
	/** Konstruktor. Tworzy plansze. */
	GameBoard(int gameSize){
		super();
		this.gameSize = gameSize;
		setDoubleBuffered(true);
		setFocusable(true);
		init();
	} // end GameBoard constructor

	/** Metoda inicjujaca obiekt. */
	private void init(){
		setSize(BoardFrame.windowHeight, BoardFrame.windowHeight);
		drawBoard();	// narysuj plansze w buforze boardBI
	} // end init
	
	/** Metoda rysujaca na obiekcie. */
	protected void paintComponent(Graphics g){
		super.paintComponent(g);	
		g.drawImage(boardBI, 0, 0, null); // narysuj plansze
	} // end paintComponent
	
	/** Metoda rysujaca obraz w boardBI. */
	private void drawBoard(){
		boardBI = loadImage(boardImgPath + gameSize + "x" + gameSize + ".png");
		rect = new Rectangle2D.Float(0,0, getWidth(), getHeight());
		boardTexture = new TexturePaint(boardBI, rect);
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
	
}
