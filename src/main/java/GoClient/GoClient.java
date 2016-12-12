package GoClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.Timer;


/** 
 * PROTOKOL KOMUNIKACJI
 * TODO: moze nie potrzebne..?
 * TODO: problemy z zamykaniem okien
 * TODO: matchmaker nie patrzy na ustawienia graczy - laczy mecze z roznymi planszami
 * TODO: zaimplementowac poprawnosc ruchow
 * TODO: wzorce projektowe
 * TODO: AI!!
 * TODO: UZYC GAME SESSION DO KOMUNIKACJI
 * 
 * */

public class GoClient implements ActionListener{

	/** Pola obslugi klient-serwer. */
	private final static int PORT = 8000;
	private Socket socket;
	public PrintWriter out;
	public BufferedReader in;
	
	/** Pola GUI. */
	private MainMenu mainMenu;
	protected BoardFrame boardFrame;
	private SettingsFrame settings;
	protected ActionListener listener;
	
/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstruktor klasy. Tworzy GUI dla klienta. */
	GoClient(){
		mainMenu = new MainMenu(this);
		settings = new SettingsFrame(this);
	} // end GoClient constructor

	/** Metoda glowna klienta. Pokazuje GUI.*/
	public static void main(String[] args) {
		GoClient client = new GoClient();
		client.mainMenu.setVisible(true);
		client.listenSocket();
	} // end main

	/** Metoda ustanawiajaca polaczenie z serwerem. */
	public void listenSocket(){
	    String host = "localhost";
	    try {
	      socket = new Socket( host, PORT);
	      out = new PrintWriter(socket.getOutputStream(), true);
	      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	      System.out.println("Connected to Go Server on port " + PORT);
	    }
	    catch (UnknownHostException e) {
	       System.out.println("Unknown host: " + host); System.exit(1);
	     }
	     catch  (IOException e) {
	       System.out.println("No I/O.\nServer is probably not set up."); System.exit(1);
	     }
	}// end listenSocket
	
	/** Metoda obslugujaca zdarzenia wysylane przez okna klienta. */
	public void actionPerformed(ActionEvent e) {
		/* Kazdy if wysyla do serwera odpowiedni komunikat, ktory serwer obsluguje metoda executeCommand*/
		String command = "OK";
		if(		 e.getActionCommand() == "Start New Game"){
			mainMenu.setVisible(false);
			command = "START";
		}else if(e.getActionCommand() == "Options"){
			command = "SETTINGS";
		}else if(e.getActionCommand() == "Exit"){
			command = "EXIT";	
		}else if(e.getActionCommand() == "Save Settings"){
			command = createSettingsString();
		}else if(e.getActionCommand() == "Exit Settings"){
			command = "CLOSE SETTINGS";
		}else if(e.getActionCommand() == "EXIT GAME"){
			command = "EXIT GAME";
		}else if(e.getActionCommand() == "Resign"){
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to resign?", "", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION){
				command = "EXIT GAME";
	        }else command = "OK";
		}else if(e.getActionCommand() == "Pass"){	/** TODO: zaimplementowac  */
			command = "OK";
		}else if(e.getActionCommand().startsWith("GAME") ){	
			command = e.getActionCommand();
		}
		/* Wyslij polecenie do server i czekaj na odpowiedz */
		out.println(command);
		try { /* Wykonaj polecenie otrzymane przez serwer */
			executeCommand(in.readLine());
		}catch (IOException exception) { System.out.println("Read failed"); System.exit(1); }	
	} // end actionPerformed

	/** Metoda wykonujaca polecenia otrzymane od serwera. 
	 * TODO: executeCommand: uzupelnic o komendy
	 * mozna pomyslec o osobnych funkcjach, zeby nie przedluzacz tej metody za bardzo*/
	private void executeCommand(String command){
		if(command == null){System.out.println("null command."); return;}
		System.out.println(command);//
		if(command.startsWith("OPEN BOARD")){
			openBoard(command);
		}else if(command.startsWith("OPEN SETTINGS")){
			mainMenu.setVisible(false); settings.setVisible(true);			
		}else if(command.startsWith("CLOSE SETTINGS")){
			settings.setVisible(false);	mainMenu.setVisible(true);		
		}else if(command.startsWith("EXIT GAME")){
			mainMenu.setVisible(true);  boardFrame.setVisible(false);
		}else if(command.startsWith("EXIT")){
			System.exit(0);
		}else if(command.startsWith("WAIT OPPONENT")){  // ta wiadomosc pewnie powinna byc w nowym okienku, nie w dialogu
														// to by ulatwilo wybor opcji i byloby bardziej kompatibilne z reszta
			//openBoard(command);
		}else if(command.startsWith("GAME")){
			executeGameCommand(command);
		}else
		if(command.startsWith("OK")){ } // all ok. do nothing
	} // end executeCommand
	
	/** Metoda otwiera okno z gra z odpowiednimi ustawieniami. */
	private void openBoard(String command){
		int size, opponent;
		char color;
		if(command.contains("AI")) opponent = 1; else opponent = 0;
		if(command.contains("19")) size = 19;
		else if(command.contains("13")) size = 13; else size = 9;
		if(command.contains("W"))color = 'W'; else color = 'B'; 
		mainMenu.setVisible(false);
		boardFrame = new BoardFrame(this, size, opponent, color); 
		//if(color == 'B') boardFrame.setEnabled(true);
		//else boardFrame.setEnabled(false);
		boardFrame.setVisible(true);
		startGame();
	} // end openBoard
	
	/** Metoda pobiera ustawienia z okna i przekazuje jako komende. */
	private String createSettingsString(){
		String command = "CHANGE SETTINGS";
		if(settings.AIBox.isSelected()) 		 command += " AI";
		if(settings.otherClientBox.isSelected()) command += " PL"; // Player
		if(settings.smallSizeBox.isSelected()) 	 command += " 9x";
		if(settings.mediumSizeBox.isSelected())  command += " 13";
		if(settings.bigSizeBox.isSelected()) 	 command += " 19";
		return command;
	} // end createSettingsString
	
	/** Metoda zaczyna komunikacje w czasie gry miedzy klientami a serwerem. */
	private void startGame(){
		if(boardFrame.playerColor == 'B'){ boardFrame.addMouseListener();}
		int timerDelay = 500; // in ms
		listener = new ActionListener(){
			private String command;
				
			public void actionPerformed(ActionEvent e){
				try {
					out.println(e.getActionCommand());
					boardFrame.repaint();
					command = in.readLine(); 
					if (command == null) {
						return;
					}
					executeCommand(command);
					if(command.equals("EXIT")) {throw new IOException();}
				}catch (IOException exception){	System.out.println("Error in game communication.");}
			}
		};
		new Timer(timerDelay, listener).start();
	} // end startGame
	
	/** Metoda do obslugi zdarzen z gry, np. blokowanie okna gracza, ktry nie ma ruchu. */
	private void executeGameCommand(String command){
		if(command.contains("ENABLE")){
			boardFrame.addMouseListener();
			if(command.contains("NEWSTONE")){
				int posXIndex = command.indexOf("POSX:"); String posX = command.substring(posXIndex+5, posXIndex+5+2) ;
				int posYIndex = command.indexOf("POSY:"); String posY = command.substring(posYIndex+5, posYIndex+5+2) ;
				boardFrame.putOpponentStone(posX, posY);
			}
		}else if(command.contains("DISABLE")){ }
	}// end executeGameCommand
	
}
