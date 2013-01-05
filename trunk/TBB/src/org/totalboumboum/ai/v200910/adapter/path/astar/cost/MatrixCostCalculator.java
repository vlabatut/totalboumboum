package org.totalboumboum.ai.v200910.adapter.path.astar.cost;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * 
 * Classe étendant la classe abstraite CostCalculator grâce à une matrice de coûts.
 * Ici, le cout pour passer d'une case à l'autre dépend uniquement de la case
 * de destination. Ce cout est égal à la valeur associée à la case dans la matrice
 * de cout fournie. Cette matrice doit faire la même taille que la zone de jeu.
 * <br/>
 * Cette classe est utilie si on veut calculer des couts plus fins qu'avec BasicCostCalculator,
 * qui considère seulement la distance. Par exemple, on peut donner un coup plus important
 * aux cases qui sont à portée d'une bombe susceptibles d'exploser, ou bien 
 * un cout infini (avec Double.POSITIVE_INFINITY) aux cases qu'on veut interdire
 * au personnage parce qu'elles sont trop dangereuses. 
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public class MatrixCostCalculator extends CostCalculator
{
	/**
	 * initialise la fonction de cout. On doit obligatoirement
	 * fournir la matrice de cout correspondante.
	 * Attention : cette matrice doit avoir la même taille que la zone de jeu.
	 * 
	 * @param costMatrix	la matrice de cout
	 * @throws StopRequestException 
	 */
	public MatrixCostCalculator(double costMatrix[][]) throws StopRequestException
	{	setCostMatrix(costMatrix);			
	}
	
	/////////////////////////////////////////////////////////////////
	// COST MATRIX		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice de cout utilisée pour déterminer le cout d'une action */
	private double costMatrix[][];
	
	/**
	 * initialise la matrice de cout. Attention : cette matrice doit avoir
	 * la même taille que la zone de jeu.
	 * 
	 * @param costMatrix	la matrice de cout
	 * 
	 * @throws StopRequestException 
	 */
	public void setCostMatrix(double costMatrix[][]) throws StopRequestException
	{	this.costMatrix = costMatrix;		
	}
	
	/**
	 * met à jour un coût dans la matrice
	 * 
	 * @param line	ligne de la case à mettre à jour
	 * @param col	colonne de la case à mettre à jour
	 * @param cost	nouveau coût à affecter
	 * 
	 * @throws StopRequestException 
	 */
	public void setCost(int line, int col, double cost) throws StopRequestException
	{	costMatrix[line][col] = cost;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * La case de départ n'est pas considérée, on renvoie seulement la valeur
	 * correspondant à la case d'arrivée dans la matrice de cout.
	 * Attention : si la matrice de cout est trop petite, la valeur maximale
	 * possible est renvoyée (Double.POSITIVE_INFINITY), et un message 
	 * d'avertissement est affiché dans la sortie standard d'erreur.
	 * 
	 * @param start	la case de départ
	 * @param end	la case d'arrivée
	 * @return le cout correspondant à la case d'arrivée dans la matrice de cout
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
