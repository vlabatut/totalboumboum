package org.totalboumboum.ai.v201011.adapter.path.astar.cost;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * permet de définir une fonction de coût utilisée lors de la recherche
 * avec l'algorithme A*
 * 
 * @author Vincent Labatut
 *
 */
public abstract class CostCalculator
{
	/** 
	 * calcule le coût de l'action consistant à aller de la case
	 * start à la case end, sachant que ces deux cases sont voisines.
	 * Il est possible de définir des coûts évolués, en tenant compte par exemple des
	 * influences négatives dans ces cases (pour le joueur) comme la présence de bombes 
	 * à proximité, etc., et des influences positives telles que la présence de bonus.
	 * Si les deux cases ne sont pas voisines, le résultat est indéterminé.
	 * 
	 * @param start	la case de départ 
	 * @param end	la case d'arrivée (qui doit être voisine)
	 * @return	le coût du déplacement
	 */
	public abstract double processCost(AiTile start, AiTile end) throws StopRequestException;
	
	/**
	 * calcule le coût d'un chemin, i.e. la somme des coûts des actions
	 * consistant à passer d'une case du chemin à la suivante.
	 * @param path
	 * @return
	 */
	public double processCost(AiPath path) throws StopRequestException
	{	double result = 0;
		AiTile previous = null;
		for(AiTile tile: path.getTiles())
		{	if(previous==null)
				previous = tile;
			else
			{	double localCost = processCost(previous,tile);
				result = result + localCost;
			}			
		}
		return result;
	}
}
