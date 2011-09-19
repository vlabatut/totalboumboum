package org.totalboumboum.ai.v200910.adapter.path.astar.heuristic;

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

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

/**
 * 
 * impl�mentation la plus simple d'une heuristique : 
 * on utilise la distance de Manhattan entre la case de d�part et 
 * la plus proche des cases d'arriv�e.
 * 
 * @author Vincent Labatut
 *
 */
public class BasicHeuristicCalculator extends HeuristicCalculator
{
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * l'heuristique la plus simple consiste à prendre la distance
	 * de Manhattan entre la case courante tile et la case d'arriv�e endTile.
	 * cf. http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29#Distance_sur_des_espaces_vectoriels
	 * 
	 * @param tile	la case concern�e 
	 * @return	la distance de Manhattan entre tile et la plus proche des cases contenues dans endTiles
	 */
	@Override
	public double processHeuristic(AiTile tile) throws StopRequestException
	{	List<AiTile> endTiles = getEndTiles();
		AiZone zone = tile.getZone();
		double result = Integer.MAX_VALUE;
		for(AiTile endTile: endTiles)
		{	int dist = zone.getTileDistance(tile,endTile);
			if(dist<result)
				result = dist;
		}
		return result;
	}
}
