package GoServer.GameSession;

public class GameFinishedState implements GameSessionStateBehavior{

	public GameSessionState getState() {
		return GameSessionState.GameFinished;
	}

	public GameSessionStateBehavior blackMoved() {
		return this;
	}

	public GameSessionStateBehavior whiteMoved() {
		return this;
	}

	public GameSessionStateBehavior gameFinished() {
		return this;
	}

	public GameSessionStateBehavior gameAborted() {
		return this;
	}

	public GameSessionStateBehavior territoryBargain() {
		return this;
	}

}
