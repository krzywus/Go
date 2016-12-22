package GoServer.GameSession;


public enum GameSessionState {	
	
	
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
	},
	TerritoryBargain {
		public GameSessionStateBehavior getStateBehavior() {
			return new TerritoryBargainState();
		}
	};
 
	/** Default	 */
	public GameSessionStateBehavior getStateBehavior() {
		return null;
	}

}
