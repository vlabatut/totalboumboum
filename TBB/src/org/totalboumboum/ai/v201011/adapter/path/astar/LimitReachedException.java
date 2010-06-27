package org.totalboumboum.ai.v201011.adapter.path.astar;

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

import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiTile;


/**
 * exception levée par l'algorithme A* quand il atteint une des limites fixées au préalable.
 * On ne peut donc pas conclure quant à l'existence d'un un chemin solution. 
 * 
 * @author Vincent Labatut
 *
 */

public final class LimitReachedException extends Exception
{	private static final long serialVersionUID = 1L;

	public LimitReachedException(AiTile startTile, List<AiTile> endTiles, int height, double cost, int size, double maxCost, int maxHeight, int maxSize)
	{	this.startTile = startTile;
		this.endTiles = endTiles;
		this.height = height;
		this.cost = cost;
		this.size = size;
		this.maxCost = maxCost;
		this.maxHeight = maxHeight;
		this.maxSize = maxSize;	
	}
	
    /////////////////////////////////////////////////////////////////
	// START TILES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	private AiTile startTile;
	
    public AiTile getStartTile()
    {	return startTile;
	}

	/////////////////////////////////////////////////////////////////
	// END TILES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	private List<AiTile> endTiles;
	
	public List<AiTile> getEndTiles()
	{	return endTiles;
	}

	/////////////////////////////////////////////////////////////////
	// HEIGHT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de hauteur (négatif = pas de limite) */
	private int maxHeight;
	/** hauteur maximale atteinte */
	private int height;

	public int getMaxHeight()
	{	return maxHeight;
	}

	public int getHeight()
	{	return height;
	}

	/////////////////////////////////////////////////////////////////
	// COST				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de coût (négatif = pas de limite) */
	private double maxCost;
	/** coût maximal atteint */
	private double cost;

	public double getMaxCost()
	{	return maxCost;
	}

	public double getCost()
	{	return cost;
	}

	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de taille exprimée en nombre de noeuds (négatif = pas de limite) */
	private int maxSize;
	/** taille maximale atteinte */
	private int size;

	public int getMaxSize()
	{	return maxSize;
	}

	public int getSize()
	{	return size;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = "";
		result = result + "height=" + height + "(" + maxHeight + ") ";
		result = result + "cost="   + cost   + "(" + maxCost   + ") ";
		result = result + "size="   + size   + "(" + maxSize   + ") ";
		result = result + "src="    + startTile + " ";
		result = result + "trgt=";
		for(AiTile tile: endTiles)
			result = result + tile + " ";
		result = result + "\n";
		return result;
	}
}
