package GoClient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Klasa obslugujaca przyciski glowne okna.
 * Umozliwia wylaczenie okna.
 * @author Filip Krzywka
 */
public class WindowClose extends WindowAdapter {
	
	private GoClient client;
	
	WindowClose(GoClient client){
		super();
		this.client = client;
	}
	
	/** Metoda obslugujaca zamkniecie okna. 
	 *  @param e Zdarzenie, ktore wywolalo metode.
	 */
	public void windowClosing(WindowEvent e){ 
		client.out.println("EXIT");
		System.exit(0);
	}
	
	
}
