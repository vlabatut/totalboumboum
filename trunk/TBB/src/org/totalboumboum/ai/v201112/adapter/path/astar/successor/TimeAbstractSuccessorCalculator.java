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
import org.totalboumboum.ai.v201112.adapter.model.full.AiFullModel;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * TODO
 * Cette implémentation de la fonction successeur permet de traiter explicitement
 * le temps. L'algorithme va considérer non seulement un déplacement dans les
 * 4 cases voisines, mais aussi l'action d'attendre. Par exemple, cette approche
 * permet de considérer le cas où le joueur va attendre qu'un feu (présent dans
 * une case voisine, et qui l'empêche de passer) disparaisse. Par conséquent,
 * on envisagera aussi de passer sur des cases que l'algorithme a déjà traitées.<br>
 * Le temps de traitement sera donc beaucoup plus long que pour les autres fonctions
 * successeurs. Cette approche ne doit donc <b>pas être utilisée souvent</b>, car elle
 * va vraisemblablement ralentir l'agent significativement.<br/>
 * Cette classe nécessite que le temps soit considéré aussi par les autres
 * fonctions, donc il faut l'utiliser conjointement à :
 * <ul>
 * 		<li>Fonctions de coût :
 * 			<ul>
 * 				<li>{@link TimeCostCalculator}</li>
 * 			</ul>
 * 		</li> 
 * 		<li>Fonction heuristiques :
 * 			<ul>
 * 				<li>{@link TimeHeuristicCalculator}</li>
 * 			</ul>
 * 		</li> 
 * </ul>
 * 
 * @author Vincent Labatut
 */
public abstract class TimeAbstractSuccessorCalculator extends SuccessorCalculator
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
	public TimeAbstractSuccessorCalculator(ArtificialIntelligence ai, AiHero hero)
	{	super(ai);
		this.hero = hero;
	}
	
	/////////////////////////////////////////////////////////////////
	// HERO						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Personnage concerné par la recherche de chemin */
	protected AiHero hero;

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
	 * Détermine le temps d'attente minimal lorsque
	 * le joueur est placé dans la case passée en paramètre.
	 * 
	 * @param tile
	 * 		Case à considérer.
	 * @return
	 * 		Un entier long représentant le temps d'attente minimal.	
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected long getWaitDuration(AiTile tile) throws StopRequestException
	{	// init
		AiZone zone = tile.getZone();
		long result = Long.MAX_VALUE;
		List<AiTile> neighbors = tile.getNeighbors();
		
		// on s'intéresse d'abord aux obstacles concrets
		// on considère chaque case voisine une par une
		for(AiTile neighbor: neighbors)
		{	// s'il y a un obstacle concret
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
		HashMap<AiBomb,Long> delays = zone.getDelaysByBombs();
		List<AiBomb> bombs = zone.getBombs();
		for(AiBomb bomb: bombs)
		{	List<AiTile> blast = bomb.getBlast();
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
