package org.totalboumboum.ai.v201415.adapter.path.cost;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201415.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201415.adapter.data.AiHero;
import org.totalboumboum.ai.v201415.adapter.data.AiItem;
import org.totalboumboum.ai.v201415.adapter.data.AiTile;
import org.totalboumboum.ai.v201415.adapter.data.AiZone;
import org.totalboumboum.ai.v201415.adapter.path.AiLocation;
import org.totalboumboum.ai.v201415.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201415.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201415.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201415.adapter.path.successor.BasicSuccessorCalculator;

/**
 * Classe étendant la classe abstraite {@link CostCalculator} de 
 * la manière la plus simple possible.
 * <br/>
 * Ici, le coût pour passer d'une case à l'autre est simplement 1, 
 * quelles que soient les cases considérées.
 * <br/>
 * La classe est compatible avec :
 * <ul>
 * 		<li>Fonction heuristiques :
 * 			<ul>
 * 				<li>{@link NoHeuristicCalculator}</li>
 * 				<li>{@link TileHeuristicCalculator}</li>
 * 			</ul>
 * 		</li> 
 * 		<li>Fonctions successeurs :
 * 			<ul>
 * 				<li>{@link BasicSuccessorCalculator}</li>
 * 			</ul>
 * 		</li> 
 * </ul>
 * 
 * @author Vincent Labatut
 */
public class TileCostCalculator extends CostCalculator
{
	/**
	 * Construit une fonction de côut
	 * utilisant l'agent passé en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		Agent de référence.
	 */
	public TileCostCalculator(ArtificialIntelligence ai)
	{	super(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Les deux emplacements sont supposés être dans des cases voisines, 
	 * on se contente de renvoyer leur distance
	 * (exprimée en cases, donc forcément ici : 1).
	 * 
	 * @param currentNode
	 * 		Le noeud contenant l'emplacement de départ. 
	 * @param nextLocation
	 * 		L'emplacement d'arrivée (case voisine de la case courante).
	 * @return	
	 * 		Le coût du déplacement entre les deux emplacements (ici : 1).
	 */ 
	@Override
	public double processCost(AiSearchNode currentNode, AiLocation nextLocation)
	{	// on suppose que la case est voisine, donc le coût est toujours de 1
		double result = 1;
		AiTile destination = nextLocation.getTile();
		
		// et on rajoute le coût supplémentaire si la case contient un adversaire
		if(opponentCost>0)
		{	AiZone zone = destination.getZone();
			List<AiHero> opponents = new ArrayList<AiHero>(zone.getRemainingOpponents());
			List<AiHero> heroes = destination.getHeroes();
			opponents.retainAll(heroes);
			if(!opponents.isEmpty())
				result = result + opponentCost;
		}
		
		// on rajoute le coût supplémentaire si la case contient un malus
		if(malusCost>0)
		{	List<AiItem> items = destination.getItems();
			Iterator<AiItem> it = items.iterator();
			boolean containsMalus = false;
			while(it.hasNext() && !containsMalus)
			{	AiItem item = it.next();
				containsMalus = !item.getType().isBonus();
			}
			if(containsMalus)
				result = result + malusCost;
		}
		
		// on rajoute le coût supplémentaire s'il est défini pour la case
		if(tileCosts!=null)
		{	int row = destination.getRow();
			int col = destination.getCol();
			result = result + tileCosts[row][col];
		}
		
		return result;		
	}

	/**
	 * le coût d'un chemin correspond ici à sa distance exprimée
	 * en cases.
	 * 
	 * @param path
	 * 		Chemin à traiter.
	 * @return
	 * 		Le coût de ce chemin.
	 */
/*	@Override
	public double processCost(AiPath path)
	{	double result = path.getTileDistance();
		return result;
	}
*/	
}
