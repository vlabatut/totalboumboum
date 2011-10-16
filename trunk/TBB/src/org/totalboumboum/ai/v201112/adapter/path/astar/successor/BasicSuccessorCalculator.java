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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.PixelCostCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Implémentation la plus simple d'une fonction successeur : 
 * on prend les 4 cases voisines, en ne gardant que celles qui sont traversables
 * par le personnage considéré, et qui n'ont pas déjà été explorées. La classe 
 * est compatible avec {@link BasicCostCalculator}, {@link PixelCostCalculator}
 * et {@link MatrixCostCalculator}.<br/>
 * On empêche tout passage sur une case déjà explorée afin de rendre le traitement 
 * plus court. Par conséquent, cette fonction n'est pas capable de traiter les 
 * chemins qui contiennent des retours en arrière ou de l'attente.
 * 
 * @author Vincent Labatut
 */
public class BasicSuccessorCalculator extends SuccessorCalculator
{
	/**
	 * Construit une fonction successeur
	 * utilisant l'IA passée en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		IA de référence.
	 */
	public BasicSuccessorCalculator(ArtificialIntelligence ai)
	{	super(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Fonction successeur la plus simple: on considère les 4 cases 
	 * voisines de la case courante,
	 * en ne conservant que les cases que le personnage 
	 * de référence peut traverser. On ignore également
	 * les cases déjà explorées, afin d'éviter les retours
	 * en arrière et de rendre le traitement plus rapide.
	 * 
	 * @param node	
	 * 		Le noeud de recherche courant
	 * @return	
	 * 		La liste des noeuds fils.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	@Override
	public List<AstarNode> processSuccessors(AstarNode node) throws StopRequestException
	{	// init
		List<AstarNode> result = new ArrayList<AstarNode>();
		AiTile tile = node.getLocation().getTile();
		AiHero hero = node.getHero();
		
		// pour chaque case voisine :
		for(Direction direction: Direction.getPrimaryValues())
		{	AiTile neighbor = tile.getNeighbor(direction);
			
			// on teste si elle est traversable 
			// et n'a pas déjà été explorée dans la branche courante de A*
			if(neighbor.isCrossableBy(hero) && !node.hasBeenExplored(neighbor))
			{	AiLocation location = new AiLocation(neighbor);
				AstarNode child = new AstarNode(location,node);
				result.add(child);
			}
		}

		return result;
	}
}
