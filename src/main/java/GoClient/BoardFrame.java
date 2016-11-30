package GoClient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class BoardFrame extends JFrame {

	/** Wymiary okna. */
	/** Przy 19x19 plansza chyba musi byc wieksza...
	 * chyba lepiej bedzie to skalowac */
	public final static int windowLength = 700; // 500 na plansze, 200 na informacje o grze
	public final static int windowHeight = 500;
	/** Plansza do gry. */
	private GameBoard board;
	/** Klient. */
	private GoClient client;

/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstruktor. Tworzy okno oraz plansze do gry.
	 * 	TODO: panel z statystykami*/
	BoardFrame(GoClient client, int size, int opponent){
		super();
		this.client = client;
		board = new GameBoard(size, opponent);	//TODO: 9 - settings.size
		addWindowListener(new BoardWindowAdapter());
		setSizes();		
		add(board);
		repaint();
	} // end Board constructor
	
	/** Ustawienie rozmiarow okna, planszy do gry oraz statystyk danej rozgrywki. */
	private void setSizes(){
		setSize(windowLength,windowHeight);
		setLocation(400,125);
		SpringLayout layout= new SpringLayout();
		setLayout(layout);
		/* Ustawienie zaleznosci w obrebie okna. */
		Dimension boardDim= new Dimension(windowHeight, windowHeight);	board.setPreferredSize(boardDim);
		layout.putConstraint(SpringLayout.WEST,	board,	0,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, board,	0,	SpringLayout.NORTH, this);
	} // end setSizes
	

/** W tym momencie bezuzyteczna klasa (brak funkcjonalnosci. */
private class BoardWindowAdapter extends WindowAdapter{
	public void windowClosing(WindowEvent e){ 
		client.out.println("EXIT");		
		System.exit(0);
	}		
} // end BoardWindowAdapter

}// end BoardFrame
