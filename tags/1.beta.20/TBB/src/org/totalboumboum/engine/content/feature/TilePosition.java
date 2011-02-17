package org.totalboumboum.engine.content.feature;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.tools.xml.XmlNames;

/**
 * relative positions of the actor and the target, expressed in terms of tiles.
 * 
 * @author Vincent Labatut
 *
 */
public enum TilePosition implements Serializable
{	
	/** no position can be defined: there's no target */
	NONE,
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
			result = TilePosition.NONE;
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
	
	public static TilePosition getTilePosition(Sprite actor, Tile tile)
	{	Sprite target = tile.getFloors().get(0);
		TilePosition result = getTilePosition(actor,target);
		return result;
	}	

	/**
	 * load a tile position value.
	 * the XML value SOME represents any tile position except NONE. 
	 * the XML value ANY represents any tile position including NONE. 
	 */
	public static List<TilePosition> loadTilePositionsAttribute(Element root, String attName)
	{	List<TilePosition> result = new ArrayList<TilePosition>();
		Attribute attribute = root.getAttribute(attName);
		String tilePositionStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
		String[] tilePositionsStr = tilePositionStr.split(" ");
		for(String str: tilePositionsStr)
		{	if(str.equalsIgnoreCase(XmlNames.VAL_SOME))
			{	result.add(TilePosition.NEIGHBOR);
				result.add(TilePosition.REMOTE);
				result.add(TilePosition.SAME);
			}
			else if(str.equalsIgnoreCase(XmlNames.VAL_ANY))
			{	result.add(TilePosition.NEIGHBOR);
				result.add(TilePosition.REMOTE);
				result.add(TilePosition.SAME);
				result.add(TilePosition.NONE);
			}
			else
			{	TilePosition tilePosition = TilePosition.valueOf(str);
				result.add(tilePosition);
			}
		}
		return result;
	}
}