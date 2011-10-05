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
import java.util.HashMap;
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
		AiZone zone = location.getZone();
AiHero h = zone.getHeroByColor(hero.getColor());
AiTile t = h.getTile();
if(!t.equals(tile))
	System.out.println();
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
					AiLocation futureLocation = new AiLocation(futureHero.getPosX(),futureHero.getPosY(),futureZone);
					
					// on teste si l'action a bien réussi : s'agit-il de la bonne case ?
					if(futureTile.equals(targetTile))
					{	// on crée le noeud fils correspondant (qui sera traité plus tard)
						AstarNode child = new AstarNode(futureLocation,node);
						result.add(child);
//if(!child.getLocation().getTile().equals(child.getLocation().getTile().getZone().getHeroByColor(hero.getColor()).getTile()))
//	System.out.println();
						
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
		long waitDuration = getWaitDuration(tile);
		if(waitDuration>0 && waitDuration<Long.MAX_VALUE)
		{	// on applique le modèle pour obtenir la zone résultant de l'action
			AiModel model = new AiModel(zone);
			model.applyChangeHeroDirection(hero,Direction.NONE);
			
			// on simule pendant la durée prévue
			model.simulate(waitDuration);
			long duration = model.getDuration();
			
			// si l'attente a bien eu lieu
			if(duration>0)
			{	// on récupère les nouvelles infos décrivant le personnage
				AiZone futureZone = model.getCurrentZone();
				AiHero futureHero = futureZone.getHeroByColor(hero.getColor());
				
				// si le perso est toujours en vie, on crée le noeud de recherche
				if(futureHero!=null)
				{	AiStateName name = futureHero.getState().getName();
					boolean safe = !name.equals(AiStateName.BURNING) && !name.equals(AiStateName.ENDED);
					if(safe)
					{	// on crée le noeud fils correspondant (qui sera traité plus tard)
						AstarNode child = new AstarNode(waitDuration,futureZone,node);
						result.add(child);
if(!child.getLocation().getTile().equals(child.getLocation().getTile().getZone().getHeroByColor(hero.getColor()).getTile()))
	System.out.println();
					}
					// si le joueur n'est plus vivant dans la zone obtenue : 
					// l'attente n'était pas une action sûre, et n'est donc pas envisagée
				}
				// si le perso est null, alors c'est que le joueur n'est plus vivant (cf commentaire ci-dessus)
			}
			// si l'action n'a pas eu lieu : problème lors de la simulation (?) 
			// l'attente n'est pas envisagée comme une action pertinente 
		}
		// si le temps estimé d'attente est 0 ou +Inf, alors l'attente n'est pas envisagée

		return result;
	}
	/**
	 * Détermine le temps d'attente minimal lorsque
	 * le joueur est placé dans la case passée en paramètre.
	 * 
	 * 
	 * @tile
	 * 		Case à considérer.
	 * @return
	 * 		Un entier long représentant le temps d'attente minimal.	
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	private long getWaitDuration(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		
		// init
		AiZone zone = tile.getZone();
		long result = Long.MAX_VALUE;
		List<AiTile> neighbors = tile.getNeighbors();
		
		// on s'intéresse d'abord aux obstacles concrets
		// on considère chaque case voisine une par une
		for(AiTile neighbor: neighbors)
		{	ai.checkInterruption();
		
			// s'il y a un obstacle concret
			if(!neighbor.isCrossableBy(hero))
			{	// s'il y a du feu
				List<AiFire> fires = neighbor.getFires();
				for(AiFire fire: fires)
				{	long duration = fire.getBurningDuration() - fire.getState().getTime();
					if(duration<result)
						result = duration;
				}
				// les autres obstacles (murs, bombes) n'ont pas besoin d'être traités
				// car on s'en occupe lorsqu'on gère les cases menacées par des bombes
			}
		}
		
		// on s'intéresse ensuite aux menaces provenant des bombes
		// on considère chaque bombe une par une
		HashMap<AiBomb,Long> delays = zone.getBombDelays();
		List<AiBomb> bombs = zone.getBombs();
		for(AiBomb bomb: bombs)
		{	ai.checkInterruption();
		
			List<AiTile> blast = bomb.getBlast();
			List<AiTile> neigh = new ArrayList<AiTile>(neighbors);
			neigh.retainAll(blast);
			// on vérifie si la bombe menace une des cases voisines
			if(!neigh.isEmpty())
			{	// si c'est le cas, on récupère la description de la bombe
				AiStateName stateName = bomb.getState().getName();
				// on peut ignorer les bombes déjà en train de brûler, car elles cohabitent avec du feu
				if(stateName.equals(AiStateName.STANDING)
				// on peut aussi ignorer les bombes qui ne sont pas à retardement,
				// car on ne peut pas prédire quand elles vont exploser
					&& bomb.hasCountdownTrigger())
				{	//long duration = normalDuration - bomb.getState().getTime() + bomb.getExplosionDuration();
					long duration = delays.get(bomb);
					if(duration<result)
						result = duration;
				}
			}
		}
		
		return result;
	}
}