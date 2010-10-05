package org.totalboumboum.engine.loop.display;

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

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class DisplayManager
{
	/////////////////////////////////////////////////////////////////
	// EVENT 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void provessEvent(SystemControlEvent event)
	{	Display d = displaysMap.get(event.getName());
		if(d!=null)
			d.switchShow(event);
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAYS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Display> displaysList = new ArrayList<Display>();
	private HashMap<String,Display> displaysMap = new HashMap<String, Display>();
	
	public void addDisplay(Display d)
	{	displaysList.add(d);
		displaysMap.put(d.getEventName(),d);
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void draw(Graphics g)
	{	for(Display d: displaysList)
			d.draw(g);		
	}
}
