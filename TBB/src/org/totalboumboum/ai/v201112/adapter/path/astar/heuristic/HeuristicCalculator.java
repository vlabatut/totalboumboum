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

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;

/**
 * Permet de définir une fonction heuristique utilisée par l'algorithme
 * A* lors de la recherche d'un plus court chemin.
 * 
 * @author Vincent Labatut
 */
public abstract class HeuristicCalculator
{
	/**
	 * Construit une fonction heuristique
	 * utilisant l'IA passée en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		IA de référence.
	 */
	public HeuristicCalculator(ArtificialIntelligence ai)
	{	this.ai = ai;
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ArtificialIntelligence ai = null;
	
	/////////////////////////////////////////////////////////////////
	// END TILE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ensemble des cases pouvant terminer le chemin */
	private final Set<AiTile> endTiles = new TreeSet<AiTile>();	

	/**
	 * Initialise/modifie l'ensemble des cases 
	 * pouvant terminer le chemin recherché.
	 * 
	 * @param endTiles	
	 * 		les cases terminant le chemin
	 */
	public void setEndTiles(Set<AiTile> endTiles)
	{	this.endTiles.clear();
		this.endTiles.addAll(endTiles);
	}

	/**
	 * Renvoie l'ensemble des cases objectifs (i.e. les cases terminant
	 * le chemin recherché).
	 * 
	 * @return	
	 * 		L'ensemble des cases objectifs.
	 */
	public Set<AiTile> getEndTiles()
	{	return endTiles;	
	}
	
	/**
	 * Renvoie la case de destination la
	 * plus proche de l'emplacement passé
	 * en paramètre. La proximité doit
	 * être évaluée en termes d'heuristique.
	 * <br/>
	 * Si plusieurs destinations sont également
	 * proches, un choix quelconque (ex : hasard)
	 * doit être réalisé).
	 * 
	 * @return
	 * 		La case de destination la plus proche.
	 */
	public abstract AiTile getClosestEndTile(AiLocation location);


	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Calcule la valeur heuristique de la case tile,
	 * le but étant de se rendre dans une des cases objectifs
	 * 
	 * @param location	
	 * 		L'emplacement concernée 
	 * @return	
	 * 		La valeur heuristique de cet emplacement.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public abstract double processHeuristic(AiLocation location) throws StopRequestException;
}
