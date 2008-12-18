package fr.free.totalboumboum.configuration.game;

public enum QuickMatchDraw
{
	BOTH,TIME,AUTOKILL;
	
	public static QuickMatchDraw getNext(QuickMatchDraw value)
	{	QuickMatchDraw result;
		if(value==BOTH)
			result = TIME;
		else if(value==TIME)
			result = AUTOKILL;
		else
			result = BOTH;
		return result;
	}
}
