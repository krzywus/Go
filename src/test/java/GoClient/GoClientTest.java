package GoClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

import GoServer.GoServer;
import junit.framework.TestCase;

public class GoClientTest extends TestCase {

	private GoClient client = null;
	private GoServer goServer = null;
	
	public void setUp(){
		System.out.println();
		goServer = new GoServer();
		Thread thread = new Thread(){
			@Override
			public void run(){
				try {
					goServer.startListeningInTestEnvironment(1);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();	
	}
	
	
	public void testInit(){
		client = new GoClient();
		client.listenSocket();
		assertNotNull(client);
		assertNotNull(client.in);
		assertNotNull(client.out);	
	}
	
	public void testActionPerformed(){
		client = new GoClient();
		client.listenSocket();
		client.out = mock(PrintWriter.class);
		doNothing().when(client.out).println("SETTINGS");
		client.in = mock(BufferedReader.class);
		try {
			when(client.in.readLine()).thenReturn("OK");
		} catch (IOException e) { e.printStackTrace();}
		client.actionPerformed(new ActionEvent(this, 0 ,"Options"));
		verify(client.out).println("SETTINGS");
		try {
			verify(client.in).readLine();
		} catch (IOException e) { e.printStackTrace();}
	}
	
	public void testActionPerformedOnOpenBoard(){
		client = new GoClient();
		client.listenSocket();
		client.boardFrame = mock(BoardFrame.class);		// tested element
		client.listener = mock(ActionListener.class);		// tested element
		client.in = mock(BufferedReader.class);			// for mocking executeCommand
		client.actionPerformed(new ActionEvent(this, 0 ,"Start"));try {
			when(client.in.readLine()).thenReturn("OPEN BOARD PL 9x W");
		} catch (IOException e) { e.printStackTrace();}
		try {
			verify(client.in).readLine();
		} catch (IOException e) { e.printStackTrace();}
		assertNotNull(client.boardFrame); // boardFrame constructor is only in openBoard method	
		assertNotNull(client.listener);   // listener constructor is only in startGame method	
	}
	

	public void tearDown(){		
		try {
		Thread.sleep(500); // to prevent Java VM from clearing clients references
			} catch (InterruptedException e) {
			e.printStackTrace();
		}
		goServer.serverShutdown();
	}
	
}
