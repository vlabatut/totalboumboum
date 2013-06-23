package org.totalboumboum.engine.loop.display.sprites;

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

import java.awt.Graphics;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * Hides/displays the sprites 
 * currently in the game, depending
 * on their {@link Role}.
 * 
 * @author Vincent Labatut
 */
public class DisplaySprites extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplaySprites(VisibleLoop loop)
	{	this.level = loop.getLevel();
		
		show = new HashMap<Role, Boolean>();
		for(Role role: roles)
			show.put(role,true);
		
		eventNames.add(SystemControlEvent.SWITCH_DISPLAY_SPRITES);
	}

	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current level */
	private Level level;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Order of the roles */
	private final List<Role> roles = Arrays.asList(
		Role.HERO,
		Role.BOMB,
		Role.FIRE,
		Role.ITEM,
		Role.BLOCK,
		Role.FLOOR
	);
	/** Indicates if the sprites should be drawn */
	private Map<Role,Boolean> show;
	/** Indicates which role is currently concerned */
	private int index = 0;
	
	@Override
	public synchronized void switchShow(SystemControlEvent event)
	{	if(event.getIndex()==SystemControlEvent.REGULAR)
			index = (index+1)%roles.size();
		else
		{	Role role = roles.get(index);
			boolean value = !show.get(role);
			show.put(role, value);
			level.setDrawSwitch(role, value);
		}
	}
	
	/**
	 * Returns the value indicating if
	 * some kind of sprite should be displayed
	 * 
	 * @param role
	 * 		The concerned kind of sprite.
	 * @return
	 * 		{@code true} if the specified kind of sprite should be displayed.
	 */
	private synchronized boolean getShow(Role role)
	{	return show.get(role);
	}
	
	/**
	 * Returns the value indicating the
	 * index of the currently selected role.
	 * 
	 * @return
	 * 		Index of the currently selected role.
	 */
	private synchronized int getIndex()
	{	return index;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Display message */
	private final String MESSAGE_SHOW = "Show all ";
	/** Hide message */
	private final String MESSAGE_HIDE = "Hide all ";
	/** End message */
	private final String MESSAGE_END = " sprites";
	
	@Override
	public String getMessage(SystemControlEvent event)
	{	String message = null;
		int index = getIndex();
		Role role = roles.get(index);
		boolean show = getShow(role);
		
		if(show)
			message = MESSAGE_SHOW;
		else
			message = MESSAGE_HIDE;
		
		message = message + role.name;
		message = message + MESSAGE_END;
	
		return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	// nothing to display
	}
}
