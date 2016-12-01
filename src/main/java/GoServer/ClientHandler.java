package GoServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/** Klasa do obslugi klientow - kazdy klient posiada osobny watek (obiekt).
* W obrebie tego watku zachodzi komunikacja miedzy klientem i serwerem. */
public class ClientHandler extends Thread {

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
				if(command.equals("EXIT")) {throw new IOException();}
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
			execute = "OPEN BOARD " + settings.boardSize + " " + settings.opponentType;
		}else if(command.startsWith("SETTINGS")){
			execute = "OPEN SETTINGS";
		}else if(command.startsWith("EXIT")){
			if(command.contains("GAME")) execute = "EXIT GAME";
			else { execute = "EXIT"; }
		}else if(command.startsWith("CHANGE SETTINGS")){
			changeSettings(command);
			execute = "CLOSE SETTINGS"; 
		}else if(command.startsWith("CLOSE SETTINGS")){
			execute = "CLOSE SETTINGS"; }
		out.println(execute); /* Wyslanie odpowiedniej komendy do klienta. */
	} // end executeCommand
	
	
	/** Metoda zmieniajaca ustawienia. */
	private void changeSettings(String command){
		if(command.contains("PL")) settings.opponentType = 0;
		if(command.contains("AI")) settings.opponentType = 1;
		if(command.contains("9x")) settings.boardSize = 9;
		if(command.contains("13")) settings.boardSize = 13;
		if(command.contains("19")) settings.boardSize = 19;
	} // end changeSettings


} // end ClientHandler CLASS