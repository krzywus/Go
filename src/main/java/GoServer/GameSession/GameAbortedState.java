package GoServer.GameSession;

public class GameAbortedState implements GameSessionStateBehavior{

	public GameSessionState getState() {
		return GameSessionState.GameAborted;
	}

	public GameSessionStateBehavior blackMoved(){
		return this;
	}

	public GameSessionStateBehavior whiteMoved(){
		return this;
	}

	public GameSessionStateBehavior gameFinished(){
		return GameSessionState.GameFinished.getStateBehavior();
	}

	public GameSessionStateBehavior gameAborted(){
		return GameSessionState.GameFinished.getStateBehavior();
	}

	public GameSessionStateBehavior territoryBargain() {
		return this;
	}

}
