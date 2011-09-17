package org.totalboumboum.ai.v201112.adapter.model;

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

import org.totalboumboum.ai.v201112.adapter.data.AiFloor;
import org.totalboumboum.ai.v201112.adapter.data.AiSprite;

/**
 * simule un sol du jeu, ie le graphisme affich� en tant que premi�re couche de toute
 * case de la zone (et �ventuellement recouvert par les autres types de sprites).
 * 
 * @author Vincent Labatut
 *
 */
final class AiSimFloor extends AiSimSprite implements AiFloor
{
	/**
	 * cr�e une simulation du sol pass� en param�tre,
	 * avec les propri�t�s pass�es en param�tres.
	 * 
	 * @param id
	 * 		num�ro d'identification du sol
	 * @param tile
	 * 		case contenant le sol
	 * @param posX
	 * 		abscisse du sol
	 * @param posY
	 * 		ordonn�e du sol
	 * @param posZ
	 * 		hauteur du sol (forc�ment 0)
	 * @param state
	 * 		�tat du sol
	 * @param burningDuration
	 * 		dur�e de combustion du sol
	 * @param currentSpeed
	 * 		vitesse courante de d�placement du sol (inutile ici)
	 */
	protected AiSimFloor(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
	}	

	/**
	 * cr�e une simulation du sol pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param floor
	 * 		sprite � simuler
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
