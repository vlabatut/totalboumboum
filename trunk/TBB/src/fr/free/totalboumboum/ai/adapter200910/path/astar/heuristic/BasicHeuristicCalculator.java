package fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import fr.free.totalboumboum.ai.adapter200910.data.AiTile;

public class BasicHeuristicCalculator extends HeuristicCalculator
{
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * l'heuristique la plus simple consiste à prendre la distance
	 * de Manhattan entre la case courante tile et la case d'arrivée endTile.
	 * cf. http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29#Distance_sur_des_espaces_vectoriels
	 * 
	 * @param tile	la case concernée 
	 * @return	la distance de Manhattan entre tile et endTile
	 */
	@Override
	public double processHeuristic(AiTile tile)
	{	double result;
		AiTile endTile = getEndTile();
		int dLine = Math.abs(tile.getLine() - endTile.getLine());
		int dCol = Math.abs(tile.getCol() - endTile.getCol());
		result = dLine + dCol;
		return result;
	}
}
