package tournament200910.adatepeozbek.v2;

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