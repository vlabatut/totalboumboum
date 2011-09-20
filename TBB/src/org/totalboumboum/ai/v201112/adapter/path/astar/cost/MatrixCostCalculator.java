package org.totalboumboum.ai.v201112.adapter.path.astar.cost;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * Classe �tendant la classe abstraite CostCalculator grâce à une matrice de coûts.
 * Ici, le coût pour passer d'une case à l'autre dépend uniquement de la case
 * de destination. Ce coût est égal à la valeur associée à la case dans la matrice
 * de cout fournie. Cette matrice doit faire la même taille que la zone de jeu.
 * En d'autres termes, le coût d'un déplacement dépend ici uniquement de la case de destination.
 * </br>
 * Cette classe est utile si on veut calculer des coûts plus fins qu'avec BasicCostCalculator,
 * qui considère seulement la distance. Par exemple, on peut donner un coup plus important
 * à l'action de passer dans une case qui est à portée d'une bombe susceptible d'exploser, ou bien 
 * un coût infini (avec Double.POSITIVE_INFINITY) à l'action de passer dans une case qu'on veut interdire
 * au personnage parce qu'elle est trop dangereuse. 
 * 
 * @author Vincent Labatut
 *
 */
public class MatrixCostCalculator extends CostCalculator
{
	/**
	 * initialise la fonction de coût. On doit obligatoirement
	 * fournir la matrice de cout correspondante.
	 * <b>ATTENTION :</b> cette matrice doit avoir la même taille que la zone de jeu.
	 * 
	 * @param costMatrix	
	 * 		la matrice de coût
	 */
	public MatrixCostCalculator(double costMatrix[][]) throws StopRequestException
	{	setCostMatrix(costMatrix);			
	}
	
	/////////////////////////////////////////////////////////////////
	// COST MATRIX		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice utilisée pour déterminer le coût d'une action */
	private double costMatrix[][];
	
	/**
	 * initialise la matrice de coût. 
	 * <b>ATTENTION :</b>cette matrice doit avoir la même taille que la zone de jeu.
	 * 
	 * @param costMatrix	
	 * 		la matrice de coût
	 */
	public void setCostMatrix(double costMatrix[][])
	{	this.costMatrix = costMatrix;		
	}
	
	/**
	 * met à jour un coût dans la matrice
	 * 
	 * @param line	
	 * 		ligne de la case à mettre à jour
	 * @param col	
	 * 		colonne de la case à mettre à jour
	 * @param cost	
	 * 		nouveau coût à affecter
	 */
	public void setCost(int line, int col, double cost) throws StopRequestException
	{	costMatrix[line][col] = cost;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * La case de départ n'est pas considérée, on renvoie seulement la valeur
	 * correspondant à la case d'arrivée dans la matrice de coût.
	 * <b>ATTENTION :</b> si la matrice de coût est trop petite, la valeur maximale
	 * possible est renvoyée (Double.POSITIVE_INFINITY), et un message 
	 * d'avertissement est affiché dans la sortie standard d'erreur.
	 * 
	 * @param start	
	 * 		la case de départ
	 * @param end	
	 * 		la case d'arrivée
	 * @return 
	 * 		le coût correspondant à la case d'arrivée dans la matrice de coût
	 */ 
	@Override
	public double processCost(AiTile start, AiTile end) throws StopRequestException
	{	int col = end.getCol();
		int line = end.getLine();
		double result = Double.POSITIVE_INFINITY;
		if(line<costMatrix.length && col<costMatrix[0].length)
			result = costMatrix[line][col];
		return result;
	}
}
