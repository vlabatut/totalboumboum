package org.totalboumboum.ai.v201112.adapter.path.astar.successor;

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

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.path.astar.AstarNode;

/**
 * Permet de définir une fonction successeur utilisée par l'algorithme
 * A* lors de la recherche d'un plus court chemin, pour développer un état.
 * 
 * @author Vincent Labatut
 */
public abstract class SuccessorCalculator
{	
	/**
	 * Construit une fonction successeur
	 * utilisant l'IA passée en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		IA de référence.
	 */
	public SuccessorCalculator(ArtificialIntelligence ai)
	{	this.ai = ai;
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ArtificialIntelligence ai = null;
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Calcule tous les états accessibles à partir du noeud de recherche
	 * passé en paramètre. On prend un noeud de recherche et non pas
	 * un état en paramètre, car le noeud de recherche contient des informations
	 * susceptibles d'éliminer certains successeurs potentiels.<br/>
	 * Par exemple, si le coût correspond au temps de déplacement, alors le coût du noeud
	 * de recherche courant correspond au temps nécessaire pour arriver à l'état
	 * correspondant. Certaines des cases accessibles depuis cet état peuvent être
	 * menacée par du feu, et le temps est une information cruciale pour déterminer
	 * si le personnage peut ou pas traverser une case avant qu'elle brûle.
	 * 
	 * @param node	
	 * 		Le noeud de recherche courant.
	 * @return	
	 * 		La liste de noeuds fils obtenus.
	 */
	public abstract List<AstarNode> processSuccessors(AstarNode node) throws StopRequestException;
}
