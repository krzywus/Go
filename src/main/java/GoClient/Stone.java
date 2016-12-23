package GoClient;

/** Klasa kamieni stawianych na planszy.*/
public class Stone {
	
	/** Aktualny kolor kamienia. */
	public char color;				// N - no alignment, B - black, W - white
	/** Pozycja kamienia na planszy. */
	protected int positionX, positionY;	
	/** Liczba oddechow kamienia. */
	public int liberty;	
	
/*-------------------------------------------------------------------------------------------------------------------*/

	
	/** Konstruktor klasy. */
	public Stone(char color, int x, int y){
		this.color = color;
		positionX = x;
		positionY = y;
		liberty = -1; 
	} // end Stone constructor
	
	/** Metoda akceptujaca wizytora ( wzorzec VISITOR ).*/
	public void accept(LibertyVisitor visitor){
		visitor.visit(this);
	} // end countLiberty
	
}
