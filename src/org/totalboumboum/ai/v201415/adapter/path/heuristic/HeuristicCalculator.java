package org.totalboumboum.ai.v201415.adapter.path.heuristic;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import org.totalboumboum.ai.v201415.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201415.adapter.data.AiTile;
import org.totalboumboum.ai.v201415.adapter.path.AiLocation;
import org.totalboumboum.ai.v201415.adapter.path.search.AiSearchNode;

/**
 * Permet de définir une fonction heuristique utilisée par un algorithme
 * de recherche lors de la recherche d'un plus court chemin.
 * 
 * @author Vincent Labatut
 */
public abstract class HeuristicCalculator
{
	/**
	 * Construit une fonction heuristique
	 * utilisant l'agent passé en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		Agent de référence.
	 */
	public HeuristicCalculator(ArtificialIntelligence ai)
	{	this.ai = ai;
	}
	
	/**
	 * Réinitialise les structures internes de
	 * l'objet avant de commencer une nouvelle
	 * recherche.
	 * 
	 * @param root
	 * 		Le noeud de recherche racine de l'arbre de recherche.
	 */
	public void init(AiSearchNode root)
	{	// à surcharger si nécessaire
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** L'agent associé */
	protected ArtificialIntelligence ai = null;
	
	/////////////////////////////////////////////////////////////////
	// END TILES		/////////////////////////////////////////////
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
	 * @param location 
	 * 		L'emplacement à traiter.
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
	 */
	public abstract double processHeuristic(AiLocation location);
}
