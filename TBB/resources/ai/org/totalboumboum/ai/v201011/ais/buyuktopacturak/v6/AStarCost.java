package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v6;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;

public class AStarCost extends CostCalculator{

	private BuyuktopacTurak bt;
	private double[][] matrix;
	
	/**
	 * C�est un constructeur qui obtient la matrice de la mode.
	 * @param matrix
	 * @throws StopRequestException
	 */
	public AStarCost(BuyuktopacTurak bt, double[][] matrix) throws StopRequestException{
		bt.checkInterruption();
		this.bt=bt;
		this.matrix=matrix;
	}

	/**
	 * Calcule le cout de l�action consistant à aller de la case d�part à la case arr�t.
	 */
	public double processCost(AiTile start, AiTile end) throws StopRequestException {
		bt.checkInterruption();
		//pour utiliser Astar, on calcule la version basic de la cost
		double resultat;
		if(matrix==null)
			resultat=1;
		else
		{
			if(matrix[end.getLine()][end.getCol()]<-600)
				resultat=200;
			else if (matrix[end.getLine()][end.getCol()]<-300)
				resultat=100;
			else if (matrix[end.getLine()][end.getCol()]<-50)
				resultat=30;
			else if (matrix[end.getLine()][end.getCol()]<50)
				resultat=20;
			else if (matrix[end.getLine()][end.getCol()]<100)
				resultat=15;
			else if (matrix[end.getLine()][end.getCol()]<150)
				resultat=10;
			else if (matrix[end.getLine()][end.getCol()]<200)
				resultat=6;
			else
				resultat=1; 
		}

		return resultat;
	}
	
}
