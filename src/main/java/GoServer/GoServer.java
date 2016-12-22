package GoServer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;

import javax.swing.JFrame;


public class GoServer {

	/** Pola obslugi klient-serwer. */
	private final static int PORT = 8000;
	private ServerSocket server = null;
	protected static HashSet<ClientHandler> clients = new HashSet<ClientHandler>();
	protected Matchmaker matchmaker;
	
/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstruktor klasy. Ustawia serwer na porcie PORT. */
	public GoServer(){
	    try {
	        server = new ServerSocket(PORT);
	        matchmaker = Matchmaker.getInstance();
	        System.out.println("Server is up on port " + PORT); 
	    }catch (IOException e) {
	        System.out.println("Could not listen on port " + PORT); System.exit(-1);
	    }		
	} // end GoServer constructor

	/** Metoda glowna, tworzy nowy serwer. Wyswietla okno serwera (tylko dla wygody). */
	public static void main(String[] args) {
	    JFrame frame = new JFrame("GO SERVER");
		frame.setResizable(false);	frame.setSize(200,200);
		frame.addWindowListener( new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); } } );
		frame.setVisible(true);
		GoServer goServer = new GoServer();      
	    try {
			goServer.startListening();
		} catch (IOException e1) { e1.printStackTrace();	}
	} // end main

	
	/** Metoda zaczyna nasluchiwac na gniezdzie. */
	protected void startListening() throws IOException{
        while(true){ /* Przyjmowanie nowych klientow. */
        	listenSocket();
        }
	}// end startListening
	

	/** Metoda przyjmujaca nowego klienta. Dla kazdego tworzona jest instancja klasy ClientHandler.
	 * @throws IOException  */
	private void listenSocket() throws IOException {
		new ClientHandler( server.accept(), this ).start();
	}// end listenSocket
	
	
	/** Metoda do zamkniecia serwera, uruchamiana automatycznie przez JVM. */
	protected void finalize() {
	    try {
	      server.close();
		  System.out.println("Server shutdown.");
	    } catch (IOException e) { System.out.println("Could not close server properly."); System.exit(-1); }
	}// end finalize
	
	/** Metoda zaczyna nasluchiwac na gniezdzie. */
	public void startListeningInTestEnvironment(int clientsToEnter) throws IOException{
        while(true){ /* Przyjmowanie nowych klientow. */
        	if(clientsToEnter > 0){
        		listenSocket();
        		clientsToEnter--;
        	}else{
        		//break;
        	}
        }
	}// end startListening
	
	/** Metoda konczy prace serwera (glownie napisana dla testów JUnit). */
	public void serverShutdown(){
		 try {
		      server.close();
			  System.out.println("Server shutdown.");
		    } catch (IOException e) { System.out.println("Could not close server properly."); System.exit(-1); }	
	}

	
}// end GoServer CLASS
