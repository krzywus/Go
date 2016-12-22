package GoServer;

import java.io.IOException;

import GoClient.GoClient;
import junit.framework.TestCase;

public class GoServerTest extends TestCase{
	
	private GoServer goServer = null;
	
	public void setUp(){
		goServer = new GoServer();
	}
	
	public void testConstructor(){
		assertNotNull(goServer);
	}
	
	public void testListening(){
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
		GoClient client = new GoClient();
		client.listenSocket();
		assertEquals(1, GoServer.clients.size());
		GoServer.clients.clear(); 
	}
	
	public void testListeningWhenManyClientJoin(){
		/* limited to small ammount as running on slow machine wont allow more 
		 * to join before server shutdown ( each client is a thread + each clientHandler is a thread)
		 * which slows down the process.	*/
		final int testNumber = 5; Thread thread = new Thread(){
			@Override
			public void run(){
				try {
					goServer.startListeningInTestEnvironment(testNumber);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		GoClient client[] = new GoClient[testNumber];
		for(GoClient c: client){
			c = new GoClient();
			c.listenSocket();
		}
		assertEquals(testNumber, GoServer.clients.size()); 
		GoServer.clients.clear();
	}
	
	public void tearDown(){	
		try {	
			Thread.sleep(1000); // to prevent Java VM from clearing clients references
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		goServer.serverShutdown();
	}
	
	
}
