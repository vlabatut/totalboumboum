package org.totalboumboum.ai.v201011.adapter.model;

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

import org.totalboumboum.ai.v201011.adapter.data.AiFloor;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;

/**
 * simule un sol du jeu, ie le graphisme affiché en tant que première couche de toute
 * case de la zone (et éventuellement recouvert par les autres types de sprites).
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
final class AiSimFloor extends AiSimSprite implements AiFloor
{
	/**
	 * crée une simulation du sol passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		numéro d'identification du sol
	 * @param tile
	 * 		case contenant le sol
	 * @param posX
	 * 		abscisse du sol
	 * @param posY
	 * 		ordonnée du sol
	 * @param posZ
	 * 		hauteur du sol (forcément 0)
	 * @param state
	 * 		état du sol
	 * @param burningDuration
	 * 		durée de combustion du sol
	 * @param currentSpeed
	 * 		vitesse courante de déplacement du sol (inutile ici)
	 */
	protected AiSimFloor(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
	}	

	/**
	 * crée une simulation du sol passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param floor
	 * 		sprite à simuler
	 * @param tile
	 * 		case contenant le sprite
	 */
	protected AiSimFloor(AiFloor floor, AiSimTile tile)
	{	super(floor,tile);		
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	return true;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Floor: [");
		result.append(super.toString());
		result.append(" ]");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void finish()
	{	super.finish();
	}
}
