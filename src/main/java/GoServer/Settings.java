package GoServer;


/** Klasa przechowujaca ustawienia gry dla konkretnego klienta. */
public class Settings {
	
	public int boardSize;
	public short opponentType; // 0 - hotseat player, 1 - other client, 2 - bot
	
/*-------------------------------------------------------------------------------------------------------------------*/

	/** */
	Settings(){
		boardSize = 9;
		opponentType = 0;
	} // end Options constructor
}
