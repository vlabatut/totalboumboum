package org.totalboumboum.ai.v200910.ais.danesatir.v5;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * 
 * @version 5
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class GeneralFuncs {
	
	/**
	 * 
	 * @param tile1
	 * 		Description manquante !
	 * @param tile2
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static boolean tileCompare(AiTile tile1,AiTile tile2){
		if (tile1.getCol() == tile2.getCol() && tile1.getLine() == tile2.getLine())
			return true;
		return false;
	}
	
	/**
	 * Print Bomb List
	 * @param bombs
	 * 		Description manquante !
	 */
	public static void printBombs(List<AiBomb> bombs) {
		for(AiBomb i : bombs) {
			//System.out.println(i);
			System.out.println("col:"+i.getCol()+" line:"+i.getLine()+" dur:"+i.getNormalDuration());
		}
	}
	
	/**
	 * Print Matrices with line and column size.
	 * @param line
	 * 		Description manquante !
	 * @param col
	 * 		Description manquante !
	 * @param matrice
	 * 		Description manquante !
	 */
	public static void printMatrice(int line, int col, double[][] matrice) {
		System.out.println("Matrice");
		for(int i=0;i<col;i++) {
			for(int j=0;j<line;j++)
				System.out.print(matrice[i][j]+" ");
			System.out.println("");
		}
	}
	
	/**
	 * 
	 * @param ai
	 * 		Description manquante !
	 * @param str
	 * 		Description manquante !
	 * @param v
	 * 		Description manquante !
	 */
	public static void printLog(DaneSatir ai,String str,VerboseLevel v) {
		//printLog(ai.getPercepts().getTotalTime()+str,v);
		printLog(str,v);
	}
	
	/**
	 * 
	 * @param str
	 * 		Description manquante !
	 * @param v
	 * 		Description manquante !
	 */
	public static void printLog(String str,VerboseLevel v) {
		if(Limits.verbose==true && Limits.verboseLevel == v || v == VerboseLevel.ALWAYS)
			System.out.println(str);
	}
}
