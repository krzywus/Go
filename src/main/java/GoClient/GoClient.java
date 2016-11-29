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
		System.out.println(e.getSource());
		/* Kazdy if wysyla do serwera odpowiedni komunikat, ktory serwer obsluguje metoda executeCommand*/
		if(e.getActionCommand() == "Start New Game"){
		      out.println("START");
		}
		if(e.getActionCommand() == "Options"){
		      out.println("OPTIONS");
		}
		if(e.getActionCommand() == "Exit"){
		      out.println("EXIT");
		      try {
				socket.close();
				return;
		      } catch (IOException e1) { e1.printStackTrace(); }		      
		}
		try { /* Wykonaj polecenie wyslane przez serwer */
			executeCommand(in.readLine());
		}catch (IOException execption) { System.out.println("Read failed"); System.exit(1); }	
	} // end actionPerformed

	/** Metoda wykonujaca polecenia otrzymane od serwera. 
	 * TODO: executeCommand: uzupelnic o komendy*/
	private void executeCommand(String command){
		System.out.println(command);//
		if(command.startsWith("OPEN BOARD")){
			mainMenu.setVisible(false);
			boardFrame = new BoardFrame(this); 
			boardFrame.addWindowListener(new MyWindowAdapter(this));
			boardFrame.setVisible(true);
		}
		if(command.startsWith("OPEN OPTIONS")){ //dodac frame dla opcji
			mainMenu.setVisible(false);
			settings.setVisible(true);			
		}
	} // end executeCommand
	
}
