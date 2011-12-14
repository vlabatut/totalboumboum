package org.totalboumboum.ai.v201112.adapter.path.successor;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201112.adapter.path.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.PixelCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.PixelHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Implémentation la plus simple d'une fonction successeur : 
 * on prend les 4 cases voisines, en ne gardant que celles qui sont 
 * traversables par le personnage considéré, et qui n'ont pas déjà 
 * été explorées.
 * <br/>
 * La classe est compatible avec :
 * <ul>
 * 		<li>Fonctions de coût :
 * 			<ul>
 * 				<li>{@link MatrixCostCalculator}</li>
 * 				<li>{@link PixelCostCalculator}</li>
 * 				<li>{@link TileCostCalculator}</li>
 * 			</ul>
 * 		</li> 
 * 		<li>Fonction heuristiques :
 * 			<ul>
 * 				<li>{@link NoHeuristicCalculator}</li>
 * 				<li>{@link PixelHeuristicCalculator}</li>
 * 				<li>{@link TileHeuristicCalculator}</li>
 * 			</ul>
 * 		</li> 
 * </ul>
 * <br/>
 * On considère qu'une case est explorée si elle
 * a déjà été traitée par l'algorithme, <i>même dans une autre 
 * branche</i>. Autrement dit, cette fonction est optimisée pour ne
 * pas tester des chemins qui repassent par une case déjà utilisée.
 * L'idée est que si une case a déjà été traitée, on ne peut pas
 * trouver de meilleur chemin pour l'atteindre. Cette hypothèse est
 * fausse quand le temps est pris en compte, car il induit une
 * action d'attente. C'est la raison pour laquelle cette classe
 * n'est pas compatible avec toutes les fonctions heuristiques et
 * de côut disponibles. 
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
	
	@Override
	public void init(AiSearchNode root)
	{	processedTiles.clear();
		processedTilesMap.clear();
		processedTiles.put(null,processedTilesMap);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** map contenant les cases déjà traitées */
	private final HashMap<AiTile,AiSearchNode> processedTilesMap = new HashMap<AiTile,AiSearchNode>();
	
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
	public List<AiSearchNode> processSuccessors(AiSearchNode node) throws StopRequestException
	{	// init
		List<AiSearchNode> result = new ArrayList<AiSearchNode>();
		AiTile tile = node.getLocation().getTile();
		AiHero hero = node.getHero();
		
		// on ne traite pas les cases déjà explorées
		if(!processedTilesMap.containsKey(tile))
		{	// màj la map des cases visitées
			processedTilesMap.put(tile,node);
			
			// pour chaque case voisine :
			for(Direction direction: Direction.getPrimaryValues())
			{	AiTile neighbor = tile.getNeighbor(direction);
				
				// on teste si elle est traversable et n'a pas déjà été explorée
				if(neighbor.isCrossableBy(hero) && processedTilesMap.get(neighbor)==null)
				{	AiLocation location = new AiLocation(neighbor);
					AiSearchNode child = new AiSearchNode(location,node);
					result.add(child);
				}
			}
		}

		return result;
	}

	@Override
	public boolean isThreatened(AiSearchNode node)
	{	boolean result = false;
		AiLocation location = node.getLocation();
		AiTile tile = location.getTile();
		AiZone zone = location.getZone();
		List<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		while(!result && it.hasNext())
		{	AiBomb bomb = it.next();
			List<AiTile> blast = bomb.getBlast();
			result = blast.contains(tile);
		}
		return result;
	}
}
