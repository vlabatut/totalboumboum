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
 * permet de d�finir une fonction de co�t utilis�e lors de la recherche
 * avec l'algorithme A*
 * 
 * @author Vincent Labatut
 *
 */
public abstract class CostCalculator
{
	/** 
	 * calcule le co�t de l'action consistant � aller de la case
	 * start � la case end, sachant que ces deux cases sont voisines.
	 * Il est possible de d�finir des co�ts �volu�s, en tenant compte par exemple des
	 * influences n�gatives dans ces cases (pour le joueur) comme la pr�sence de bombes 
	 * � proximit�, etc., et des influences positives telles que la pr�sence de bonus.
	 * Si les deux cases ne sont pas voisines, le r�sultat est ind�termin�.
	 * 
	 * @param start	la case de d�part 
	 * @param end	la case d'arriv�e (qui doit �tre voisine)
	 * @return	le co�t du d�placement
	 */
	public abstract double processCost(AiTile start, AiTile end) throws StopRequestException;
	
	/**
	 * calcule le co�t d'un chemin, i.e. la somme des co�ts des actions
	 * consistant � passer d'une case du chemin � la suivante.
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
