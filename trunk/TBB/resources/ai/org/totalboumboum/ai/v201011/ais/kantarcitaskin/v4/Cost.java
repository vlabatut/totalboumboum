package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v4;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;

public class Cost extends CostCalculator
{
	/**
	 * Classe de la fonction de cout et utilisé par A* pour determiner le chemin plus courts 
	 * d'un cible
	 * 
	 */
	double[][] matrix;
	/**
	 * Constructeur de la classe 
	 * @param matrix
	 * 		zone numerique du jeu
	 * */
	public Cost(double[][] matrix)
	{
		this.matrix = matrix;
	}

	@Override
	/**
	 * Une version basic de la fonction de cout
	 * @param start
	 * 		le debut de chemin
	 * @param end
	 * 		la case cible
	 * 
	 * @return cost
	 * 		le cost du chemin trouv�
	 * 
	 * */
	public double processCost(AiTile start, AiTile end)throws StopRequestException {
		double resultat;
		if(matrix==null)
			resultat = 1;
		else
		{
			if(matrix[start.getLine()][start.getCol()]<0)
				resultat = 200;
			else
			{
				if(matrix[start.getLine()][start.getCol()]<40)
					resultat = 2;
				else
					resultat = 1;
			}
				
		}
		
		return resultat;
	}
	
	
}
