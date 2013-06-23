package org.totalboumboum.ai.v201011.adapter.path.astar.successor;

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
import org.totalboumboum.ai.v201011.adapter.path.astar.AstarNode;

/**
 * permet de définir une fonction successeur utilisée par l'algorithme
 * A* lors de la recherche d'un plus court chemin, pour développer un état
 * 
 * @author Vincent Labatut
 *
 */
public abstract class SuccessorCalculator
{
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * calcule tous les états accessibles à partir du noeud de recherche
	 * passé en paramètre. On prend un noeud de recherche et non pas
	 * un état en paramètre, car le noeud de recherche contient des informations
	 * susceptibles d'éliminer certains successeurs potentiels. 
	 * Par exemple, si le cout correspond au temps de déplacement, alors le cout du noeud
	 * de recherche courant correspond au temps nécessaire pour arriver à l'état
	 * correspondant. Certaines des cases accessibles depuis cet état peuvent être
	 * menancée par du feu, et le temps est une information cruciale pour déterminer
	 * si le personnage peut ou pas traverser une case avant qu'elle ne brûle.
	 * 
	 * @param node	
	 * 		le noeud de recherche courant 
	 * @return	
	 * 		la liste de cases accessibles
	 */
	public abstract List<AiTile> processSuccessors(AstarNode node) throws StopRequestException;
}
