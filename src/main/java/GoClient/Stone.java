package GoClient;

public class Stone {

	public char color;			// N - no alignment, B - black, W - white
	private int positionX, positionY;	// do puszczenia rekurencji do sasiednich kamieni w tym samym kolorze
	private int liberty;		// liczba oddechow kamienia. mozliwe, ze niepotrzebne.
	private GameBoard board;	// do uruchomienia rekurencji w countLiberty?
	
/*-------------------------------------------------------------------------------------------------------------------*/

	
	/** Konstruktor klasy. */
	Stone(char color, int x, int y, GameBoard board){
		this.color = color;
		this.board = board;
		positionX = x;
		positionY = y;
		liberty = countLiberty();
	} // end Stone constructor
	
	/** Metoda liczaca ile oddechow ma dany kamien. 
	 * TODO: zaimplementowac 
	 * ( prawdopodobnie rekurencyjnie sprawdzic ile wolnego maja kamienie tego samego koloru
	 * sasiadujace z tym kamieniem )
	 * MOZE wzorzec VISITOR*/
	private int countLiberty(){
		return 0;
	} // end countLiberty
	
	/** Metoda zwracajaca ilosc oddechow kamienia. */
	public int getLiberty(){
		return countLiberty();
	} // end getLiberty
	
}
