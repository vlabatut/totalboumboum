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
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.partial.AiExplosion;
import org.totalboumboum.ai.v201112.adapter.model.partial.AiPartialModel;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette implémentation de la fonction successeur permet de traiter explicitement
 * le temps. L'algorithme va considérer non seulement un déplacement dans les
 * 4 cases voisines, mais aussi l'action d'attendre. Par exemple, cette approche
 * permet de considérer le cas où le joueur va attendre qu'un feu (présent dans
 * une case voisine, et qui l'empêche de passer) disparaisse. Par conséquent,
 * on envisagera aussi de passer sur des cases que l'algorithme a déjà traitées.<br>
 * Le temps de traitement sera donc plus long que pour les autres fonctions
 * successeurs. Cette approche ne doit donc <b>pas être utilisée souvent</b>, car elle
 * va vraisemblablement ralentir l'agent significativement.<br/>
 * A noter que le modèle utilisé pour prédire l'évolution de la zone est
 * moins précis que celui utilisé dans {@link TimeFullSuccessorCalculator},
 * donc les calculs devraient être plus rapide (mais les résultats moins fiables)<br/>
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
public class TimePartialSuccessorCalculator extends SuccessorCalculator
{
	/**
	 * Crée une nouvelle fonction successeur basée sur le temps.
	 * La vitesse de déplacement utilisée lors de l'application
	 * de A* sera forcément celle du personnage contrôlé
	 * par l'agent passé en paramètre.
	 * 
	 * @param ai
	 * 		IA de référence pour gérer les interruptions.
	 */
	public TimePartialSuccessorCalculator(ArtificialIntelligence ai)
	{	super(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// MODELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** structure utilisée pour stocker les modèles partiels */
	private HashMap<AiSearchNode,AiPartialModel> models;
	
	@Override
	public void init()
	{	models = new HashMap<AiSearchNode, AiPartialModel>();
	}

	/**
	 * Renvoie le modèle associé au noeud de recherche
	 * passé en paramètre. Ce modèle correspond à la représentation
	 * interne (ici simplifiée) utilisée pour calculer
	 * les successeur du noeud de recherche.
	 * 
	 * @param searchNode
	 * 		Le noeud de recherche dont on veut le modèle.
	 * @return
	 * 		Le modèle associé au noeud de recherche, ou {@code null}
	 * 		si aucun modèle ne lui est associé.
	 */
	public AiPartialModel getModel(AiSearchNode searchNode)
	{	return models.get(searchNode);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Fonction successeur considérant à la fois les 4 cases 
	 * voisines de la case courante, comme pour {@link BasicSuccessorCalculator},
	 * mais aussi la possibilité d'attendre dans la case courante.<br/>
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
	public List<AiSearchNode> processSuccessors(AiSearchNode node) throws StopRequestException
	{	ai.checkInterruption();
		
		// init
		AiLocation location = node.getLocation();
		AiTile tile = location.getTile();
		List<AiSearchNode> result = new ArrayList<AiSearchNode>();
		AiPartialModel currentModel = models.get(node);
		AiZone zone = location.getZone();
		if(currentModel==null)
		{	currentModel = new AiPartialModel(zone);
			models.put(node,currentModel);
		}
		
		// on considère chaque déplacement possible
		List<Direction> directions = Direction.getPrimaryValues();
		for(Direction direction: directions)
		{	// on récupère la case cible
			AiTile targetTile = tile.getNeighbor(direction);
			
			// cette case ne doit pas avoir été visitée depuis la dernière pause
			if(!node.hasBeenExplored(targetTile))
			{	// on applique le modèle pour obtenir la zone résultant de l'action
				AiPartialModel model = new AiPartialModel(currentModel);
				
				// on simule jusqu'au changement d'état du personnage : 
				// soit le changement de case, soit l'élimination
				boolean safe = model.simulateMove(direction);
				long duration = model.getDuration();
				
				// si le joueur a survécu et si une action a bien eu lieu
				if(safe && duration>0)
				{	// on récupère la nouvelle case occupée par le personnage
					AiLocation futureLocation = model.getOwnLocation();
					AiTile futureTile = futureLocation.getTile();
					
					// on teste si l'action a bien réussi : s'agit-il de la bonne case ?
					if(futureTile.equals(targetTile))
					{	// on crée le noeud fils correspondant (qui sera traité plus tard)
						AiSearchNode child = new AiSearchNode(futureLocation,node);
						// on l'ajoute au noeud courant
						result.add(child);
						// on enregistre le modèle correspondant pour une utilisation ultérieure ici-même
						models.put(child,model);
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
		AiPartialModel model = new AiPartialModel(currentModel);
		long waitDuration = getWaitDuration(currentModel);
		if(waitDuration>0 && waitDuration<Long.MAX_VALUE)
		{	// on applique le modèle pour obtenir la zone résultant de l'action
			// on simule pendant la durée prévue
			boolean safe = model.simulateWait(waitDuration);
			long duration = model.getDuration();
			
			// si l'attente a bien eu lieu et si le perso est toujours en vie
			if(duration>0 && safe)
			{	// on crée le noeud fils correspondant (qui sera traité plus tard)
				AiSearchNode child = new AiSearchNode(waitDuration,zone,node);
				result.add(child);
			}
			// si l'action n'a pas eu lieu : problème lors de la simulation (?) 
			// >> l'attente n'est pas envisagée comme une action pertinente 
			// si le joueur n'est plus vivant dans la zone obtenue : 
			// >> l'attente n'était pas une action sûre, et n'est donc pas envisagée
		}
		// si le temps estimé d'attente est 0 ou +Inf, alors l'attente n'est pas envisagée

		return result;
	}
	
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
	private long getWaitDuration(AiPartialModel model) throws StopRequestException
	{	// init
		long result = Long.MAX_VALUE;
		AiTile tile = model.getOwnLocation().getTile();
		List<AiTile> neighbors = tile.getNeighbors();
		
		// on considère chaque case voisine une par une
		for(AiTile neighbor: neighbors)
		{	// si une explosion menace cette case
			AiExplosion explosion = model.getExplosion(neighbor);
			if(explosion!=null)
			{	long duration = explosion.getDuration();
				if(duration<result)
					result = duration;
			}
		}
		
		return result;
	}
}
