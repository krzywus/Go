package GoServer.GameSession;

public interface GameSessionStateBehavior {

	/** Get current state */
	GameSessionState getState();
	
	GameSessionStateBehavior blackMoved();
	
	GameSessionStateBehavior whiteMoved();

	GameSessionStateBehavior territoryBargain();
	
	GameSessionStateBehavior gameFinished();
	
	GameSessionStateBehavior gameAborted();
	
	
}
