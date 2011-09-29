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
import org.totalboumboum.ai.v201112.adapter.path.AiPath;

/**
 * Permet de définir une fonction de coût 
 * utilisée lors de la recherche
 * avec l'algorithme A*.
 * 
 * @author Vincent Labatut
 */
public abstract class CostCalculator
{
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Calcule le coût de l'action consistant à aller de la case
	 * start à la case end, sachant que ces deux cases sont voisines.
	 * Il est possible de définir des coûts évolués, en tenant compte par exemple des
	 * influences négatives dans ces cases (pour le joueur) comme la prèsence de bombes 
	 * à proximité, etc., et des influences positives telles que la prèsence de bonus.
	 * Si les deux cases ne sont pas voisines, le résultat est indéterminé.
	 * 
	 * @param previous
	 * 		La case précédente.
	 * @param current
	 * 		La case courante (voisine de la précédente). 
	 * @param next	
	 * 		La case suivante (voisine de la courante).
	 * @return	
	 * 		Le coût du déplacement entre la case courante et la case suivante.
	 */
	public abstract double processCost(AiTile previous, AiTile current, AiTile next) throws StopRequestException;
	
	/**
	 * Calcule le coût d'un chemin, i.e. la somme des coûts des actions
	 * consistant à passer d'une case du chemin à la suivante.
	 * 
	 * @param path
	 * 		Chemin à traiter
	 * @return
	 * 		Le coût de ce chemin.
	 */
	public double processCost(AiPath path) throws StopRequestException
	{	double result = 0;
		AiTile previous = null;
		AiTile preprevious = null;
		
		for(AiTile tile: path.getTiles())
		{	if(previous==null)
				previous = tile;
			else
			{	double localCost = processCost(preprevious,previous,tile);
				result = result + localCost;
				preprevious = previous;
				previous = tile;
			}			
		}
		return result;
	}
}
