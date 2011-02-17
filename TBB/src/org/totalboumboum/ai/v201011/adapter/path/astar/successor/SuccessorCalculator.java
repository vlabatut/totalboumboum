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
 * permet de d�finir une fonction successeur utilis�e par l'algorithme
 * A* lors de la recherche d'un plus court chemin, pour d�velopper un �tat
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
	 * calcule tous les �tats accessibles � partir du noeud de recherche
	 * pass� en param�tre. On prend un noeud de recherche et non pas
	 * un �tat en param�tre, car le noeud de recherche contient des informations
	 * susceptibles d'�liminer certains successeurs potentiels. 
	 * Par exemple, si le cout correspond au temps de d�placement, alors le cout du noeud
	 * de recherche courant correspond au temps n�cessaire pour arriver � l'�tat
	 * correspondant. Certaines des cases accessibles depuis cet �tat peuvent �tre
	 * menanc�e par du feu, et le temps est une information cruciale pour d�terminer
	 * si le personnage peut ou pas traverser une case avant qu'elle ne br�le.
	 * 
	 * @param node	
	 * 		le noeud de recherche courant 
	 * @return	
	 * 		la liste de cases accessibles
	 */
	public abstract List<AiTile> processSuccessors(AstarNode node) throws StopRequestException;
}
