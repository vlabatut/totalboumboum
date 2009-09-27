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

/**
 * permet de définir une fonction heuristique utilisée par l'algorithme
 * A* lors de la recherche d'un plus court chemin.
 */
public abstract class HeuristicCalculator
{
	/////////////////////////////////////////////////////////////////
	// END TILE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** case terminant le chemin */
	private AiTile endTile;	

	/**
	 * initialise/modifie la case terminant le chemin recherché
	 * @param endTile	la case terminant le chemin
	 */
	public void setEndTile(AiTile endTile)
	{	this.endTile = endTile;
		
	}

	/**
	 * renvoie la case objectif (i.e. la case terminant
	 * le chemin recherché)
	 * @return	la case objectif
	 */
	public AiTile getEndTile()
	{	return endTile;	
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * calcule la valeur heuristique de la case tile,
	 * le but étant de se rendre dans la case objectif endTile.
	 * 
	 * @param tile	la case concernée 
	 * @return	l'heuristique de la case
	 */
	public abstract double processHeuristic(AiTile tile);
}
