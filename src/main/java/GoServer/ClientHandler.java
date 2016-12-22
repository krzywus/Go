package GoServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import GoServer.GameSession.GameSession;

/** Klasa do obslugi klientow - kazdy klient posiada osobny watek (obiekt).
* W obrebie tego watku zachodzi komunikacja miedzy klientem i serwerem. */
public class ClientHandler extends Thread {

	/** Pola do obslugi klient-serwer. */
	private GoServer server;
	private Socket socket;
	protected BufferedReader in;
	public PrintWriter out;
	private String command;
	
	/** Zmienna do ominiecia wyslania zadania do klienta. */
	private boolean skipExecution = false;
	
	/** Pola ustawien - aktualny tryb gry (rozmiar planszy, typ przeciwnika). */
	private Settings settings;
	/** Pole aktywnej gry. */
	private GameSession session;
	
/*---------------------------------------------------------------------------------------------*/

	/** Konstruktor klasy. Tworzy gniazdo dla klienta oraz nowe ustawienia. */
	ClientHandler(Socket socket, GoServer server){
		this.socket = socket;
		this.server= server;
		GoServer.clients.add(this);
		settings = new Settings();
		setGameSession(null);
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
				if(command.equals("EXIT")) {throw new IOException();}
			}
		}catch (IOException e){	System.out.println("Client disconnected with Server.");
		}finally{
			try {
				GoServer.clients.remove(this);
				socket.close();
			} catch (IOException e) { System.out.println("Error in closing socket. Server shutdown."); System.exit(-1);}
		}
	} // end run
	
	/** Metoda obslugujaca komunikaty wyslane przez klienta. */
	private void executeCommand(String command){
		String execute = "OK";
		skipExecution = false;
		if(command.startsWith("OK")){ //aby ominac wszystkie ify
		}else if(command.startsWith("GAME")){
			skipExecution = true;	
			session.doAction(command);
		}else if(command.startsWith("SETTINGS")){
			execute = "OPEN SETTINGS";
		}else if(command.startsWith("EXIT")){
			if(command.contains("GAME")){
				execute = "EXIT GAME";
				if(session != null){
					session.abortGame(this);
				}
			}
			else { execute = "EXIT"; }
		}else if(command.startsWith("CHANGE SETTINGS")){
			changeSettings(command);
			execute = "CLOSE SETTINGS"; 
		}else if(command.startsWith("CLOSE SETTINGS")){
			execute = "CLOSE SETTINGS"; 
		}else if(command.startsWith("START")){
			if(!server.matchmaker.addPlayer(this, settings.boardSize, settings.opponentType)){
				skipExecution = true;
				/**TODO: waiting frame*/
				//execute = "WAIT OPPONENT";
			} // w przeciwnym wypadku gra powinna sie otworzyc. 
		} 
		
		if(skipExecution){	// jezeli gracz jest w grze, komende wysle do niego session
			skipExecution = false;
		}else{
			out.println(execute); /* Wyslanie odpowiedniej komendy do klienta. */
		}
	} // end executeCommand
	
	
	/** Metoda zmieniajaca ustawienia. */
	private void changeSettings(String command){
		if		(command.contains("PL")) settings.opponentType = 0;
		else if (command.contains("AI")) settings.opponentType = 1;
		if		(command.contains("9x")) settings.boardSize = 9;
		else if (command.contains("13")) settings.boardSize = 13;
		else if (command.contains("19")) settings.boardSize = 19;
	} // end changeSettings

	/** Metoda zaczyna gre u klienta. */
	public void startGame(ClientHandler opponent, char color){
		String execute = "OPEN BOARD " + settings.boardSize + " " + settings.opponentType + " " + color;
		out.println(execute); /* Wyslanie odpowiedniej komendy do klienta. */
	}// end startGame
	
	/** Metoda ustawia sesje rozgrywki dla klienta. */
	public void setGameSession(GameSession session){
		this.session = session;
	}//end setGameSession
	

} // end ClientHandler CLASS