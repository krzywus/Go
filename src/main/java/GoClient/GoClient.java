package GoClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class GoClient implements ActionListener{

	/** Pola obslugi klient-serwer. */
	private final static int PORT = 8000;
	private Socket socket;
	public PrintWriter out;
	public BufferedReader in;
	
	/** Pola GUI. */
	private MainMenu mainMenu;
	private BoardFrame boardFrame;
	private SettingsFrame settings;
	
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
	
	/** Metoda obslugujaca zdarzenia wysylane przez okna klienta.
	 * TODO: actionPerformed: napisac obsluge zdarzen z okna gry. */
	public void actionPerformed(ActionEvent e) {
		/* Kazdy if wysyla do serwera odpowiedni komunikat, ktory serwer obsluguje metoda executeCommand*/
		String command = "";
		if(		 e.getActionCommand() == "Start New Game"){
			command = "START";
		}else if(e.getActionCommand() == "Options"){
			command = "SETTINGS";
		}else if(e.getActionCommand() == "Exit"){
			command = "EXIT";
		    try {
		    	out.println(command);
		    	socket.close();
				return;
		    } catch (IOException e1) { e1.printStackTrace(); }		      
		}else if(e.getActionCommand() == "Save Settings"){
			command = createSettingsString();
		}else if(e.getActionCommand() == "Exit Settings"){
			command = "CLOSE SETTINGS";
		}
		out.println(command);
		try { /* Wykonaj polecenie otrzymane przez serwer */
			executeCommand(in.readLine());
		}catch (IOException execption) { System.out.println("Read failed"); System.exit(1); }	
	} // end actionPerformed

	/** Metoda wykonujaca polecenia otrzymane od serwera. 
	 * TODO: executeCommand: uzupelnic o komendy
	 * mozna pomyslec o osobnych funkcjach, zeby nie przedluzacz tej metody za bardzo*/
	private void executeCommand(String command){
		System.out.println(command);//
		if(command.startsWith("OPEN BOARD")){ // uzupelnic o wybrane w opcje z settings, zeby otwieralo odpowiedni rozmiar
			openBoard(command);
		}else if(command.startsWith("OPEN SETTINGS")){
			mainMenu.setVisible(false);
			settings.setVisible(true);			
		}else if(command.startsWith("CLOSE SETTINGS")){
			settings.setVisible(false);	
			mainMenu.setVisible(true);		
		}else		
		if(command.startsWith("OK")){ 
		}
	} // end executeCommand
	
	/** Metoda otwiera okno z gra z odpowiednimi ustawieniami. */
	private void openBoard(String command){
		int size, opponent;
		if(command.contains("AI")) opponent = 1; else opponent = 0;
		if(command.contains("19")) size = 19;
		else if(command.contains("13")) size = 13; else size = 9;
		mainMenu.setVisible(false);
		boardFrame = new BoardFrame(this, size, opponent); 
		boardFrame.addWindowListener(new MyWindowAdapter(this));
		boardFrame.setVisible(true);
	}
	
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
	
}
