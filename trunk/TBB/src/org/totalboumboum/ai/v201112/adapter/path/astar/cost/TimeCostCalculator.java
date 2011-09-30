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

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.astar.AstarLocation;

/**
 * Dans cette classe de coût, on ne s'intéresse pas à la distance parcourue,
 * mais plutôt au temps nécessaire pour parcourir le chemin.<br/>
 * Ceci permet de gérer les temps d'arrêt nécessaires pour laisser
 * certains obstacles tels que les bombes disparaître. Autrement dit,
 * cette classe gère les chemins au pixel près, et permet de tenir
 * compte des pauses.
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
	 * @param hero
	 * 		Personnage de référence pour calculer le coût temporel.
	 */
	public TimeCostCalculator(AiHero hero)
	{	this.hero = hero;
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
	 * @param current
	 * 		L'emplacement de départ. 
	 * @param next	
	 * 		L'emplacement d'arrivée (case voisine de la case courante).
	 * @return	
	 * 		Le temps nécessaire pour aller du départ à l'arrivée.
	 */ 
	@Override
	public double processCost(AstarLocation current, AstarLocation next) throws StopRequestException
	{	AiZone zone = current.getZone();
		double speed = hero.getWalkingSpeed();
		double distance = zone.getPixelDistance(current,next);
		double result = Math.round(distance/speed * 1000);
		return result;		
	}
}