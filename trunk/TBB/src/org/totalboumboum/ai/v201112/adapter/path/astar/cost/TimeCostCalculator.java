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
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.astar.AstarLocation;

/**
 * Dans cette classe de coût, on ne s'intéresse pas à la distance parcourue,
 * mais plutôt au temps nécessaire pour parcourir le chemin.<br/>
 * Ceci permet de gérer les temps d'arrêt nécessaires pour laisser
 * certains obstacles tels que les bombes disparaître.
 * 
 * @author Vincent Labatut
 */
public class TimeCostCalculator extends CostCalculator
{	
	/////////////////////////////////////////////////////////////////
	// STARTING POINT			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** case de départ du chemin en cours de recherche */
	private AiTile startTile;
	/** abscisse de départ (doit être contenue dans la case de départ) */
	private double startX;
	/** ordonnée de départ (doit être contenue dans la case de départ) */
	private double startY;
	
	public void updateStartPoint(AiTile startTile, double startX, double startY)
	{	this.startTile = startTile;
		this.startX = startX;
		this.startY = startY;
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
	 * @param previous
	 * 		La case précédente.
	 * @param current
	 * 		La case courante (voisine de la précédente). 
	 * @param next	
	 * 		La case suivante (voisine de la courante).
	 * @return	
	 * 		la distance entre ces cases
	 */ 
	@Override
	public double processCost(AstarLocation previous, AstarLocation current, AstarLocation next) throws StopRequestException
	{	// init
		double startX = start.getPosX();
		double startY = start.getPosY();
		double endX = end.getPosX();
		double endY = end.getPosY();

		// specific case : start is the first tile of the considered path
		if(start.equals(startTile))
		{	startX = this.startX;
			startY = this.startY;
		}
		
		// process the pixel Manhattan distance
		AiZone zone = start.getZone();
		double result = zone.getPixelDistance(startX,startY,endX,endY);
		
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
	public double processCost(AiPath path) throws StopRequestException
	{	double result = path.getPixelDistance();
		return result;
	}
}


/**
 * - créer un objet AstarLocation contenant à la fois les positions en pixels et la case (>pratique pr gauler la simzone)
 * - au lieu de calculer le coût entre deux cases, on calcule le cout entre deux positions >> plus général
 */
