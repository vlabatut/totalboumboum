package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v6;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
@SuppressWarnings("deprecation")
public class Cost extends CostCalculator
{
	ArtificialIntelligence ai;
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
	 * @param ai 
	 * @throws StopRequestException 
	 * */
	public Cost(double[][] matrix, ArtificialIntelligence ai) throws StopRequestException
	{
		ai.	checkInterruption();
		this.ai = ai;
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
	 * 		le cost du chemin trouvé
	 * 
	 * */
	public double processCost(AiTile start, AiTile end)throws StopRequestException {
		ai.	checkInterruption();
		double resultat;
		if(matrix==null)
			resultat = 1;
		if(matrix[start.getLine()][start.getCol()]<-200)
			resultat = 1000;
		else
		{
			if(matrix[start.getLine()][start.getCol()]<-100)
				resultat = 400;
			else
			{
				if(matrix[start.getLine()][start.getCol()]<0)
					resultat=300;
				else
				{
					if(matrix[start.getLine()][start.getCol()]==0 && matrix[start.getLine()][start.getCol()]>100)
						resultat = 0;
					else
					{
						if(matrix[start.getLine()][start.getCol()]<=40)
							resultat = 2;
						else
							resultat = 1;
					}
					
				}
				
			}
				
		}
		
		return resultat;
	}
	
	
}
