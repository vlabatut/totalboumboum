package org.totalboumboum.ai.v200910.ais.adatepeozbek.v4;

public class Debug
{
	private static boolean debug = false;
	
	public static void write(String str)
	{
		if(debug)
			System.out.print(str);
	}
	
	public static void writeln(String str)
	{
		if(debug)
			System.out.println(str);
	}
		
}