package org.totalboumboum.ai.v201112.adapter.path.heuristic;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.util.Set;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.cost.PixelCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.BasicSuccessorCalculator;

/**
 * Heuristique utilisant la <a> href="http://fr.wikipedia.org/wiki/Distance_de_Manhattan">distance de Manhattan</a>
 * exprimées en pixels, pour fonctionner avec {@link PixelCostCalculator}.
 * <br/>
 * La classe est compatible avec :
 * <ul>
 * 		<li>Fonction de coût :
 * 			<ul>
 * 				<li>{@link PixelCostCalculator}</li>
 * 			</ul>
 * 		</li> 
 * 		<li>Fonctions successeurs :
 * 			<ul>
 * 				<li>{@link BasicSuccessorCalculator}</li>
 * 			</ul>
 * 		</li> 
 * </ul>
 * <br/>
 * <b>Attention :<b/> cette classe ne permet pas de gérer des
 * chemins contenant des attentes. Par contre, à la différence
 * de {@link TileHeuristicCalculator}, elle gère les distances
 * en pixels.
 * 
 * @author Vincent Labatut
 */
public class PixelHeuristicCalculator extends HeuristicCalculator
{
	/**
	 * Construit une fonction heuristique
	 * utilisant l'IA passée en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		IA de référence.
	 */
	public PixelHeuristicCalculator(ArtificialIntelligence ai)
	{	super(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// END TILES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public AiTile getClosestEndTile(AiLocation location)
	{	// init
		Set<AiTile> endTiles = getEndTiles();
		AiZone zone = location.getZone();
		double minH = Integer.MAX_VALUE;
		AiTile result = null;
		
		for(AiTile endTile: endTiles)
		{	double h = zone.getPixelDistance(location,endTile);
			if(h < minH)
			{	minH = h;
				result = endTile;
			}
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * L'heuristique la plus simple consiste à prendre la distance
	 * de Manhattan entre la case courante tile et la case d'arrivée endTile.
	 * cf. <a href="http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29#Distance_sur_des_espaces_vectoriels">Wikipedia</a>.
	 * Ici, on calcule cette distance exprimée en pixels plutôt qu'en cases
	 * comme c'est le cas dans {@link TileHeuristicCalculator}.
	 * 
	 * @param location	
	 * 		L'emplacement concerné. 
	 * @return	
	 * 		La distance de Manhattan entre l'emplacement passé en paramètre
	 * 		et la plus proche des cases contenues dans le champ {@code endTiles}.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	@Override
	public double processHeuristic(AiLocation location) throws StopRequestException
	{	// init
		Set<AiTile> endTiles = getEndTiles();
		AiZone zone = location.getZone();
		double result = Integer.MAX_VALUE;
		
		for(AiTile endTile: endTiles)
		{	//double endX = endTile.getPosX();
			//double endY = endTile.getPosY();
			double h = zone.getPixelDistance(location,endTile);
			if(h < result)
				result = h;
		}
		
		return result;
	}
}
