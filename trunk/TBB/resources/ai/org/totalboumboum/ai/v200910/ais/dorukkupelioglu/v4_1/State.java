package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_1;

/**
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
 */
public class State {
	
	public static int FIRE=17; //patlamış bomba etkisi	
	public static int BOMB=16;//12
	public static int INDESTRUCTIBLE=15;	
	public static int DESTWILLEXPLODE=14;
	public static int DESTRUCTIBLE=13;
	public static int RIVALDANGERR=12;
	public static int BONUSDANGERR=11; //kumandalı bomba etki alanındaki bonus
	public static int DANGERR=10; //patlamamış kumandalı bomba etkisi
	public static int RIVALDANGER=9;
	public static int BONUSDANGER=8; //bomba etki alanındaki bonus  
	public static int DANGER=7; //patlamamış sayaçlı bomba etkisi
	public static int MALUS=6;//2 ekle
	public static int RIVAL=5;		
	public static int BONUS=4; 
	public static int FREEINDESTRUCTIBLE=3; //hero indest ten geçebiliosa
	public static int FREEDESTRUCTIBLE=2; //hero destructible den geçebiliosa
	public static int FREE=1;
}
