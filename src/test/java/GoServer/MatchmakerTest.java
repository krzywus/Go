package GoServer;

import java.io.IOException;

import GoClient.GoClient;
import junit.framework.TestCase;

public class MatchmakerTest extends TestCase {

	private GoServer goServer = null;
	
	public void setUp(){
		System.out.println();
	}
	
	public void testInit(){		
		goServer = new GoServer();
		assertNotNull(goServer.matchmaker);
	}
	
	public void testStartGame(){
		goServer = new GoServer();
		Thread thread = new Thread(){
			@Override
			public void run(){
				try {
					goServer.startListeningInTestEnvironment(2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		GoClient player1 = new GoClient();
		player1.listenSocket();
		GoClient player2 = new GoClient();
		player2.listenSocket();
		int i = 0, gameSize = 9;
		for(ClientHandler handler: GoServer.clients ){
			if(i == 0)
				assertFalse( goServer.matchmaker.addPlayer(handler, gameSize, 0) );
			else
				assertTrue( goServer.matchmaker.addPlayer(handler, gameSize, 0) );
			i++;
		}
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
