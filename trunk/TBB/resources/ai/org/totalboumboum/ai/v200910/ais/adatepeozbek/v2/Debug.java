package org.totalboumboum.ai.v200910.ais.adatepeozbek.v2;

/**
 * 
 * @version 2
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
public class Debug
{
	private static boolean debug = false;
	
	public static void Write(String str)
	{
		if(debug)
			System.out.print(str);
	}
	
	public static void Writeln(String str)
	{
		if(debug)
			System.out.println(str);
	}
		
}