package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v4;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;

public class AStarCost extends CostCalculator{

	private double[][] matrix;
	
	/**
	 * C’est un constructeur qui obtient la matrice de la mode.
	 * @param matrix
	 * @throws StopRequestException
	 */
	public AStarCost(double[][] matrix) throws StopRequestException{
		this.matrix=matrix;
	}

	/**
	 * Calcule le cout de l’action consistant à aller de la case départ à la case arrêt.
	 */
	public double processCost(AiTile start, AiTile end) throws StopRequestException {
		//pour utiliser Astar, on calcule la version basic de la cost
		double resultat;
		if(matrix==null)
			resultat=1;
		else
		{
			if(matrix[start.getLine()][start.getCol()]<0)
				resultat=100;
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
