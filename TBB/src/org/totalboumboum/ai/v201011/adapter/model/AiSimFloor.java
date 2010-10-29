package org.totalboumboum.ai.v201011.adapter.model;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

/**
 * simule un sol du jeu, ie le graphisme affich� en tant que premi�re couche de toute
 * case de la zone (et �ventuellement recouvert par les autres types de sprites).
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimFloor extends AiSimSprite
{
	/**
	 * cr�e une simulation du sol pass� en param�tre,
	 * avec les propri�t�s pass�es en param�tres.
	 * 
	 * @param tile	case contenant le sprite
	 */
	public AiSimFloor(AiSimTile tile)
	{	super(tile);
	}	

	/**
	 * cr�e une simulation du sol pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite � simuler
	 */
	AiSimFloor(AiFloor sprite, AiSimTile tile)
	{	super(sprite,tile);		
	}

	/**
	 * construit une simulation du sol pass� en param�tre,
	 * (donc une simple copie) et la place dans la case indiqu�e.
	 * 
	 * @param sprite	simulation � copier
	 * @param tile	simulation de la case devant contenir la copie
	 */
	public AiSimFloor(AiSimFloor sprite, AiSimTile tile)
	{	super(sprite,tile);
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean isCrossableBy(AiSimSprite sprite)
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
	void finish()
	{	super.finish();
	}
}
