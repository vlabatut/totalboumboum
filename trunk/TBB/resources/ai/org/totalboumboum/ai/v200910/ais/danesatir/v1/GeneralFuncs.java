package org.totalboumboum.ai.v200910.ais.danesatir.v1;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * 
 * @version 1
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
public class GeneralFuncs {
	public static boolean tileCompare(AiTile tile1,AiTile tile2){
		if (tile1.getCol() == tile2.getCol() && tile1.getLine() == tile2.getLine())
			return true;
		return false;
	} 
	public static double getTimeToExplode(AiBomb a) {
		// make more accurate
		if( a.getNormalDuration() - a.getTime() < 0)
			return Limits.expandBombTime;
		return a.getNormalDuration() - a.getTime();
	}
	public static void printBombs(List<AiBomb> bombs) {
		for(AiBomb i : bombs) {
			//System.out.println(i);
			System.out.println("col:"+i.getCol()+" line:"+i.getLine()+" dur:"+getTimeToExplode(i));
		}
	}
	public static boolean checkVerboseLevel(int verbose) {
		// make Verbose enums
		if (Limits.debug && Limits.verboseLevel > verbose)
			return true;
		return false;
	}
	public static void printMatrice(int line, int col, double[][] matrice) {
		System.out.println("Matrice");
		for(int i=0;i<col;i++) {
			for(int j=0;j<line;j++)
				System.out.print(matrice[i][j]+" ");
			System.out.println("");
		}
	}
}
