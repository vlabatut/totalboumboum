package org.totalboumboum.ai.v200910.adapter.data;

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

import org.totalboumboum.engine.content.sprite.floor.Floor;

/**
 * représente un sol du jeu, ie le graphisme affiché en tant que première couche de toute
 * case de la zone (et éventuellement recouvert par les autres types de sprites).
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public class AiFloor extends AiSprite<Floor>
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * crée une représentation du sol passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à représenter
	 */
	AiFloor(AiTile tile, Floor sprite)
	{	super(tile,sprite);		
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	void update(AiTile tile)
	{	super.update(tile);
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean isCrossableBy(AiSprite<?> sprite)
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
