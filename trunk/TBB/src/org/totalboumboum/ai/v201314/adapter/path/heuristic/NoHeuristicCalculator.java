package org.totalboumboum.ai.v201314.adapter.path.heuristic;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.search.Dijkstra;

/**
 * Implémente une fonction heuristique bidon,
 * dans le but d'utiliser des algorithmes de recherche
 * aveugle tels que le parcours en largeur ou en profondeur,
 * ou bien l'algorithme de {@link Dijkstra}.
 * Ici, la méthode {@link #processHeuristic} renvoie toujours 0.
 * <br/>
 * La classe est compatible avec toutes les fonctions de coût
 * et toutes les fonctions successeurs.
 * 
 * @author Vincent Labatut
 */
public class NoHeuristicCalculator extends HeuristicCalculator
{
	/**
	 * Construit une fonction heuristique
	 * utilisant l'agent passé en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		Agent de référence.
	 */
	public NoHeuristicCalculator(ArtificialIntelligence ai)
	{	super(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// END TILES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public AiTile getClosestEndTile(AiLocation location)
	{	// comme c'est de la recherche aveugle, on renvoie le
		// premier objectif rencontré
		AiTile result = getEndTiles().iterator().next();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Cette heuristique est constante et renvoie toujours la valeur 0.
	 * 
	 * @param location	
	 * 		L'emplacement concerné (ignoré). 
	 * @return	
	 * 		Toujours zéro.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	@Override
	public double processHeuristic(AiLocation location) throws StopRequestException
	{	return 0;
	}
}
