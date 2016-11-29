package GoServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;


public class GoServer {

	/** Pola obslugi klient-serwer. */
	private final static int PORT = 8000;
	private ServerSocket server = null;
	
/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstruktor klasy. Ustawia serwer na porcie PORT. */
	GoServer(){
	    try {
	        server = new ServerSocket(PORT);
	        System.out.println("Server is up on port " + PORT);
	      } 
	      catch (IOException e) {
	        System.out.println("Could not listen on port " + PORT); System.exit(-1);
	      }	
	} // end GoServer constructor

	/** Metoda glowna, tworzy nowy serwer. Wyswietla okno serwera (tylko dla wygody). */
	public static void main(String[] args) {
	    GoServer server = new GoServer();
	    JFrame frame = new JFrame("GO SERVER");
		frame.setResizable(false);	frame.setSize(200,200);
		frame.addWindowListener( new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } } );
		frame.setVisible(true);
        while(true){ /* Przyjmowanie nowych klientow. */
           try {
			server.listenSocket();
           } catch (IOException e1) { System.out.println("Error listening to socket.");	}
         }
	} // end main


	/** Metoda przyjmujaca nowego klienta. Dla kazdego tworzona jest instancja klasy ClientHandler.
	 * @throws IOException  */
	private void listenSocket() throws IOException {
		new ClientHandler( server.accept() ).start();
	}// end listenSocket
	
	
	/** Metoda do zamkniecia serwera, uruchamiana automatycznie przez JVM. */
	protected void finalize() {
	    try {
	      server.close();
		  System.out.println("Server shutdown.");
	    } catch (IOException e) { System.out.println("Could not close server properly."); System.exit(-1); }
	}// end finalize
	
/*-------------------------------------------------------------------------------------------------------------------*/	
	
/** Klasa do obslugi klientow - kazdy klient posiada osobny watek (obiekt).
 * W obrebie tego watku zachodzi komunikacja miedzy klientem i serwerem. */
private static class ClientHandler extends Thread {
	
	/** Pola do obslugi klient-serwer. */
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private String command;
	
	/** Pola ustawien - aktualny tryb gry (rozmiar planszy, typ przeciwnika). */
	private Settings settings;
    	
/*---------------------------------------------------------------------------------------------*/

	/** Konstruktor klasy. Tworzy gniazdo dla klienta oraz nowe ustawienia. */
	ClientHandler(Socket socket){
		this.socket = socket;
		settings = new Settings();
	} // end ClientHandler constructor

	/** Metoda watku run. Ustanawia polaczenie in, out. Obsluguje komunikacje. */
	public void run() {
		try {
			System.out.println("Client connected.");
			in = new BufferedReader(new InputStreamReader( socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			while (true) { /* Obsluga komunikacji klient-serwer. */
				command = in.readLine(); 
				if (command == null) {
					return;
				}
				executeCommand(command);
			}
		}catch (IOException e){	System.out.println("Client disconnected with Server.");
		}finally{
			try {
				socket.close();
			} catch (IOException e) { System.out.println("Error in closing socket. Server shutdown."); System.exit(-1);}
		}
	} // end run

	/** Metoda obslugujaca komunikaty wyslane przez klienta. */
	private void executeCommand(String command){
		String execute = "";
		if(command.startsWith("START")){
			execute = "OPEN BOARD";
		}else if(command.startsWith("OPTIONS")){
			execute = "OPEN OPTIONS";
		}else if(command.startsWith("EXIT")){
			execute = "EXIT"; }
		
		out.println(execute); /* Wyslanie odpowiedniej komendy do klienta. */
	} // end executeCommand
	
} // end ClientHandler CLASS
	
	
}// end GoServer CLASS
