package org.totalboumboum.ai.v201314.ais._simplet;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * Pour cet exemple, on ne distingue qu'un petit nombre
 * de critères et de catégories.
 * 
 * @author Vincent Labatut
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected PreferenceHandler(Agent ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on initialise certains objets une fois pour toutes
		initData();
	}

	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** La zone de jeu */
	private AiZone zone = null;
	/** Le personnage contrôlé par cet agent */
	private AiHero ownHero = null;
	/** Un objet dijkstra utilisé pour identifier le voisinage de l'agent */
	private Dijkstra dijkstra = null;
	/** Les méthodes communes */
	private CommonTools commonTools;
	/** Indique pour chaque case traitée si on veut y poser une bombe ou pas */
	protected HashMap<AiTile,Boolean> bombTiles = new HashMap<AiTile,Boolean>();

	/**
	 * Permet d'initialiser certains objets une fois pour toutes.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void initData() throws StopRequestException
	{	ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		commonTools = ai.commonTools;
		
		// on a besoin d'un objet dijkstra pour le traitement,
		CostCalculator costCalculator = new TileCostCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		dijkstra = new Dijkstra(ai,ownHero,costCalculator,successorCalculator);
		dijkstra.setMaxHeight(5); // on se met une limite raisonnable
	}
	
	@Override
	protected void resetCustomData() throws StopRequestException
	{	ai.checkInterruption();
		
		bombTiles.clear();
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		Map<AiTile,AiSearchNode> map = null;
		
		// ici, la sélection des cases dont on veut la préférence dépend du mode
		AiMode mode = ai.modeHandler.getMode();
		
		// pour le mode attaque, l'agent considère les cases à portée de sa cible
		if(mode==AiMode.ATTACKING)
		{	// on récupère la cible
			AiHero target = ai.targetHandler.target;
			if(target!=null)
			{	// on récupère les cases permettant de la menacer directement
				Set<AiTile> dangerous = commonTools.getTilesForRadius(target.getTile(),ownHero);
				// on les ajoute aux cases à traiter
				result.addAll(dangerous);
			}
		}
		
		// pour le mode collecte, l'agent considère les items ainsi
		// que les cases situées dans son voisinage
		else
		{	// on récupère les items visibles
			List<AiItem> items = zone.getItems();
			// s'il y en a, on considère uniquement leurs cases (grosse simplification !)
			if(!items.isEmpty())
			{	for(AiItem item: items)
				{	ai.checkInterruption();	
					
					AiTile tile = item.getTile();
					result.add(tile);
				}
			}
			// sinon on on considère le voisinage de l'agent
			else
			{	// on utilise une version simple de dijkstra pour identifier le voisinage
				AiLocation startLocation = new AiLocation(ownHero);
				try
				{	map = dijkstra.startProcess(startLocation);
					result.addAll(map.keySet());
				}
				catch (LimitReachedException e)
				{	//e.printStackTrace();
				}
			}
		}
		
		// on veut toujours avoir au moins une case
		if(result.isEmpty())
		{	AiTile tile = null;
			// si on n'en a pas, on en prend une au hasard
			final AiTile currentTile = ownHero.getTile();
			// on utilise une version simple de dijkstra pour identifier le voisinage
			AiLocation startLocation = new AiLocation(ownHero);
			try
			{	if(map==null)
					map = dijkstra.startProcess(startLocation);
				List<AiTile> tiles = new ArrayList<AiTile>(map.keySet());
				Collections.shuffle(tiles);
				Collections.sort(tiles,new Comparator<AiTile>()
				{	@Override
					public int compare(AiTile t1, AiTile t2)
					{	int d1 = zone.getTileDistance(t1,currentTile);
						int d2 = zone.getTileDistance(t2,currentTile);
						int result = d2 - d1; // ordre inverse
						return result;
					}
				});
				if(!tiles.isEmpty())
				{	tile = tiles.get(0);
					result.add(tile);
				}
			}
			catch (LimitReachedException e)
			{	//e.printStackTrace();
			}		
			
			// on prend un voisin direct
//			List<AiTile> neighbors = new ArrayList<AiTile>(currentTile.getNeighbors());
//			Collections.shuffle(neighbors);
//			Iterator<AiTile> it = neighbors.iterator();
//			while(result.isEmpty() && it.hasNext())
//			{	ai.checkInterruption();	
//				
//				AiTile neighbor = it.next();
//				if(neighbor.isCrossableBy(ownHero))
//				{	result.add(neighbor);
//					tile = neighbor;
//				}
//			}
			
			// si aucune case n'est accessible (!?) on prend la case courante
			if(result.isEmpty())
			{	result.add(currentTile);
				tile = currentTile;
			}
			
			bombTiles.put(tile,false);
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiCategory identifyCategory(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
	
		AiCategory result = null;
		// la catégorie dépend du mode
		AiMode mode = ai.modeHandler.getMode();
		
		// pour l'attaque, on n'a définit qu'une seule catégorie
		if(mode.equals(AiMode.ATTACKING))
		{	result = getCategory("ATTACK");
			if(!bombTiles.containsKey(tile))
				bombTiles.put(tile,true);
		}
		
		// pour la collecte, on teste simplement la présence d'un item
		else
		{	List<AiItem> items = tile.getItems();
			if(items.isEmpty())
			{	result = getCategory("COLLECT_VISIBLE_ITEM");
				bombTiles.put(tile,false);
			}
			else
			{	result = getCategory("COLLECT_WALL_NEIGHBOR");
				bombTiles.put(tile,true);
			}
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
	}
}
