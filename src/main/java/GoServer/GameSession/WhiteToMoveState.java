package GoServer.GameSession;

public class WhiteToMoveState implements GameSessionStateBehavior{

	public GameSessionState getState() {
		return GameSessionState.WhiteToMove;
	}

	public GameSessionStateBehavior blackMoved() {
		return this;
	}

	public GameSessionStateBehavior whiteMoved() {
		return GameSessionState.BlackToMove.getStateBehavior();
	}

	public GameSessionStateBehavior gameFinished() {
		return this;
	}

	public GameSessionStateBehavior gameAborted() {
		return GameSessionState.GameAborted.getStateBehavior();
	}

	public GameSessionStateBehavior territoryBargain() {
		return GameSessionState.TerritoryBargain.getStateBehavior();
	}

}
