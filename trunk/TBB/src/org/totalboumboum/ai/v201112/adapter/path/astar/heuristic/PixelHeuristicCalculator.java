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
 * Heuristique utilisant la distance de Manhattan exprimées en pixels,
 * pour aller avec PixelCostCalculator.<br/>
 * <b>Attention :<b/> cette classe ne permet pas de gérer des
 * chemins contenant des attentes. Par contre, à la différence
 * de {@link BasicHeuristicCalculator}, elle gère les distances
 * en pixels.
 * 
 * @author Vincent Labatut
 */
public class PixelHeuristicCalculator extends HeuristicCalculator
{
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * L'heuristique la plus simple consiste à prendre la distance
	 * de Manhattan entre la case courante tile et la case d'arrivée endTile.
	 * cf. <a href="http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29#Distance_sur_des_espaces_vectoriels">Wikipedia</a>.
	 * Ici, on calcule cette distance exprimée en pixels plutôt qu'en cases
	 * comme c'est le cas dans {@link BasicHeuristicCalculator}.
	 * 
	 * @param location	
	 * 		L'emplacement concerné. 
	 * @return	
	 * 		La distance de Manhattan entre l'emplacement passé en paramètre
	 * 		et la plus proche des cases contenues dans le champ {@code endTiles}.
	 */
	@Override
	public double processHeuristic(AstarLocation location) throws StopRequestException
	{	// init
		List<AiTile> endTiles = getEndTiles();
		AiZone zone = location.getZone();
		double startX = location.getPosX();
		double startY = location.getPosY();
		double result = Integer.MAX_VALUE;
		
		for(AiTile endTile: endTiles)
		{	//double endX = endTile.getPosX();
			//double endY = endTile.getPosY();
			double dist = zone.getPixelDistance(startX,startY,endTile);
			if(dist<result)
				result = dist;
		}
		
		return result;
	}
}
