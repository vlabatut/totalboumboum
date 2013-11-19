package org.totalboumboum.ai.v201314.adapter.data.internal;

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
import org.totalboumboum.engine.content.sprite.floor.Floor;

/**
 * Représente un sol du jeu, i.e. le graphisme affiché en tant que première couche de toute
 * case de la zone (et éventuellement recouvert par les autres types de sprites).
 * 
 * @author Vincent Labatut
 */
final class AiDataFloor extends AiDataSprite<Floor> implements AiFloor
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crée une représentation du sol passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile
	 * 		Case contenant le sprite.
	 * @param sprite
	 * 		Ssprite à représenter.
	 */
	protected AiDataFloor(AiDataTile tile, Floor sprite)
	{	super(tile,sprite);		
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void update(AiDataTile tile, long elapsedTime)
	{	super.update(tile,elapsedTime);
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
