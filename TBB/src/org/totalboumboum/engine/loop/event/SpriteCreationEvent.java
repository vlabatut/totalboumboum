package org.totalboumboum.engine.loop.event;

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.game.round.RoundVariables;

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

public class SpriteCreationEvent extends ReplayEvent
{
/*
	public SpriteCreationEvent(int id, long time, String name, Role role, int line, int col)
	{	this.id = id;
		this.time = time;
		this.name = name;
		this.role = role;
		this.line = line;
		this.col = col;
	}
*/
	public SpriteCreationEvent(Sprite sprite, String name)
	{	// identification
		id = sprite.getId();
		this.name = name;
		
		// time
		time = RoundVariables.loop.getTotalEngineTime();
		
		// role
		role = sprite.getRole();
		
		// color
		color = sprite.getColor();
		
		// location
		Tile tile = sprite.getTile();
		this.line = tile.getLine();
		this.col = tile.getCol();
	}

	/////////////////////////////////////////////////////////////////
	// ID				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int id;
	
	public int getId()
	{	return id;	
	}

	/////////////////////////////////////////////////////////////////
	// TIME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long time;
	
	public long getTime()
	{	return time;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	
	public String getName()
	{	return name;	
	}

	/////////////////////////////////////////////////////////////////
	// ROLE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Role role;
	
	public Role getRole()
	{	return role;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ROLE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PredefinedColor color;
	
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int line;
	private int col;
	
	public int getLine()
	{	return line;
	}

	public int getCol()
	{	return col;	
	}
}
