package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v2;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
public class Cost extends CostCalculator
{
	int[][] matrix;
	public Cost(int[][] matrix)
	{
		this.matrix = matrix;
	}

	@Override
	//Une version basic de costConstructeur pour pouvoir utilise A*. A améliorer.
	public double processCost(AiTile start, AiTile end)throws StopRequestException {
		int resultat;
		if(matrix==null)
			resultat = 1;
		else
		{
			if(matrix[start.getLine()][start.getCol()]<0)
				resultat = -matrix[start.getLine()][start.getCol()];
			else
				resultat = matrix[start.getLine()][start.getCol()];
		}
			
		
		return resultat;
	}
	
	
}
