package GoServer.Bot;


public class StickTogetherStrategy extends Strategy {

	
	StickTogetherStrategy(GoBot bot){
		super(bot);
	}// end Strategy constr
	
	public void setValues() {
		bot.StarPointValue	   = 5;
		bot.StickingStoneValue = 8;
		bot.AttackValue 	   = 1;
		bot.KillingStoneValue  = 4;
		bot.DefenceValue 	   = 3;	// should be low as algorithm is now based on it heavily
		bot.DiagonalMoveValue  = 7;
		bot.LongMoveValue	   = 1;
	}

}
