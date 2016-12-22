package GoServer;

import java.io.IOException;
import java.util.Iterator;


import GoClient.GoClient;
import junit.framework.TestCase;

public class ClientHandlerTest extends TestCase {

	private GoServer goServer = null;
	private GoClient client = null;
	private ClientHandler handler = null;
	
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
		client = new GoClient();
		client.listenSocket();
		Iterator<ClientHandler> i = GoServer.clients.iterator();
		handler = i.next();
	}
	
	public void testInit(){
		assertNotNull(handler);
	}
	
	public void testIsRunning(){ 
		for(ClientHandler handle: GoServer.clients) assertEquals(Thread.State.RUNNABLE, handle.getState() );
	}
	
	
	public void tearDown(){
		goServer.serverShutdown();
	}
}
