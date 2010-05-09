package fr.free.totalboumboum.ai.nextadapter.path.astar.successor;

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

import java.util.ArrayList;
import java.util.List;

import fr.free.totalboumboum.ai.nextadapter.data.AiHero;
import fr.free.totalboumboum.ai.nextadapter.data.AiTile;
import fr.free.totalboumboum.ai.nextadapter.path.astar.AstarNode;
import fr.free.totalboumboum.engine.content.feature.Direction;

/**
 * impl�mentation la plus simple d'une fonction successeur : 
 * on prend les 4 cases voisines, en ne gardant que celles qui sont traversables
 * par le personnage consid�r�.
 * Une version plus complexe et plus efficace consisterait � utiliser la prodondeur
 * du noeud de recherche pour calculer le temps n�cessaire pour arriver jusqu'� la case
 * courante, et � v�rifier qu'aucune bombe ne sera en train d'exploser dans les cases voisines
 * quand le joueur y sera. En d'autres termes, on peut calculer si le joueur a le temps
 * de passer sur une case avant qu'elle ne soit prise dans une explosion.
 */
public class BasicSuccessorCalculator extends SuccessorCalculator
{
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * fonction successeur la plus simple: on consid�re les 4 cases voisines de la case courante,
	 * en ne conservant que les cases que le personnage de r�f�rence peut traverser 
	 * 
	 * @param node	le noeud de recherche courant
	 * @return	une liste des cases successeurs
	 */
	@Override
	public List<AiTile> processSuccessors(AstarNode node)
	{	// init
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tile = node.getTile();
		AiHero hero = node.getHero();
		// pour chaque case voisine : on la rajoute si elle est traversable
		for(Direction direction: Direction.getPrimaryValues())
		{	AiTile neighbor = tile.getNeighbor(direction);
			if(neighbor.isCrossableBy(hero))
				result.add(neighbor);			
		}
		//
		return result;
	}
}
