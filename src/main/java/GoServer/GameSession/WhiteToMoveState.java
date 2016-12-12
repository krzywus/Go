package GoServer.GameSession;

public class WhiteToMoveState implements GameSessionStateBehavior{

	public GameSessionState getState() {
		return GameSessionState.WhiteToMove;
	}

	public GameSessionStateBehavior blackMoved() {
		// TODO Auto-generated method stub
		return null;
	}

	public GameSessionStateBehavior whiteMoved() {
		// TODO Auto-generated method stub
		return null;
	}

	public GameSessionStateBehavior gameFinished() {
		// TODO Auto-generated method stub
		return null;
	}

	public GameSessionStateBehavior gameAborted() {
		// TODO Auto-generated method stub
		return null;
	}

}
