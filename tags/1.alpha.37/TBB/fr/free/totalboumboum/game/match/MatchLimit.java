package fr.free.totalboumboum.game.match;

public enum MatchLimit
{
	ALL_LEVELS, ROUND_LIMIT, POINTS_LIMIT;
	
	public static MatchLimit getValueFromString(String str)
	{	MatchLimit result = null;
		if(str.equalsIgnoreCase("all-levels"))
			result = ALL_LEVELS;
		else if(str.equalsIgnoreCase("round-limit"))
			result = ROUND_LIMIT;
		else if(str.equalsIgnoreCase("points-limit"))
			result = POINTS_LIMIT;
		return result;
	}
}
