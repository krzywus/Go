package GoServer.Bot;

public class TerritoryGainStrategy extends Strategy{

	TerritoryGainStrategy(GoBot bot) {
		super(bot);
	}

	public void setValues() {
		bot.StarPointValue	   = 9;
		bot.StickingStoneValue = 1;
		bot.AttackValue 	   = 2;
		bot.KillingStoneValue  = 3;
		bot.DefenceValue 	   = 3;	// should be low as algorithm is now based on it heavily
		bot.DiagonalMoveValue  = 5;
		bot.LongMoveValue	   = 5;
	}

	
}
