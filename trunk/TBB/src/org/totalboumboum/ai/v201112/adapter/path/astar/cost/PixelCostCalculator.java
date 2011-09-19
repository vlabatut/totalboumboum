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

/**
 * Classe �tendant la classe abstraite CostCalculator de la mani�re à déterminer
 * le co�t en fonction de la distance en pixel entre les cases.
 * Cela ne change rien pour toutes les cases sauf la première, car en fonction
 * de la position du point de départ, deux chemins peuvent correspondre à la même
 * distance si on considère les cases, mais une distance différente si on consid�re
 * les pixels.
 * <b>Attention :</b> le point de départ doit obligatoirement être mis à 
 * jour avant chaque nouvel appel à A*. 
 * 
 * @author Vincent Labatut
 *
 */
public class PixelCostCalculator extends CostCalculator
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
	 * Les deux cases sont suppos�es être voisines, 
	 * on se contente de renvoyer la distance en pixels entre leurs centres.
	 * Sauf si la case start correspond à la première case
	 * du chemin : l�, on renvoie la distance entre le point
	 * de départ et le centre de la case suivante.
	 * 
	 * @param start	
	 * 		la case de départ
	 * @param end	
	 * 		la case d'arrivée
	 * @return 
	 * 		la distance entre ces cases
	 */ 
	@Override
	public double processCost(AiTile start, AiTile end) throws StopRequestException
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
	 * le co�t d'un chemin correspond ici à sa distance 
	 * exprimée en pixels.
	 * 
	 * @param path
	 * 		chemin à traiter
	 * @return
	 * 		le co�t de ce chemin
	 */
	public double processCost(AiPath path) throws StopRequestException
	{	double result = path.getPixelDistance();
		return result;
	}
}
