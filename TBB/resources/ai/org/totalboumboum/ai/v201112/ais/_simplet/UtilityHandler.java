package org.totalboumboum.ai.v201112.ais._simplet;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.ais._simplet.criterion.CriterionDestruction;
import org.totalboumboum.ai.v201112.ais._simplet.criterion.CriterionLocality;
import org.totalboumboum.ai.v201112.ais._simplet.criterion.CriterionThreat;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201112.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Pour cet exemple, on ne distingue qu'un petit nombre
 * de critères et de cas.
 * 
 * @author Vincent Labatut
 */
public class UtilityHandler extends AiUtilityHandler<Simplet>
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
	protected UtilityHandler(Simplet ai) throws StopRequestException
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
	}
	
	@Override
	protected void resetData() throws StopRequestException
	{	ai.checkInterruption();
		super.resetData();
		
		bombTiles.clear();
	}
	
	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Cas de collecte correspondant à une case contenant un item visible */
	private final String CASE_COLLECT_VISIBLE_ITEM = "VISIBLE_ITEM";
	/** Cas de collecte correspondant à une case voisine d'un mur destructible */
	private final String CASE_COLLECT_WALL_NEIGHBOR = "WALL_NEIGHBOR";
	/** Cas d'attaque correspondant à une case suffisamment proche de la cible */
	private final String CASE_ATTACK = "ATTACK";
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		
		// ici, la sélection des cases dont on veut l'utilité dépend du mode
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
				{	HashMap<AiTile,AiSearchNode> map = dijkstra.startProcess(startLocation);
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
			AiTile currentTile = ownHero.getTile();
			List<AiTile> neighbors = new ArrayList<AiTile>(currentTile.getNeighbors());
			Collections.shuffle(neighbors);
			Iterator<AiTile> it = neighbors.iterator();
			while(result.isEmpty() && it.hasNext())
			{	ai.checkInterruption();	
				
				AiTile neighbor = it.next();
				if(neighbor.isCrossableBy(ownHero))
				{	result.add(neighbor);
					tile = neighbor;
				}
			}
			
			// si aucun voisin n'est accessible (!?) on prend la case courante
			if(result.isEmpty())
			{	result.add(currentTile);
				tile = currentTile;
			}
			
			bombTiles.put(tile,false);
		}
		
		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException
	{	ai.checkInterruption();

		// on définit les critères
		CriterionLocality criterionLocality = new CriterionLocality(ai);
		CriterionDestruction criterionDestruction = new CriterionDestruction(ai);
		CriterionThreat criterionThreat = new CriterionThreat(ai);
		
		// on définit les cas
		AiUtilityCase caseCollectVisibleItem;
		AiUtilityCase caseCollectWallNeighbor;
		AiUtilityCase caseAttack;
		// cas de collecte
		{	// cas de l'item visible
			Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
			criteria.add(criterionLocality);
			caseCollectVisibleItem = new AiUtilityCase(CASE_COLLECT_VISIBLE_ITEM,criteria);
			cases.put(CASE_COLLECT_VISIBLE_ITEM,caseCollectVisibleItem);
		}
		{	// cas des autres cases
			Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
			criteria.add(criterionLocality);
			criteria.add(criterionDestruction);
			caseCollectWallNeighbor = new AiUtilityCase(CASE_COLLECT_WALL_NEIGHBOR,criteria);
			cases.put(CASE_COLLECT_WALL_NEIGHBOR,caseCollectVisibleItem);
		}
		// cas d'attaque
		{	// un seul cas
			Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
			criteria.add(criterionLocality);
			criteria.add(criterionThreat);
			caseAttack = new AiUtilityCase(CASE_ATTACK,criteria);
			cases.put(CASE_ATTACK,caseAttack);
		}
		
		// on affecte les valeurs d'utilité
		int utility = 1;
		AiUtilityCombination combi;
		// cas de collecte
		for(int locality=0;locality<=CriterionLocality.LOCALITY_LIMIT;locality++)
		{	ai.checkInterruption();	
			
			for(int destruction=0;destruction<=CriterionDestruction.DESTRUCTION_LIMIT;destruction++)
			{	ai.checkInterruption();	
				
				combi = new AiUtilityCombination(caseCollectWallNeighbor);
				combi.setCriterionValue(criterionLocality,locality);
				combi.setCriterionValue(criterionDestruction,destruction);
				referenceUtilities.put(combi,utility);
				utility++;
			}
		}
		for(int locality=0;locality<=CriterionLocality.LOCALITY_LIMIT;locality++)
		{	ai.checkInterruption();	
			
			combi = new AiUtilityCombination(caseCollectVisibleItem);
			combi.setCriterionValue(criterionLocality,locality);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		// on màj la valeur d'utilité maximale pour la collecte
		maxUtilities.put(AiMode.COLLECTING,utility-1);
		
		// cas d'attaque
		utility = 1;
		for(int threat=1;threat<=CriterionThreat.THREAT_LIMIT;threat++)
		{	ai.checkInterruption();	
			
			for(int locality=0;locality<=CriterionLocality.LOCALITY_LIMIT;locality++)
			{	ai.checkInterruption();	
				
				combi = new AiUtilityCombination(caseAttack);
				combi.setCriterionValue(criterionLocality,locality);
				combi.setCriterionValue(criterionThreat,threat);
				referenceUtilities.put(combi,utility);
				utility++;
			}
		}
		// on màj la valeur d'utilité maximale pour l'attaque
		maxUtilities.put(AiMode.ATTACKING,utility-1);
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
	
		AiUtilityCase result = null;
		// le cas dépend du mode
		AiMode mode = ai.modeHandler.getMode();
		
		// pour l'attaque, on n'a définit qu'un seul cas
		if(mode.equals(AiMode.ATTACKING))
		{	result = cases.get(CASE_ATTACK);
			if(!bombTiles.containsKey(tile))
				bombTiles.put(tile,true);
		}
		
		// pour la collecte, on teste simplement la présence d'un item
		else
		{	List<AiItem> items = tile.getItems();
			if(items.isEmpty())
			{	result = cases.get(CASE_COLLECT_VISIBLE_ITEM);
				bombTiles.put(tile,false);
			}
			else
			{	result = cases.get(CASE_COLLECT_WALL_NEIGHBOR);
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
