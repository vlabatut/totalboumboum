package org.totalboumboum.ai.v200910.adapter.path.astar.cost;

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

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;

/**
 * 
 * permet de définir une fonction de cout utilisée lors de la recherche
 * avec l'algorithme A*
 * 
 * @author Vincent Labatut
 *
 */
public abstract class CostCalculator
{
	/** 
	 * calcule le cout de l'action consistant à aller de la case
	 * start à la case end, sachant que ces deux cases sont voisines.
	 * Il est possible de définir des couts �volu�s, en tenant compte par exemple des
	 * influences n�gatives dans ces cases (pour le joueur) comme la prèsence de bombes 
	 * à proximit�, etc., et des influences positives telles que la prèsence de bonus.
	 * Si les deux cases ne sont pas voisines, le r�sultat est indétermin�.
	 * 
	 * @param start	la case de départ 
	 * @param end	la case d'arrivée (qui doit être voisine)
	 * @return	le coût du déplacement
	 */
	public abstract double processCost(AiTile start, AiTile end) throws StopRequestException;
	
	/**
	 * calcule le cout d'un chemin, i.e. la somme des couts des actions
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
