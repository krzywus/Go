package GoClient;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class BoardFrameBuilder {
	
	/** Wymiary okna. */
	public final static int smallBoardWindowLength 	= 730; // 9*50+80 na plansze, 200 na informacje o grze
	public final static int smallBoardWindowHeight 	= 530;
	public final static int mediumBoardWindowLength = 780; // 580 na plansze, 200 na informacje o grze
	public final static int mediumBoardWindowHeight = 580;
	public final static int bigBoardWindowLength 	= 880; // 680 na plansze, 200 na informacje o grze
	public final static int bigBoardWindowHeight 	= 680;	
	/** Aktualny rozmiar planszy. */
	protected int windowLength, windowHeight;
	/** Element okna. */
	protected JButton resign, pass, info;
	/** Element okna. */
	protected JLabel player1, player2;
	/** Element okna. */
	protected JButton bargainAccept, bargainDecline, bargainSend;
	
	/** Okno ktorego wymiary sa ustalane.*/
	private BoardFrame frame;
	
/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstrutkor klasy. */
	BoardFrameBuilder(BoardFrame frame){
		this.frame = frame;
		init();
		chooseWindowSize();
	}//end BoardFrameBuilder constr
	
	/** Metoda do inicjalizacji pol. */
	private void init(){
		resign  		 = new JButton("Resign"); 	resign.addActionListener(frame.client);
		pass 			 = new JButton("Pass");		pass.addActionListener(frame.client);
		bargainAccept	 = new JButton("Accept"); 	bargainAccept.addActionListener(frame.client);
		bargainDecline 	 = new JButton("Decline");  bargainDecline.addActionListener(frame.client);
		bargainSend		 = new JButton("Send"); 	bargainSend.addActionListener(frame.client);
		info			 = new JButton("INFO"); 	info.addActionListener(frame.client);
		if(frame.playerColor == 'W'){
			player1 = new JLabel("<html> Black<br>  Opponent</html>"); //TODO: Player - clientID ?
			player2 = new JLabel("<html> White<br>  You<br> Komi: 6.5</html>"); //html do wyrownywania tekstu
		}else{
			player1 = new JLabel("<html> Black<br>  You</html>"); 
			player2 = new JLabel("<html> White<br>  Opponent<br> Komi: 6.5</html>"); //html do wyrownywania tekstu
		}
		frame.inGameInfo = new JLabel("");	
	}// end init
	
	/** Metoda ustala wielkosc okna w zaleznosci od rozmiaru planszy. */
	private void chooseWindowSize(){
		if(frame.boardSize == 9){
			frame.windowLength = smallBoardWindowLength;
			frame.windowHeight = smallBoardWindowHeight;			
			windowLength = smallBoardWindowLength;
			windowHeight = smallBoardWindowHeight;
		}else if(frame.boardSize == 13){
			frame.windowLength = mediumBoardWindowLength;
			frame.windowHeight = mediumBoardWindowHeight;
			windowLength = mediumBoardWindowLength;
			windowHeight = mediumBoardWindowHeight;
		}else if(frame.boardSize == 19){
			frame.windowLength = bigBoardWindowLength;
			frame.windowHeight = bigBoardWindowHeight;	
			windowLength = bigBoardWindowLength;
			windowHeight = bigBoardWindowHeight;
		}		
	} // end chooseWindowSize
	
	/** Ustawienie rozmiarow okna, planszy do gry oraz statystyk danej rozgrywki. */
	protected void setSizes(){
		frame.setSize(windowLength,windowHeight);
		frame.setLocation(300,50);
		SpringLayout layout= new SpringLayout();
		frame.setLayout(layout);
		/* Ustawienie zaleznosci w obrebie okna. */
		Dimension boardDim= new Dimension(windowHeight, windowHeight);	frame.board.setPreferredSize(boardDim);
		Dimension playerLabelDim= new Dimension(windowLength - windowHeight, 50);	
		player1.setPreferredSize(playerLabelDim); player2.setPreferredSize(playerLabelDim);
		Dimension inGameInfoLabelDim= new Dimension(windowLength - windowHeight, 80);	
		frame.inGameInfo.setPreferredSize(inGameInfoLabelDim);
		Dimension buttonDim= new Dimension(100, 30);	
		resign.setPreferredSize(buttonDim); pass.setPreferredSize(buttonDim);
		bargainAccept.setPreferredSize(buttonDim); bargainDecline.setPreferredSize(buttonDim);
		bargainSend.setPreferredSize(buttonDim); info.setPreferredSize(buttonDim);
		layout.putConstraint(SpringLayout.WEST,	frame.board,	0,	SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.NORTH, frame.board,	0,	SpringLayout.NORTH, frame);

		layout.putConstraint(SpringLayout.WEST,		player1,	windowHeight,	SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.NORTH, 	player1,	0,	SpringLayout.NORTH, frame);
		layout.putConstraint(SpringLayout.WEST,		player2,	windowHeight,	SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.NORTH, 	player2,	windowHeight/10,	SpringLayout.NORTH, frame);

		layout.putConstraint(SpringLayout.WEST,		frame.inGameInfo,	windowHeight,	SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.NORTH, 	frame.inGameInfo,	windowHeight - 2*windowHeight/10,	SpringLayout.NORTH, frame);

		layout.putConstraint(SpringLayout.WEST,		resign,	windowHeight+15,	SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.NORTH,	resign,	2*windowHeight/5,	SpringLayout.NORTH, frame);
		layout.putConstraint(SpringLayout.WEST,		pass,	windowHeight+15,	SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.NORTH, 	pass,	2*windowHeight/5+50,	SpringLayout.NORTH, frame);
		layout.putConstraint(SpringLayout.WEST,		info,	windowHeight+15,	SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.NORTH, 	info,	2*windowHeight/5+100,	SpringLayout.NORTH, frame);
		
		layout.putConstraint(SpringLayout.WEST,		bargainAccept,	windowHeight+15,	SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.NORTH,	bargainAccept,	2*windowHeight/5,	SpringLayout.NORTH, frame);
		layout.putConstraint(SpringLayout.WEST,		bargainDecline,	windowHeight+15,	SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.NORTH, 	bargainDecline,	2*windowHeight/5+50,	SpringLayout.NORTH, frame);
		layout.putConstraint(SpringLayout.WEST,		bargainSend,	windowHeight+15,	SpringLayout.WEST, frame);
		layout.putConstraint(SpringLayout.NORTH,	bargainSend,	2*windowHeight/5+100,	SpringLayout.NORTH, frame);
	} // end setSizes
	
	/** Metoda dodajaca elementy do okna. */
	protected void addToFrame(){
		frame.add(frame.board);		
		frame.add(player1);		frame.add(player2);
		frame.add(resign);		frame.add(pass);
		frame.add(frame.inGameInfo);	frame.add(info);
	} // end addToFrame
}
