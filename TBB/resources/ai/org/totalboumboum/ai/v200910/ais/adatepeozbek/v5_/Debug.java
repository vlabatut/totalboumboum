package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5_;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

public class Debug
{
	private static boolean debug = false;
	
	public static void write(String str, ArtificialIntelligence ownAi) throws StopRequestException
	{	ownAi.checkInterruption();
		if(debug)
			System.out.print(str);
	}
	
	public static void writeln(String str, ArtificialIntelligence ownAi) throws StopRequestException
	{	ownAi.checkInterruption();
		if(debug)
			System.out.println(str);
	}
		
}