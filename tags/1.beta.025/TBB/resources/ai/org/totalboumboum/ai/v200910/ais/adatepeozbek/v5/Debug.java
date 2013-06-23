package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5;

/**
 * 
 * @version 5
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
public class Debug
{
	/** */
	private static boolean debug = false;
	
	/**
	 * 
	 * @param str
	 * 		Description manquante !
	 */
	public static void write(String str)
	{
		if(debug)
			System.out.print(str);
	}
	
	/***
	 * 
	 * @param str
	 * 		Description manquante !
	 */
	public static void writeln(String str)
	{
		if(debug)
			System.out.println(str);
	}
		
}