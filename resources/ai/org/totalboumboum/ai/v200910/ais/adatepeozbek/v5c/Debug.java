package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5c;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

/**
 * 
 * @version 5.c
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
@SuppressWarnings("deprecation")
 class Debug
{
	/** */
	private static boolean debug = false;
	
	/**
	 * 
	 * @param str
	 * 		Description manquante !
	 * @param ownAi
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public static void write(String str, ArtificialIntelligence ownAi) throws StopRequestException
	{	ownAi.checkInterruption();
		if(debug)
			System.out.print(str);
	}
	
	/**
	 * 
	 * @param str
	 * 		Description manquante !
	 * @param ownAi
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public static void writeln(String str, ArtificialIntelligence ownAi) throws StopRequestException
	{	ownAi.checkInterruption();
		if(debug)
			System.out.println(str);
	}
		
}