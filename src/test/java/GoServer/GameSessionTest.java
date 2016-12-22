package GoServer;

import java.io.IOException;
import java.util.Iterator;

import GoClient.GoClient;
import GoServer.ClientHandler;
import GoServer.GoServer;
import GoServer.Matchmaker;
import GoServer.GameSession.GameSession;
import GoServer.GameSession.GameSessionState;
import junit.framework.TestCase;

public class GameSessionTest extends TestCase{
	
	
	private GoServer goServer = null;
	private ClientHandler handler1 = null;
	private ClientHandler handler2 = null;
	private GoClient player1 = null;
	private GoClient player2 = null;
	
	
	public void setUp(){
		System.out.println();
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
		player1 = new GoClient();
		player1.listenSocket();
		player2 = new GoClient();
		player2.listenSocket();
		Iterator<ClientHandler> i = GoServer.clients.iterator();
		handler1 = i.next();
		handler2 = i.next();
		try {
			Thread.sleep(1000); // to prevent Java VM from clearing clients references
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void testInit(){		
		Matchmaker matchmaker = Matchmaker.getInstance();
		assertNotNull(matchmaker);
	}
	
	public void testStartGame(){
		GameSession session = new GameSession(handler1, handler2);
		assertEquals(GameSessionState.BlackToMove, session.getState());
	}

	
	public void testChangeState(){
		GameSession session = new GameSession(handler1, handler2);
		assertEquals(GameSessionState.BlackToMove, session.getStateBehavior().getState());
		session.setState( session.getStateBehavior().blackMoved().getState() );
		assertEquals(GameSessionState.WhiteToMove, session.getStateBehavior().getState());
	}
	
	public void testChangeStatesDuringGame(){
		GameSession session = new GameSession(handler1, handler2);		
		assertEquals(GameSessionState.BlackToMove, session.getStateBehavior().getState());
		session.setState( session.getStateBehavior().blackMoved().getState() );				// black player moved
		assertEquals(GameSessionState.WhiteToMove, session.getStateBehavior().getState());
		session.setState( session.getStateBehavior().whiteMoved().getState() );				// white player moved
		assertEquals(GameSessionState.BlackToMove, session.getStateBehavior().getState());
		session.setState( session.getStateBehavior().territoryBargain().getState() );		// both players passed
		assertEquals(GameSessionState.TerritoryBargain, session.getStateBehavior().getState());
		session.setState( session.getStateBehavior().gameFinished().getState() );			// both players agreed on territory
		assertEquals(GameSessionState.GameFinished, session.getStateBehavior().getState());
	}
	
	public void testChangeStateAborted(){
		GameSession session = new GameSession(handler1, handler2);		
		assertEquals(GameSessionState.BlackToMove, session.getStateBehavior().getState());
		session.setState( session.getStateBehavior().blackMoved().getState() );				// black player moved
		assertEquals(GameSessionState.WhiteToMove, session.getStateBehavior().getState());
		session.setState( session.getStateBehavior().whiteMoved().getState() );				// white player moved
		assertEquals(GameSessionState.BlackToMove, session.getStateBehavior().getState());
		session.setState( session.getStateBehavior().gameAborted().getState() );			// black player aborted
		assertEquals(GameSessionState.GameAborted, session.getStateBehavior().getState());
	}
	
	public void testChangeStateWhileNotYourTurnToMove(){
		GameSession session = new GameSession(handler1, handler2);		
		assertEquals(GameSessionState.BlackToMove, session.getStateBehavior().getState());
		session.setState( session.getStateBehavior().blackMoved().getState() );				// black player moved
		assertEquals(GameSessionState.WhiteToMove, session.getStateBehavior().getState());
		session.setState( session.getStateBehavior().blackMoved().getState() );				// black player moved
		assertEquals(GameSessionState.WhiteToMove, session.getStateBehavior().getState());
	}
	
	public void tearDown(){
			goServer.serverShutdown();
	}
}
