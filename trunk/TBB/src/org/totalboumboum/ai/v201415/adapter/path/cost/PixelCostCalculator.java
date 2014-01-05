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
import org.totalboumboum.ai.v201415.adapter.path.heuristic.PixelHeuristicCalculator;
import org.totalboumboum.ai.v201415.adapter.path.successor.BasicSuccessorCalculator;

/**
 * Classe étendant la classe abstraite {@link CostCalculator} de la manière à déterminer
 * le coût en fonction de la distance en pixels entre deux emplacements.
 * <br/>
 * La classe est compatible avec :
 * <ul>
 * 		<li>Fonction heuristiques :
 * 			<ul>
 * 				<li>{@link NoHeuristicCalculator}</li>
 * 				<li>{@link PixelHeuristicCalculator}</li>
 * 			</ul>
 * 		</li> 
 * 		<li>Fonctions successeurs :
 * 			<ul>
 * 				<li>{@link BasicSuccessorCalculator}</li>
 * 			</ul>
 * 		</li>
 * </ul>
 * <br/> 
 * Cette classe n'est pas conçue pour traiter les chemins contenant des
 * retours en arrière. Voir {@link TimeCostCalculator} pour ça.
 * 
 * @author Vincent Labatut
 */
public class PixelCostCalculator extends CostCalculator
{	
	/**
	 * Construit une fonction de coût
	 * utilisant l'agent passée en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		Agent de référence.
	 */
	public PixelCostCalculator(ArtificialIntelligence ai)
	{	super(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Les deux emplacements sont supposés être dans des cases voisines. 
	 * On renvoie la <a href="http://fr.wikipedia.org/wiki/Distance_de_Manhattan">distance de Manhattan</a>
	 * (exprimée en pixels) qui les sépare. 
	 * 
	 * @param currentNode
	 * 		Le noeud contenant l'emplacement de départ. 
	 * @param nextLocation
	 * 		L'emplacement d'arrivée (case voisine de la case courante).
	 * @return	
	 * 		La distance en pixels entre l'emplacement de départ et celui d'arrivée.
	 */ 
	@Override
	public double processCost(AiSearchNode currentNode, AiLocation nextLocation)
	{	// on calcule simplement la distance en pixels
		AiLocation currentLocation = currentNode.getLocation();
		AiTile destination = nextLocation.getTile();
		AiZone zone = currentLocation.getZone();
		double result = zone.getPixelDistance(currentLocation,nextLocation);
		
		// on rajoute le coût supplémentaire si la case contient un adversaire
		if(opponentCost>0)
		{	List<AiHero> opponents = new ArrayList<AiHero>(zone.getRemainingOpponents());
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
	 * Le coût d'un chemin correspond ici à sa distance 
	 * exprimée en pixels.
	 * 
	 * @param path
	 * 		Chemin à traiter.
	 * @return
	 * 		Le coût de ce chemin.
	 */
/*	@Override
	public double processCost(AiPath path)
	{	double result = path.getPixelDistance();
		return result;
	}
*/	
}
