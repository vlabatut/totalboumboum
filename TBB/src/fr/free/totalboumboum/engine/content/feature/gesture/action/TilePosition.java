package fr.free.totalboumboum.engine.content.feature.gesture.action;

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

/**
 * relative positions of the actor and the target, expressed in terms of tiles.
 */
public enum TilePosition
{	/** no position can be defined: there's no target, or it has no tile, or the actor has no tile */
	UNDEFINED,
	/** the actor and target are together in the same tile */
	SAME,
	/** the actor and target are in neighbor tiles */
	NEIGHBOR,
	/** the actor and target are in remote tiles */
	REMOTE;
	
	/**
	 * returns the relative positions of the actor and target tiles,
	 * or SAME if the actor has no tile but the target has one,
	 * or UNDEFINED if there's no target or if it has no tile. 
	 */
	public static TilePosition getTilePosition(Sprite actor, Sprite target)
	{	TilePosition result;
		Tile actorTile = actor.getTile();
		Tile targetTile = null;
		if(target!=null)
			targetTile = target.getTile();
		// tile position is undefined
		if(actorTile==null || targetTile==null)
			result = TilePosition.UNDEFINED;
		else	
		{	// same tile
			if(actorTile==targetTile)
				result = TilePosition.SAME;
			// neighbor tiles
			else if(actorTile.isNeighbor(targetTile))
				result = TilePosition.NEIGHBOR;
			// not neighbor tiles
			else
				result = TilePosition.REMOTE;
		}
		return result;
	}	
}