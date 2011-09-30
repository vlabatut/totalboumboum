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
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
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
	 */
	@Override
	public List<AstarNode> processSuccessors(AstarNode node) throws StopRequestException
	{	AiZone zone = node.getAi().getZone();
	
		// le fait qu'il n'y ait plus de bombes ni d'explosions à proximité
		// du personnage considéré simplifie grandement les calculs
		boolean safeMode = zone.getFires().isEmpty() && zone.getBombs().isEmpty();

		
		
// on peut définir une fct qui hasBeenVisited qui s'arrête dès qu'on rencontre une attente
// l'attente n'est valide que si on a un obstacle à côté
// on ne peut pas revenir sur la case précédente (y a au moins une étape d'attente entre)
// on peut repasser sur une case déjà visitée à condition que ça ne soit pas dans la même branche temporelle (séparation via attente)
		
		
		
		
		
		// init
		List<AstarNode> result = new ArrayList<AstarNode>();
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
	
	private boolean isSafeMode(AiHero hero)
	{	boolean result = false;
		AiTile tile = hero.getTile();
		AiZone zone = tile.getZone();
		List<AiTile> neighbors = tile.getNeighbors();
		
		
		
		return result;
	}
}
