package GoClient;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

public class BoardFrame extends JFrame {

	/** Wymiary okna. */
	/** TODO:	te wartosci do osobnego enuma ? (oprocz ostatnich - windowL i windowH)*/
	public final static int smallBoardWindowLength 	= 730; // 9*50+80 na plansze, 200 na informacje o grze
	public final static int smallBoardWindowHeight 	= 530;
	public final static int mediumBoardWindowLength = 780; // 580 na plansze, 200 na informacje o grze
	public final static int mediumBoardWindowHeight = 580;
	public final static int bigBoardWindowLength 	= 880; // 680 na plansze, 200 na informacje o grze
	public final static int bigBoardWindowHeight 	= 680;
	public int windowLength, windowHeight;
	/** Plansza do gry. */
	private int boardSize;
	private GameBoard board;
	private JButton resign, pass;
	private JLabel player1, player2;
	/** Klient. */
	private GoClient client;

/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstruktor. Tworzy okno oraz plansze do gry.
	 * 	TODO: panel z statystykami*/
	BoardFrame(GoClient client, int size, int opponent){
		super();
		this.client = client;
		init();
		boardSize = size;
		chooseWindowSize();
		board = new GameBoard(this, boardSize, opponent);	//TODO: 9 - settings.size
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		setSizes();		
		addToFrame();
		repaint();
	} // end Board constructor
	
	/** Metoda do inicjalizacji pol. */
	private void init(){
		addWindowListener(new BoardWindowAdapter());
		resign = new JButton("Resign"); resign.addActionListener(client);
		pass = new JButton("Pass");		pass.addActionListener(client);
		player1 = new JLabel("<html> White<br>  Player1</html>"); //html do wyrownywania tekstu
		player2 = new JLabel("<html> Black<br>  Player2</html>"); //TODO: Player - clientID ?
	}
	
	/** Metoda ustala wielkosc okna w zaleznosci od rozmiaru planszy. */
	private void chooseWindowSize(){
		if(boardSize == 9){
			windowLength = smallBoardWindowLength;
			windowHeight = smallBoardWindowHeight;
		}else if(boardSize == 13){
			windowLength = mediumBoardWindowLength;
			windowHeight = mediumBoardWindowHeight;
		}else if(boardSize == 19){
			windowLength = bigBoardWindowLength;
			windowHeight = bigBoardWindowHeight;	
		}		
	} // end chooseWindowSize
	
	/** Ustawienie rozmiarow okna, planszy do gry oraz statystyk danej rozgrywki. */
	private void setSizes(){
		setSize(windowLength,windowHeight);
		setLocation(300,50);
		SpringLayout layout= new SpringLayout();
		setLayout(layout);
		/* Ustawienie zaleznosci w obrebie okna. */
		Dimension boardDim= new Dimension(windowHeight, windowHeight);	board.setPreferredSize(boardDim);
		Dimension playerLabelDim= new Dimension(windowLength - windowHeight, 50);	
		player1.setPreferredSize(playerLabelDim); player2.setPreferredSize(playerLabelDim);
		Dimension buttonDim= new Dimension(100, 30);	
		resign.setPreferredSize(buttonDim); pass.setPreferredSize(buttonDim);
		layout.putConstraint(SpringLayout.WEST,	board,	0,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, board,	0,	SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST,		player1,	windowHeight,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, 	player1,	0,	SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST,		player2,	windowHeight,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, 	player2,	windowHeight/10,	SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST,		resign,	windowHeight+15,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH,	resign,	2*windowHeight/5,	SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST,		pass,	windowHeight+15,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, 	pass,	2*windowHeight/5+50,	SpringLayout.NORTH, this);
	} // end setSizes
	
	/** Metoda dodajaca elementy do okna. */
	private void addToFrame(){
		add(board);		
		add(player1);	add(player2);
		add(resign);		add(pass);
	} // end addToFrame
	
	
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
