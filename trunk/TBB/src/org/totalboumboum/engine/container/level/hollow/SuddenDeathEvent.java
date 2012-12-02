package org.totalboumboum.engine.container.level.hollow;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SuddenDeathEvent implements Comparable<SuddenDeathEvent>
{	/** Class id */
	private static final long serialVersionUID = 1L;

	public SuddenDeathEvent(long time)
	{	// time
		this.time = time;
	}

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected long time;
	
	public long getTime()
	{	return time;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<Tile,List<Sprite>> spriteMap = new HashMap<Tile, List<Sprite>>();
	
	public HashMap<Tile,List<Sprite>> getSprites()
	{	return spriteMap;
	}
	
	public void addSprites(Tile tile, List<Sprite> sprites)
	{	this.spriteMap.put(tile,sprites);
	}

	/////////////////////////////////////////////////////////////////
	// COMPARABLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public int compareTo(SuddenDeathEvent o)
	{	long time2 = o.getTime();
		int result = (int)Math.signum(time - time2);
		return result;
	}

	@Override
	public int hashCode()
	{	Long temp = new Long(time);
		return temp.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{	boolean result = false;
		
		if(obj!=null)
		{	if(obj instanceof SuddenDeathEvent)
			{	SuddenDeathEvent se = (SuddenDeathEvent) obj;
				result = time==se.getTime();
			}
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append(time + ":");
		for(Entry<Tile,List<Sprite>> entry: spriteMap.entrySet())
		{	Tile tile = entry.getKey();
			result.append(" " + tile + "\n");
			List<Sprite> list = entry.getValue();
			for(Sprite sprite: list)
				result.append("   " + sprite + "\n");
		}
		return result.toString();
	}
}
