package org.totalboumboum.ai.v201112.adapter.path.astar.cost;

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

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.BasicSuccessorCalculator;

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
	 * utilisant l'IA passée en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		IA de référence.
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
	 * @param current
	 * 		L'emplacement de départ. 
	 * @param next	
	 * 		L'emplacement d'arrivée (case voisine de la case courante).
	 * @return	
	 * 		Le coût du déplacement entre les deux emplacements (ici : 1).
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */ 
	@Override
	public double processCost(AiLocation current, AiLocation next) throws StopRequestException
	{	return 1;		
	}

	/**
	 * le coût d'un chemin correspond ici à sa distance exprimée
	 * en cases.
	 * 
	 * @param path
	 * 		chemin à traiter
	 * @return
	 * 		le coût de ce chemin
	 */
/*	@Override
	public double processCost(AiPath path) throws StopRequestException
	{	double result = path.getTileDistance();
		return result;
	}
*/	
}
