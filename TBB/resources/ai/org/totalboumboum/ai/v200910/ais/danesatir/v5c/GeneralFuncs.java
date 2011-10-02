package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * 
 * @version 5.c
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class GeneralFuncs {
	public static boolean tileCompare(AiTile tile1,AiTile tile2,ArtificialIntelligence ai) throws StopRequestException{
		ai.checkInterruption();
		if (tile1.getCol() == tile2.getCol() && tile1.getLine() == tile2.getLine())
			return true;
		return false;
	}
	/**
	 * Print Bomb List
	 * @param bombs
	 * @throws StopRequestException 
	 */
	public static void printBombs(List<AiBomb> bombs,ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		for(AiBomb i : bombs) {
			ai.checkInterruption();
			//System.out.println(i);
			System.out.println("col:"+i.getCol()+" line:"+i.getLine()+" dur:"+i.getNormalDuration());
		}
	}
	
	/**
	 * Print Matrices with line and column size.
	 * @param line
	 * @param col
	 * @param matrice
	 * @throws StopRequestException 
	 */
	public static void printMatrice(int line, int col, double[][] matrice,ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		System.out.println("Matrice");
		for(int i=0;i<col;i++) {
			ai.checkInterruption();
			for(int j=0;j<line;j++)
			{	ai.checkInterruption();			
				System.out.print(matrice[i][j]+" ");
			}
			System.out.println("");
		}
	}
	public static void printLog(DaneSatir ai,String str,VerboseLevel v) throws StopRequestException {
		ai.checkInterruption();
		//printLog(ai.getPercepts().getTotalTime()+str,v);
		printLog(str,v,ai);
	}
	public static void printLog(String str,VerboseLevel v,ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		if(Limits.verbose==true && Limits.verboseLevel == v || v == VerboseLevel.ALWAYS)
			System.out.println(str);
	}
}
