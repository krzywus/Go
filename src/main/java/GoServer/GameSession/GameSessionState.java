package GoServer.GameSession;


public enum GameSessionState {
	//TODO: Comment the following definitions 
	//OFF, ON, STOPPED, STARTED, UNKNOW;
	
	//TODO: Uncomment the following block of code
	
	
	
	WhiteToMove {
		public GameSessionStateBehavior getStateBehavior() {
			return new WhiteToMoveState();
		}
	},
	BlackToMove {
		public GameSessionStateBehavior getStateBehavior() {
			return new BlackToMoveState();
		}
	},
	GameFinished {
		public GameSessionStateBehavior getStateBehavior() {
			return new GameFinishedState();
		}
	},
	GameAborted {
		public GameSessionStateBehavior getStateBehavior() {
			return new GameAbortedState();
		}
	};
 
	/** Default	 */
	public GameSessionStateBehavior getStateBehavior() {
		return null;
	}

}
