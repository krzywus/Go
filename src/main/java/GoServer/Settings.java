package GoServer;


/** Klasa przechowujaca ustawienia gry dla konkretnego klienta. 
 * TODO:Jezeli nie bedzie tu wiecej, to lepiej trzymac te pola po prostu w kliencie po stronie serwera*/
public class Settings {
	
	public int boardSize;
	public short opponentType; // 0 - other client, 1 - bot,  2 - hotseat player?
	
/*-------------------------------------------------------------------------------------------------------------------*/

	/** */
	Settings(){
		boardSize = 9;
		opponentType = 1;
	} // end Options constructor
}
