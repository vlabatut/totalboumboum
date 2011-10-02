package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v2;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;

/**
 * @author Onur Büyüktopaç
 * @author Yiğit Turak
 */
@SuppressWarnings("deprecation")
public class AStarCost extends CostCalculator{

	private double[][] matrix;
	
	public AStarCost(double[][] matrix) throws StopRequestException{
		this.matrix=matrix;
	}

	//pour utiliser Astar, on calcule la version basic de la cost
	public double processCost(AiTile start, AiTile end) throws StopRequestException {
		double resultat;
		if(matrix==null)
			resultat=1;
		else
		{
			if(matrix[start.getLine()][start.getCol()]<0)
				resultat=10;
			else if (matrix[start.getLine()][start.getCol()]<10)
				resultat=4;
			else if (matrix[start.getLine()][start.getCol()]<30)
				resultat=3;
			else
				resultat=2; 
		}
		return resultat;
	}
	
}
