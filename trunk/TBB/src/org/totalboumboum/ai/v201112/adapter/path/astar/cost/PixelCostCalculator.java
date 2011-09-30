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
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;

/**
 * Classe étendant la classe abstraite {@link CostCalculator} de la manière à déterminer
 * le coût en fonction de la distance en pixels entre deux emplacements.<br/>
 * Cette classe n'est pas conçue pour traiter les chemins contenant des
 * retours en arrière. Voir {@link TimeCostCalculator} pour ça.
 * 
 * @author Vincent Labatut
 */
public class PixelCostCalculator extends CostCalculator
{	
	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Les deux emplacements sont supposés être dans des cases voisines. 
	 * On renvoie la distance de Manhattan (en pixels) qui les sépare. 
	 * 
	 * @param current
	 * 		L'emplacement de départ. 
	 * @param next	
	 * 		L'emplacement d'arrivée (case voisine de la case courante).
	 * @return	
	 * 		La distance en pixels entre l'emplacement de départ et celui d'arrivée.
	 */ 
	@Override
	public double processCost(AiLocation current, AiLocation next) throws StopRequestException
	{	AiZone zone = current.getZone();
		double result = zone.getPixelDistance(current,next);
		return result;		
	}

	/**
	 * le coût d'un chemin correspond ici à sa distance 
	 * exprimée en pixels.
	 * 
	 * @param path
	 * 		chemin à traiter
	 * @return
	 * 		le coût de ce chemin
	 */
/*	@Override
	public double processCost(AiPath path) throws StopRequestException
	{	double result = path.getPixelDistance();
		return result;
	}
*/	
}
