package org.totalboumboum.ai.v201112.adapter.path.successor;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201112.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Implémentation d'une fonction successeur destinée à effectuer
 * une approximation d'un chemin et d'un temps de déplacement. 
 * L'idée est de ne pas chercher à contourner les obstacles temporaires
 * que sont les murs destructibles, les bombes et les items, mais
 * plutôt de considérer que leur traversée dure plus longtemps.
 * Seuls les murs indestructibles sont considérés comme des obstacles
 * réels. Comme pour {@link BasicSuccessorCalculator}, on ignore les 
 * cases qui ont déjà été explorées.
 * <br/>
 * La classe est compatible avec :
 * <ul>
 * 		<li>Fonctions de coût :
 * 			<ul>
 * 				<li>{@link ApproximateCostCalculator}</li>
 * 			</ul>
 * 		</li> 
 * 		<li>Fonction heuristiques :
 * 			<ul>
 * 				<li>{@link NoHeuristicCalculator}</li>
 * 				<li>{@link TimeHeuristicCalculator}</li>
 * 			</ul>
 * 		</li> 
 * </ul>
 * <br/>
 * On considère qu'une case est explorée si elle
 * a déjà été traitée par l'algorithme, <i>même dans une autre 
 * branche</i>. Autrement dit, cette fonction est optimisée pour ne
 * pas tester des chemins qui repassent par une case déjà utilisée.
 * L'idée est que si une case a déjà été traitée, on ne peut pas
 * trouver de meilleur chemin pour l'atteindre.
 * <br/>
 * A noter que cette classe est basée sur le temps, et non pas
 * la distance, toutefois elle ne considère pas l'action d'attente
 * (en raison de l'approximation assez importante qui est effectuée),
 * donc son application est très rapide.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public class ApproximateSuccessorCalculator extends SuccessorCalculator
{
	/**
	 * Construit une fonction successeur
	 * utilisant l'IA passée en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		IA de référence.
	 */
	public ApproximateSuccessorCalculator(ArtificialIntelligence ai)
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
	 * Fonction successeur d'approximation : on ne considère que
	 * les murs indestructibles comme des obstacles. Les autres
	 * objets ne sont pas considérés comme des obstacles, car ils
	 * sont temporaires : une bombe va exploser, un mur destructible
	 * peut être détruit, etc.
	 * <br/> 
	 * On ignore également les cases déjà explorées, afin d'éviter 
	 * les retours en arrière et de rendre le traitement plus rapide.
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
		
		// on ne traite pas les cases déjà explorées
		if(!processedTilesMap.containsKey(tile))
		{	// màj la map des cases visitées
			processedTilesMap.put(tile,node);
			
			// pour chaque case voisine :
			for(Direction direction: Direction.getPrimaryValues())
			{	AiTile neighbor = tile.getNeighbor(direction);
				// on teste si elle est contient un mur indestructible :
				// tout autre obstacle est considéré comme traversable
				List<AiBlock> blocks = neighbor.getBlocks();
				boolean noProcess = false;
				Iterator<AiBlock> it = blocks.iterator();
				while(!noProcess && it.hasNext())
				{	AiBlock block = it.next();
					noProcess = !block.isDestructible();
				}
				
				// si la case ne contient pas de mur indestructible et
				// si on ne l'a pas déjà explorée, alors on la traite
				if(!noProcess && processedTilesMap.get(neighbor)==null)
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
