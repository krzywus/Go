package GoClient;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/** Okno do gry - zawiera plansze oraz przyciski do pasowania, rezygnacji, informacji oraz panele z inforamcjami.*/
public class BoardFrame extends JFrame {


	/** Plansza do gry. */
	protected int boardSize;
	protected GameBoard board;
	/** Klasa tworzaca wymiary i elementy okna. */
	protected BoardFrameBuilder boardBuilder;	
	/** Aktualny rozmiar planszy. */
	protected int windowLength, windowHeight;
	/** Element okna. */
	protected JLabel inGameInfo;
	/** Klient. */
	protected GoClient client;
	protected char playerColor;
	/** MouseListener */
	private BoardMouseAdapter mouseAdapter = new BoardMouseAdapter();
	private BargainMouseAdapter bargainMouseAdapter = new BargainMouseAdapter();
	/** Pole przechowuje infomarcje czy prowadzony jest targ o terytorium. */
	protected boolean territoryBargain = false;
	/** Obsluga targowania. */
	protected BargainHandler bargainHandler;

/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstruktor. Tworzy okno oraz plansze do gry.
	 * 	TODO: panel z statystykami*/
	BoardFrame(GoClient client, int size, int opponent, char color){
		super();
		this.client = client;
		playerColor = color;
		init();
		boardSize = size;
		boardBuilder.chooseWindowSize();
		board = new GameBoard(this, boardSize, opponent);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		boardBuilder.setSizes();		
		boardBuilder.addToFrame();
		setResizable(true);
		repaint();
	} // end Board constructor
	
	/** Metoda do inicjalizacji pol. */
	private void init(){
		addWindowListener(new BoardWindowAdapter());
		boardBuilder = new BoardFrameBuilder(this);
		boardBuilder.init();
	} // end init
	
	/** Metoda dodaje obsluge myszy do okna. */
	protected void addMouseListener() {
		addMouseListener( mouseAdapter );
	}// end addMouseListener	
	
	/** Metoda usuwa obsluge myszy do okna. */
	protected void removeMouseListener() {
		removeMouseListener( mouseAdapter );
	}// end addMouseListener
	
	/** Metoda dodaje obsluge myszy do okna w trakcie targu. */
	protected void addBargainMouseListener() {
		addMouseListener( bargainMouseAdapter );
	}// end addMouseListener	
	
	/** Metoda usuwa obsluge myszy do okna w trakcie targu. */
	protected void removeBargainMouseListener() {
		removeMouseListener( bargainMouseAdapter );
	}// end addMouseListener
	
	
	/** Metoda wykonujaca ruch przeciwnika na planszy. */
	public void putOpponentStone(String posX, String posY){
		if(posX.contains(" ")) posX = Character.toString( posX.charAt(0) );
		if(posY.contains(" ")) posY = Character.toString( posY.charAt(0) );
		int x = Integer.parseInt(posX);
		int y = Integer.parseInt(posY);
		board.putOpponentStone(x, y);
	} // end putOpponentStone

	/** Metoda zleca planszy usuniecie martwych kamieni. */
	protected void deleteDeadStones(String posX, String posY){
		if(posX.contains(" ")) posX = Character.toString( posX.charAt(0) );
		if(posY.contains(" ")) posY = Character.toString( posY.charAt(0) );
		int x = Integer.parseInt(posX);
		int y = Integer.parseInt(posY);
		board.deleteDeadStones(x, y);
	}//end deleteDeadStones
	
	/** Metoda blokuje przyciski Resign oraz Pass po zakonczeniu gry.*/
	protected void disableButtons(){
		boardBuilder.resign.setEnabled(false);
		boardBuilder.pass.setEnabled(false);
		
	}//end disableButtons
	
	/** Metoda odblokowuje przyciski Resign oraz Pass po zakonczeniu gry.*/
	protected void enableButtons(){
		boardBuilder.resign.setEnabled(true);
		boardBuilder.pass.setEnabled(true);
	}//end enableButtons
	
	/** Metoda zaczyna targowanie - zmienia dostepne przyciski w oknie, tworzy obsluge targowania. */
	protected void startBargain(){
		bargainHandler = new BargainHandler(board.getBoard(), boardSize);
		this.removeMouseListener(); 
		remove(boardBuilder.pass); remove(boardBuilder.resign); remove(boardBuilder.info);
		add(boardBuilder.bargainAccept); add(boardBuilder.bargainDecline); add(boardBuilder.bargainSend);
	} // end buttonChange
	
	/** Metoda resetuje mape alpha w BargainHandler. */
	protected void resetAlphaBoard(){
		bargainHandler.resetAlphaBoard();
		bargainHandler.resetSelectedStones();
		board.resetAlphaBoard();
	}// end resetAlpgaBoard
	
	/** Metoda zaznacza kamienie zaznaczone przez przeciwnika. */
	protected void markOpponentProposition(int positionX, int positionY){
		Stone stoneBoard[][] = board.getBoard();
		Stone stone = stoneBoard[positionX][positionY];
		board.setAlphaBoard( bargainHandler.markStones(stone) );  
		repaint();
	}// end markOpponentProposition
	
/*-------------------------------------------------------------------------------------------------------------------*/	
/** Klasa do obsługi przyciskow myszy na planszy. */
	private class BoardMouseAdapter implements MouseListener{
		public void mouseClicked (MouseEvent e) {
			if(board.contains(e.getPoint())){ 
				inGameInfo.setText("");
				disableButtons();
				removeMouseListener(this);	// wylaczenie mozliwosci wysylania sygnalow przez myszke
				if(board.checkMoveValidity(e.getX(), e.getY())){  
					int newStone[] = board.newStone;
					client.listener.actionPerformed(  // przeslanie do klienta
							new ActionEvent(this, 0, 
							"GAME MOVE POSX:" + newStone[0] + " POSY:" + newStone[1] + " KILL ")); 	
				}else { addMouseListener(this); }
			}
		} // end mouseClicked
		public void mouseEntered (MouseEvent e) {}
		public void mouseExited	 (MouseEvent e) {}
		public void mousePressed (MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	} // end BoardMouseAdapter/*-------------------------------------------------------------------------------------------------------------------*/	
/** Klasa do obsługi przyciskow myszy na planszy w trakcie targu. */
	private class BargainMouseAdapter implements MouseListener{
		public void mouseClicked (MouseEvent e) {
			if(board.contains(e.getPoint())){ 
				/** TODO: jak w powyzszej klasie, odpowiednie zachowanie na klikniecia*/
				board.setAlphaBoard( bargainHandler.markStones(board.getStone(e.getX(), e.getY())) );
				board.repaint(); // TODO: check if works
			}
		} // end mouseClicked
		public void mouseEntered (MouseEvent e) {}
		public void mouseExited	 (MouseEvent e) {}
		public void mousePressed (MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	} // end BoardMouseAdapter
/*-------------------------------------------------------------------------------------------------------------------*/	
/** Klasa do obsługi zamykania okna. */
private class BoardWindowAdapter extends WindowAdapter{
	@Override
	public void windowClosing(WindowEvent e){ 
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit current game?", "", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			client.actionPerformed(new ActionEvent(this, 0, "EXIT GAME"));
        }
	}		
} // end BoardWindowAdapter


}// end BoardFrame
