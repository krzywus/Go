package GoServer.GameSession;

public class TerritoryBargainState implements GameSessionStateBehavior {

	public GameSessionState getState() {
		return GameSessionState.TerritoryBargain;
	}

	public GameSessionStateBehavior blackMoved() {
		return this;
	}

	public GameSessionStateBehavior whiteMoved() {
		return this;
	}

	public GameSessionStateBehavior territoryBargain() {
		return this;
	}

	public GameSessionStateBehavior gameFinished() {
		return GameSessionState.GameFinished.getStateBehavior();
	}

	public GameSessionStateBehavior gameAborted() {
		return GameSessionState.GameAborted.getStateBehavior();
	}

}
