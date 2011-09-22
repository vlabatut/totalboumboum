package org.totalboumboum.ai.v200910.ais.danesatir.v4;

/**
 * 
 * @version 4
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
public class Limits {
	public static double dangerLimit=200;
	public static double expandBombTime=300;
	//expandBombTime should low dangerLimit
	
	public static double bombDuration=0;
	public static double tileDistance=80;
	
	public static double defaultCost=10;
	
	//Debug values
	public static boolean verbose=false;
	public static VerboseLevel verboseLevel=VerboseLevel.HIGH;
	public static double bonusRate=0.5;
	public static double fireRate=5;
	public static int turnCount=7;
}
