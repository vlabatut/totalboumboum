package org.totalboumboum.ai.v201112.adapter.path.astarcopy.cost;

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
 * Classe étendant la classe abstraite CostCalculator de la manière à déterminer
 * le coût en fonction de la distance en pixel entre les cases.<br/>
 * Cela ne change rien pour toutes les cases sauf la première, car en fonction
 * de la position du point de départ, deux chemins peuvent correspondre à la même
 * distance si on considère les cases, mais une distance différente si on considère
 * les pixels.<br/>
 * <b>Attention :</b> le point de départ doit obligatoirement être mis à 
 * jour à chaque nouvelle itération du moteur de jeu, avant l'appel à A*.<br/>
 *  Cette classe n'est pas conçue pour traiter les chemins contenant des
 *  retours en arrière. Voir {@link TimeCostCalculator} pour ça.
 * 
 * @author Vincent Labatut
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
	
	/**
	 * Cette méthosz permet de mettre jour tout changement 
	 * dans la position du joueur. Elle doit être utilisée à
	 * chaque nouvel appel du moteur de jeu.
	 * 
	 * @param startTile
	 * 		La nouvelle case occupée par le personnage.
	 * @param startX
	 * 		Sa nouvelle abscisse en pixels.
	 * @param startY
	 * 		Sa nouvelle ordonnée en pixels.
	 */
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
	 * <b>Note :</b> la première case est en fait ignorée. 
	 * 
	 * @param current
	 * 		La case courante (voisine de la précédente). 
	 * @param next	
	 * 		La case suivante (voisine de la courante).
	 * @return	
	 * 		La distance en pixels entre la cases courante et la case suivante.
	 */ 
	@Override
	public double processCost(AiTile current, AiTile next) throws StopRequestException
	{	// init
		double startX = current.getPosX();
		double startY = current.getPosY();
		double endX = next.getPosX();
		double endY = next.getPosY();

		// specific case : start is the first tile of the considered path
		if(current.equals(startTile))
		{	startX = this.startX;
			startY = this.startY;
		}
		
		// process the pixel Manhattan distance
		AiZone zone = current.getZone();
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
	@Override
	public double processCost(AiPath path) throws StopRequestException
	{	double result = path.getPixelDistance();
		return result;
	}
}
