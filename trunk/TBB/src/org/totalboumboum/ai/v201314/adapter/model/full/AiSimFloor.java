package org.totalboumboum.ai.v201314.adapter.model.full;

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

import org.totalboumboum.ai.v201314.adapter.data.AiFloor;
import org.totalboumboum.ai.v201314.adapter.data.AiSprite;

/**
 * Simule un sol du jeu, ie le graphisme affiché en tant que première couche de toute
 * case de la zone (et éventuellement recouvert par les autres types de sprites).
 * 
 * @author Vincent Labatut
 */
public final class AiSimFloor extends AiSimSprite implements AiFloor
{
	/**
	 * Crée une simulation du sol passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		Numéro d'identification du sol.
	 * @param tile
	 * 		Case contenant le sol.
	 * @param posX
	 * 		Abscisse du sol.
	 * @param posY
	 * 		Ordonnée du sol.
	 * @param posZ
	 * 		Hauteur du sol (forcément 0).
	 * @param state
	 * 		État du sol.
	 * @param burningDuration
	 * 		Durée de combustion du sol.
	 * @param currentSpeed
	 * 		Vitesse courante de déplacement du sol (inutile ici).
	 */
	protected AiSimFloor(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
	}	

	/**
	 * Crée une simulation du sol passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile
	 * 		Case contenant le sprite
	 * @param floor
	 * 		Sprite à simuler.
	 */
	protected AiSimFloor(AiSimTile tile, AiFloor floor)
	{	super(tile,floor);		
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
