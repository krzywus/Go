package GoClient;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

public class MainMenu extends JFrame{

	/** Wymiary okna. */
	private final static int windowLength = 450;
	private final static int windowHeight = 450;
	/** Klient. */
	private GoClient client;
	/** Przyciski do wyboru w menu. */
	private static JButton startGame, options, exit;
	
/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstruktor. Ustawia rozmiar i dodaje przyciski. */
	MainMenu(GoClient client){
		super();
		setTitle("Game");
		this.client = client;
		addWindowListener(new WindowClose(this.client));
	
		addElements();
		setSizes();
		repaint();
	} // end MainMenu constructor

	/** Metoda ustala rozmiar okna i rozmieszczenie przyciskow. */
	private void setSizes(){
		setResizable(false);
		setSize(windowLength,windowHeight);
		setLocation(400,125);
		SpringLayout layout= new SpringLayout();
		setLayout(layout);
		/* Ustawienie rozmiarow przycikow. */
		Dimension startGameDim= new Dimension(200,50);	startGame.setPreferredSize(startGameDim);
		Dimension optionsDim  =	new Dimension(150,50);	options.setPreferredSize(optionsDim);
		Dimension exitDim     =	new Dimension(100,50);		exit.setPreferredSize(exitDim);
		/* Ustawienie miejsca obiektow w obrebie okna. */
		layout.putConstraint(SpringLayout.WEST,	startGame,	(int) (windowLength/2-startGameDim.getWidth()/2),	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST,	options,	(int) (windowLength/2-optionsDim.getWidth()/2) , 	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST,	exit,		(int) (windowLength/2-exitDim.getWidth()/2), 		SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, startGame,	75,		SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, options,	75*2,	SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, exit,		75*3,	SpringLayout.NORTH, this);
	} // end setSizes

	/** Metoda tworzaca i dodajaca przyciski do okna. */
	private void addElements(){
		startGame = new JButton("Start New Game");	startGame.addActionListener(client);
		options = new JButton("Options");			options.addActionListener(client);
		exit = new JButton("Exit");					exit.addActionListener(client);
		add(startGame); add(options); add(exit);
	} // end addButtons
	
}
