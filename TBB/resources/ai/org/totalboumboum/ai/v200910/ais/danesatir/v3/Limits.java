package org.totalboumboum.ai.v200910.ais.danesatir.v3;

/**
 * 
 * @version 3
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
public class Limits {
	public static double dangerLimit=200;
	public static double expandBombTime=190;
	//expandBombTime should low dangerLimit
	
	public static double bombDuration=0;
	public static double tileDistance=100;
	
	public static double defaultCost=10;
	
	//Debug values
	public static boolean verbose=false;
	public static VerboseLevel verboseLevel=VerboseLevel.HIGH;
}
