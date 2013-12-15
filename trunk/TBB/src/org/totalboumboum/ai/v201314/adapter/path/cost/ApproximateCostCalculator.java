package org.totalboumboum.ai.v201314.adapter.path.cost;

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
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.model.partial.AiExplosion;
import org.totalboumboum.ai.v201314.adapter.model.partial.AiExplosionList;
import org.totalboumboum.ai.v201314.adapter.model.partial.AiPartialModel;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;

/**
 * Dans cette classe de coût, on ne s'intéresse pas à la distance parcourue,
 * mais plutôt au temps nécessaire pour parcourir le chemin. Cependant,
 * on ne fait qu'une approximation du temps nécessaire.
 * <br/>
 * Cette classe doit être utilisée conjointement à :
 * <ul>
 * 		<li>Fonction heuristiques :
 * 			<ul>
 * 				<li>{@link NoHeuristicCalculator}</li>
 * 				<li>{@link TimeHeuristicCalculator}</li>
 * 			</ul>
 * 		</li> 
 * 		<li>Fonctions successeurs :
 * 			<ul>
 * 				<li>{@link ApproximateSuccessorCalculator}</li>
 * 			</ul>
 * 		</li>
 * </ul>
 * 
 * @author Vincent Labatut
 */
public class ApproximateCostCalculator extends CostCalculator
{
	/**
	 * Crée une nouvelle fonction de coût basée sur le temps.
	 * La vitesse de déplacement utilisée lors de l'application
	 * de A* sera celle du personnage passé en paramètre.
	 * <br/>
	 * A noter qu'un modèle est construit, mais qu'il n'est pas
	 * utilisé pour la simulation : seulement pour accéder
	 * aux temps d'explosions.
	 * 
	 * @param ai
	 * 		Agent de référence.
	 * @param hero
	 * 		Personnage de référence pour calculer le coût temporel.
	 */
	public ApproximateCostCalculator(ArtificialIntelligence ai, AiHero hero)
	{	super(ai);
	
		this.hero = hero;
		ownBombDuration = hero.getBombDuration();
		ownExplosionDuration = hero.getExplosionDuration();
		crossTileDuration = (int)(AiTile.getSize()/hero.getWalkingSpeed() * 1000);
		
		AiZone zone = ai.getZone();
		model = new AiPartialModel(zone);
	}
	
	@Override
	public void init(AiSearchNode root)
	{	this.hero = root.getHero();
		ownBombDuration = hero.getBombDuration();
		ownExplosionDuration = hero.getExplosionDuration();
		crossTileDuration = (int)(AiTile.getSize()/hero.getWalkingSpeed() * 1000);
		
		AiZone zone = ai.getZone();
		model = new AiPartialModel(zone);
	}
	
	/////////////////////////////////////////////////////////////////
	// MODEL					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le modèle utilisé lors de la simulation */
	private AiPartialModel model;
	
	/**
	 * Renvoie le modèle utilisé
	 * pour calculer les temps d'explosions.
	 * 
	 * @return
	 * 		Un modèle partiel.
	 */
	public AiPartialModel getModel()
	{	return model;
	}

	/////////////////////////////////////////////////////////////////
	// HERO						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Personnage concerné par la recherche de chemin */
	private AiHero hero;
	/** Durée d'une bombe pour ce personnage */
	private long ownBombDuration;
	/** Durée d'explosion d'une bombe pour ce personnage */
	private long ownExplosionDuration;
	/** Durée pour que ce personnage traverse une case */
	private long crossTileDuration;

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
	 * on se contente de renvoyer un coût temporel constituant
	 * une estimation du temps nécessaire pour traverser la
	 * case, en fonction de son contenu. Par exemple pour un
	 * mur destructible, on suppose qu'il faut le temps de poser
	 * une bombe, d'attendre qu'elle explose et de se déplacer dans
	 * la case. Pour une case en feu ou menacée par une bombe, il
	 * faut le temps d'attendre que l'explosion apparaisse et/ou
	 * disparaisse, et de se déplacer dans la case.  
	 * 
	 * @param current
	 * 		Le noeud contenant l'emplacement de départ. 
	 * @param next	
	 * 		L'emplacement d'arrivée (case voisine de la case courante).
	 * @return	
	 * 		Le temps nécessaire pour aller du départ à l'arrivée.
	 */ 
	@Override
	public double processCost(AiSearchNode current, AiLocation next)
	{	AiTile destination = next.getTile();
		double result = crossTileDuration;
		
		// si la case contient un bloc, il est forcément
		// destructible (cf. ApproximateSuccessorCalculator)
		// et on compte le temps pour exploser un bloc.
		List<AiBlock> blocks = destination.getBlocks();
		if(!blocks.isEmpty())
		{	// durée d'une bombe et de son explosion
			result = result + ownBombDuration + ownExplosionDuration;
		}
	
		// sinon, on verifie si la case est menacée 
		else
		{	AiExplosionList explosionList = model.getExplosionList(destination);
			if(explosionList!=null)
			{	long startTime = (long)current.getCost();
				long endTime = startTime + crossTileDuration;
				AiExplosion explosion = explosionList.getIntersection(startTime,endTime);
				if(explosion!=null)
				{	long explosionDuration = endTime - startTime;
					result = result + explosionDuration;
				}
			}
		}

		// on rajoute le coût supplémentaire si la case contient un adversaire
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
		
		return result;		
	}
}