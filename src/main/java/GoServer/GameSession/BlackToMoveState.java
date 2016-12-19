package GoServer.GameSession;

public class BlackToMoveState implements GameSessionStateBehavior {

	public GameSessionState getState() {
		return GameSessionState.BlackToMove;
	}

	public GameSessionStateBehavior blackMoved() {
		return GameSessionState.WhiteToMove.getStateBehavior();
	}

	public GameSessionStateBehavior whiteMoved() {
		return this;
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
