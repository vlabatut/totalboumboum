package org.totalboumboum.ai.v201112.adapter.path.heuristic;

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

import java.util.Set;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimeFullSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;

/**
 * Heuristique utilisant la distance de Manhattan exprimées en pixels,
 * et surtout le temps nécessaire à son parcours.
 * <br/>
 * Cette classe nécessite que le temps soit considéré aussi par les autres
 * fonctions, donc il faut l'utiliser conjointement à :
 * <ul>
 * 		<li>Fonction de coût :
 * 			<ul>
 * 				<li>{@link TimeCostCalculator}</li>
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
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public class TimeHeuristicCalculator extends HeuristicCalculator
{
	/**
	 * Crée une nouvelle fonction heuristique basée sur le temps.
	 * La vitesse de déplacement utilisée lors de l'application
	 * de A* sera celle du personnage passé en paramètre.
	 * 
	 * @param ai
	 * 		IA de référence.
	 * @param hero
	 * 		Personnage de référence pour calculer la durée des déplacements.
	 */
	public TimeHeuristicCalculator(ArtificialIntelligence ai, AiHero hero)
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
	 * pour calculer la durée des déplacements.
	 * 
	 * @return
	 * 		Le personnage de référence.
	 */
	public AiHero getHero()
	{	return hero;
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
		
		// on calcule la distance de Manhattan en pixels
		for(AiTile endTile: endTiles)
		{	//double endX = endTile.getPosX();
			//double endY = endTile.getPosY();
			double h = zone.getPixelDistance(location,endTile);
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
	 * Ici, on considère le temps nécessaire pour parcourir cette distance exprimée en pixels.
	 * L'intérêt est d'avoir une fonction heuristique cohérente avec la fonction
	 * de coût basée sur le temps implémentée par {@link TimeCostCalculator}.
	 * 
	 * @param location	
	 * 		L'emplacement concerné. 
	 * @return	
	 * 		Le temps nécessaire pour parcourir la distance de Manhattan entre 
	 * 		l'emplacement passé en paramètre et la plus proche des cases contenues 
	 * 		dans le champ {@code endTiles}.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	@Override
	public double processHeuristic(AiLocation location) throws StopRequestException
	{	// init
		double speed = hero.getWalkingSpeed();
		Set<AiTile> endTiles = getEndTiles();
		AiZone zone = location.getZone();
		double minH = Integer.MAX_VALUE;
		
		// on calcule la distance de Manhattan en pixels
		for(AiTile endTile: endTiles)
		{	//double endX = endTile.getPosX();
			//double endY = endTile.getPosY();
			double h = zone.getPixelDistance(location,endTile);
			if(h < minH)
				minH = h;
		}
		
		// on calcule le temps nécessaire au parcours de cette distance
		long result = Math.round(minH/speed * 1000);
		return result;
	}
}
