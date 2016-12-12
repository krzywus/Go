package GoServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import GoServer.GameSession.GameSession;
import GoServer.GameSession.GameSessionState;

/** Klasa do obslugi klientow - kazdy klient posiada osobny watek (obiekt).
* W obrebie tego watku zachodzi komunikacja miedzy klientem i serwerem. */
public class ClientHandler extends Thread {

	/** Pola do obslugi klient-serwer. */
	private GoServer server;
	private Socket socket;
	private BufferedReader in;
	protected PrintWriter out;
	private String command;
	
	/** Zmienna do ominiecia wyslania zadania do klienta. */
	private boolean skipExecution = false;
	

	/** Pola ustawien - aktualny tryb gry (rozmiar planszy, typ przeciwnika). */
	private Settings settings;
	/** Pole aktywnej gry. */
	private GameSession session;
	private ClientHandler opponent;
	
/*---------------------------------------------------------------------------------------------*/

	/** Konstruktor klasy. Tworzy gniazdo dla klienta oraz nowe ustawienia. */
	ClientHandler(Socket socket, GoServer server){
		this.socket = socket;
		this.server= server;
		this.server.clients.add(this);
		settings = new Settings();
		setGameSession(null);
	} // end ClientHandler constructor

	/** Metoda watku run. Ustanawia polaczenie in, out. Obsluguje komunikacje. 
	 * TODO: moze trzeba miec tablice out/in zeby nie mieszala sie komunikacja? chyba nie*/
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
				server.clients.remove(this);
				socket.close();
			} catch (IOException e) { System.out.println("Error in closing socket. Server shutdown."); System.exit(-1);}
		}
	} // end run
	
	/** Metoda obslugujaca komunikaty wyslane przez klienta. */
	private void executeCommand(String command){
		String execute = "OK";
		if(command.startsWith("SETTINGS")){
			execute = "OPEN SETTINGS";
		}else if(command.startsWith("EXIT")){
			if(command.contains("GAME")){
				execute = "EXIT GAME";
				if(session != null) session.setState(GameSessionState.GameAborted);
			}
			else { execute = "EXIT"; }
		}else if(command.startsWith("CHANGE SETTINGS")){
			changeSettings(command);
			execute = "CLOSE SETTINGS"; 
		}else if(command.startsWith("CLOSE SETTINGS")){
			execute = "CLOSE SETTINGS"; 
		}else if(command.startsWith("GAME")){
				execute = getGameAction(command); 
		}else if(command.startsWith("START")){
			if(!server.matchmaker.addPlayer(this)){
				skipExecution = true;
				//execute = "WAIT OPPONENT";
			} // w przeciwnym wypadku gra powinna sie otworzyc. 
		} 
		
		if(skipExecution){
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
		this.opponent = opponent;
		out.println(execute); /* Wyslanie odpowiedniej komendy do klienta. */
	}// end startGame
	
	/** Metoda ustawia sesje rozgrywki dla klienta. */
	public void setGameSession(GameSession session){
		this.session = session;
	}//end setGameSession
	
	/** Metoda do tworzenia instrukcji dla klientow w czasie gry. */
	private String getGameAction(String command){
		String execute = "OK";
		if(command.contains("MOVED")){
			int posXIndex = command.indexOf("POSX:");
			String posX = command.substring(posXIndex+5, posXIndex+5+2) ;
			int posYIndex = command.indexOf("POSY:");
			String posY = command.substring(posYIndex+5, posYIndex+5+2) ;
			System.out.println(posX + " " + posY);
			execute = "GAME DISABLE BOARD";
			opponent.out.println("GAME ENABLE BOARD NEWSTONE POSX:" + posX + " POSY:" + posY + " ");
		}
		return execute;
	}// end getGameAction

} // end ClientHandler CLASS