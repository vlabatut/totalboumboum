package org.totalboumboum.ai.v201011.adapter.path.astar.heuristic;

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

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * heuristique utilisant la distance de Manhattan exprimées en pixels,
 * pour aller avec PixelCostCalculator.
 * 
 * @author Vincent Labatut
 *
 */
public class PixelHeuristicCalculator extends HeuristicCalculator
{
	
	/////////////////////////////////////////////////////////////////
	// STARTING POINT			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** case de départ du chemin en cours de recherche */
	private AiTile startTile;
	/** abscisse de départ (doit être contenue dans la case de départ) */
	private double startX;
	/** ordonnée de départ (doit être contenue dans la case de départ) */
	private double startY;
	
	public void updateStartPoint(AiTile startTile, double startX, double startY)
	{	this.startTile = startTile;
		this.startX = startX;
		this.startY = startY;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * l'heuristique la plus simple consiste à prendre la distance
	 * de Manhattan entre la case courante tile et la case d'arrivée endTile.
	 * cf. http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29#Distance_sur_des_espaces_vectoriels
	 * ici, on calcule cette distance exprimée en pixels plutot qu'en case
	 * comme c'est le cas dans BasicHeuristicCalculator.
	 * 
	 * @param tile	
	 * 		la case concern�e 
	 * @return	
	 * 		la distance de Manhattan entre tile et la plus proche des cases contenues dans endTiles
	 */
	@Override
	public double processHeuristic(AiTile tile) throws StopRequestException
	{	// init
		List<AiTile> endTiles = getEndTiles();
		AiZone zone = tile.getZone();
		double startX = tile.getPosX();
		double startY = tile.getPosY();
		double result = Integer.MAX_VALUE;
		
		// specific case : tile is the first tile of the considered path
		if(tile.equals(startTile))
		{	startX = this.startX;
			startY = this.startY;
		}
		
		for(AiTile endTile: endTiles)
		{	double endX = endTile.getPosX();
			double endY = endTile.getPosY();
			double dist = zone.getPixelDistance(startX,startY,endX,endY);
			if(dist<result)
				result = dist;
		}
		
		return result;
	}
}
