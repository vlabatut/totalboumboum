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
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiStateName;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.AiModel;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v201112.adapter.path.dybref.DybrefNode;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette implémentation de la fonction successeur permet de traiter explicitement
 * le temps. L'algorithme va considérer non seulement un déplacement dans les
 * 4 cases voisines, mais aussi l'action d'attendre. Par exemple, cette approche
 * permet de considérer le cas où le joueur va attendre qu'un feu (présent dans
 * une case voisine, et qui l'empêche de passer) disparaisse. Par conséquent,
 * on envisagera aussi de passer sur des cases que l'algorithme a déjà traitées.<br>
 * Le temps de traitement sera donc beaucoup plus long que pour les autres fonctions
 * successeurs. Cette approche ne doit donc <b>pas être utilisée souvent</b>, car elle
 * va vraisemblablement ralentir l'agent significativement. 
 * 
 * @author Vincent Labatut
 */
public class TimeSuccessorCalculator extends SuccessorCalculator
{
	/**
	 * Crée une nouvelle fonction successeur basée sur le temps.
	 * La vitesse de déplacement utilisée lors de l'application
	 * de A* sera celle du personnage passé en paramètre.
	 * 
	 * @param ai
	 * 		IA de référence pour gérer les interruptions.
	 * @param hero
	 * 		Personnage de référence pour calculer la durée des déplacements.
	 */
	public TimeSuccessorCalculator(ArtificialIntelligence ai, AiHero hero)
	{	super(ai);
		this.hero = hero;
	}
	
	/////////////////////////////////////////////////////////////////
	// HERO						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Personnage concerné par la recherche de chemin */
	private AiHero hero;

	/**
	 * Renvoie le personnage utilisé
	 * pour calculer les actions possibles.
	 * 
	 * @return
	 * 		Le personnage de référence.
	 */
	public AiHero getHero()
	{	return hero;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Fonction successeur considérant à la fois les 4 cases 
	 * voisines de la case courante, comme pour {@link BasicSuccessorCalculator},
	 * mais aussi la possibilité d'attendre dans la case courante.
	 * Autre différence : les cases déjà traversées sont considérées,
	 * car le chemin peut inclure des retours en arrière pour éviter
	 * des explosions.
	 * 
	 * @param node	
	 * 		Le noeud de recherche courant.
	 * @return	
	 * 		La liste des noeuds fils.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	@Override
	public List<AstarNode> processSuccessors(AstarNode node) throws StopRequestException
	{	ai.checkInterruption();
		
		AiLocation location = node.getLocation();
		AiTile tile = location.getTile();
		AiZone zone = node.getAi().getZone();
		List<AstarNode> result = new ArrayList<AstarNode>();
		
		// on considère chaque déplacement possible
		List<Direction> directions = Direction.getPrimaryValues();
		for(Direction direction: directions)
		{	// on récupère la case cible
			AiTile targetTile = tile.getNeighbor(direction);
			
			// cette case ne doit pas avoir été visitée depuis la dernière pause
			if(!node.hasBeenExploredSincePause(targetTile))
			{	// on applique le modèle pour obtenir la zone résultant de l'action
				AiModel model = new AiModel(zone);
				model.applyChangeHeroDirection(hero,direction);
				
				// on simule jusqu'au changement d'état du personnage : 
				// soit le changement de case, soit l'élimination
				boolean safe = model.simulate(hero);
				long duration = model.getDuration();
				
				// si le joueur a survécu et si une action a bien eu lieu
				if(safe && duration>0)
				{	// on récupère la nouvelle case occupée par le personnage
					AiZone futureZone = model.getCurrentZone();
					AiHero futureHero = futureZone.getHeroByColor(hero.getColor());
					AiTile futureTile = futureHero.getTile();
					AiLocation futureLocation = new AiLocation(futureHero.getPosX(),futureHero.getPosX(),futureZone);
					
					// on teste si l'action a bien réussi : s'agit-il de la bonne case ?
					if(futureTile.equals(targetTile))
					{	// on crée le noeud fils correspondant (qui sera traité plus tard)
						AstarNode child = new AstarNode(futureLocation,node);
						result.add(child);
					}
					// si la case n'est pas la bonne : 
					// la case ciblée n'était pas traversable et l'action est à ignorer
				}
				// si le joueur n'est plus vivant dans la zone obtenue : 
				// la case ciblée n'est pas sûre et est ignorée
			}
			// si la case a déjà été visitée depuis la dernière pause,
			// on l'ignore car il est inutile d'y repasser
		}
		
		// considère éventuellement l'action d'attente, 
		// si un obstacle temporaire est présent dans une case voisine
		// l'obstacle temporaire peut être : du feu, une bombe, un mur destructible, une menace d'explosion.
		boolean temporaryObstacle = false;
		Iterator<Direction> it = directions.iterator();
		while(!temporaryObstacle && it.hasNext())
		{	// on récupère la case cible
			Direction direction = it.next();
			AiTile targetTile = tile.getNeighbor(direction);
			
			// on teste la présence d'un obstacle abstrait : menace d'une explosion
			
			
			// on teste la présence d'un obstacle concret
			if(!targetTile.isCrossableBy(hero))
			{	// on teste la nature temporaire de cet obstacle : 
				// pour l'instant seulement le feu et les bombes
				// NOTE à compléter si on inclut un jour des murs indestructibles mobiles
				temporaryObstacle = !targetTile.getFires().isEmpty()
					|| !targetTile.getBombs().isEmpty();
			}
		}
		// s'il y a un obstacle, on considère donc l'attente
		if(temporaryObstacle)
		{
			
		}
		
		
		
		
		// init
		AiTile tile = node.getLocation().getTile();
		AiHero hero = node.getHero();
		
		// pour chaque case voisine :
		for(Direction direction: Direction.getPrimaryValues())
		{	// on simule le déplacement dans la direction choisie
			
			
			
			AiTile neighbor = tile.getNeighbor(direction);
			
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
	/**
	 * 
	 * 
	 * @return
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	private long getWaitDuration() throws StopRequestException
	{	ai.checkInterruption();
		
		// init
		long result = Long.MAX_VALUE;
		List<AiTile> neighbors = tile.getNeighbors();
		
		// is there a fire in the neighbor tiles?
		List<AiFire> fires = zone.getFires();
		for(AiFire fire: fires)
		{	ai.checkInterruption();
			AiTile fireTile = fire.getTile();
			if(neighbors.contains(fireTile))
			{	long duration = fire.getBurningDuration() - fire.getState().getTime();
				if(duration<result)
					result = duration;
			}
		}
		
		// is one of the neighbor tiles within the range of some bomb?
		if(fires.isEmpty())
		{	List<AiBomb> bombs = zone.getBombs();
			for(AiBomb bomb: bombs)
			{	ai.checkInterruption();
				List<AiTile> blast = bomb.getBlast();
				List<AiTile> neigh = new ArrayList<AiTile>(neighbors);
				neigh.retainAll(blast);
				if(!neigh.isEmpty())
				{	AiStateName stateName = bomb.getState().getName();
					long normalDuration = bomb.getNormalDuration();
					// we can ignore burning bombs, since there's already fire in the tile
					if(stateName.equals(AiStateName.STANDING)
					// we can also ignore non-time bombs, since there's no mean to predict when they're going to explode
						&& normalDuration>0)
					{	long duration = normalDuration - bomb.getState().getTime() + bomb.getExplosionDuration();
						if(duration<result)
							result = duration;
					}
				}
			}
		}
		
		return result;
	}
}
