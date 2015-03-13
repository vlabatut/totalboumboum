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
import org.totalboumboum.ai.v201415.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201415.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201415.adapter.path.search.AiSearchNode;
import org.totalboumboum.ai.v201415.adapter.path.successor.TimeFullSuccessorCalculator;
import org.totalboumboum.ai.v201415.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.ai.v201415.adapter.tools.AiPixelDistanceTools;

/**
 * Dans cette classe de coût, on ne s'intéresse pas à la distance parcourue,
 * mais plutôt au temps nécessaire pour parcourir le chemin.
 * <br/>
 * Ceci permet de gérer les temps d'arrêt nécessaires pour laisser
 * certains obstacles tels que les bombes disparaître. Autrement dit,
 * cette classe gère les chemins au pixel près, et permet de tenir
 * compte des pauses.
 * <br/>
 * Cette classe nécessite que le temps soit considéré aussi par les autres
 * fonctions, donc il faut l'utiliser conjointement à :
 * <ul>
 * 		<li>Fonction heuristiques :
 * 			<ul>
 * 				<li>{@link NoHeuristicCalculator}</li>
 * 				<li>{@link TimeHeuristicCalculator}</li>
 * 			</ul>
 * 		</li> 
 * 		<li>Fonctions successeurs :
 * 			<ul>
 * 				<li>{@link TimeFullSuccessorCalculator}</li>
 * 				<li>{@link TimePartialSuccessorCalculator}</li>
 * 			</ul>
 * 		</li>
 * </ul>
 * 
 * @author Vincent Labatut
 */
public class TimeCostCalculator extends CostCalculator
{
	/**
	 * Crée une nouvelle fonction de coût basée sur le temps.
	 * La vitesse de déplacement utilisée lors de l'application
	 * de A* sera celle du personnage passé en paramètre.
	 * 
	 * @param ai
	 * 		Agent de référence.
	 * @param hero
	 * 		Personnage de référence pour calculer le coût temporel.
	 */
	public TimeCostCalculator(ArtificialIntelligence ai, AiHero hero)
	{	super(ai);
		this.hero = hero;
	}
	
	@Override
	public void init(AiSearchNode root)
	{	this.hero = root.getHero();
	}

	/////////////////////////////////////////////////////////////////
	// HERO						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Personnage concerné par la recherche de chemin */
	private AiHero hero;

	/**
	 * Renvoie le personnage utilisé
	 * pour calculer le coût temporel.
	 * 
	 * @return
	 * 		Un personnage.
	 */
	public AiHero getHero()
	{	return hero;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Les deux cases sont supposées être voisines, 
	 * on se contente de renvoyer la distance en pixels entre leurs centres.
	 * Sauf si la case start correspond à la première case
	 * du chemin : là, on renvoie la distance entre le point
	 * de départ et le centre de la case suivante.
	 * 
	 * @param currentNode
	 * 		Le noeud contenant l'emplacement de départ. 
	 * @param nextLocation
	 * 		L'emplacement d'arrivée (case voisine de la case courante).
	 * @return	
	 * 		Le temps nécessaire pour aller du départ à l'arrivée.
	 */ 
	@Override
	public double processCost(AiSearchNode currentNode, AiLocation nextLocation)
	{	// on calcule le temps nécessaire pour aller de la position courante à la suivante
		AiLocation currentLocation = currentNode.getLocation();
		AiTile destination = nextLocation.getTile();
		AiZone zone = currentLocation.getZone();
		double speed = hero.getWalkingSpeed();
		AiPixelDistanceTools distTools = zone.getPixelDistanceTools();
		double distance = distTools.getDistance(currentLocation,nextLocation);
		double result = Math.round(distance/speed * 1000);
		
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
}