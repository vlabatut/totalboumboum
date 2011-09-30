package org.totalboumboum.ai.v201112.adapter.path.astar.heuristic;

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

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.astar.AstarLocation;

/**
 * Implémentation la plus simple d'une heuristique : 
 * on utilise la distance de Manhattan entre la case de départ et 
 * la plus proche des cases d'arrivée.<br/>
 * <b>Attention :<b/> cette classe ne permet pas de gérer des
 * chemins contenant des attentes. De plus les distances sont
 * calculées en cases, et non pas en pixels : rapidement calculé,
 * mais approximatif. 
 * 
 * @author Vincent Labatut
 */
public class BasicHeuristicCalculator extends HeuristicCalculator
{
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * l'heuristique la plus simple consiste à prendre la distance
	 * de Manhattan entre la case courante tile et la case d'arrivée endTile.
	 * cf. <a href="http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29#Distance_sur_des_espaces_vectoriels">Wikipedia</a>
	 * 
	 * @param location	
	 * 		L'emplacement concerné. 
	 * @return	
	 * 		la distance de Manhattan entre l'emplacement passé en paramètre
	 * 		et la plus proche des cases contenues dans le champ {@code endTiles}.
	 */
	@Override
	public double processHeuristic(AstarLocation location) throws StopRequestException
	{	// init
		AiTile startTile = location.getTile();
		List<AiTile> endTiles = getEndTiles();
		AiZone zone = location.getZone();
		double result = Integer.MAX_VALUE;
		
		// process
		for(AiTile endTile: endTiles)
		{	int dist = zone.getTileDistance(startTile,endTile);
			if(dist<result)
				result = dist;
		}
		
		return result;
	}
}