package fr.free.totalboumboum.configuration.game.quickmatch;

public enum QuickMatchDraw
{
	NONE,BOTH,TIME,AUTOKILL;
	
	public static QuickMatchDraw getNext(QuickMatchDraw value)
	{	QuickMatchDraw result = null;
		if(value==NONE)
			result = BOTH;
		else if(value==BOTH)
			result = TIME;
		else if(value==TIME)
			result = AUTOKILL;
		else if(value==AUTOKILL)
			result = NONE;
		return result;
	}
}
