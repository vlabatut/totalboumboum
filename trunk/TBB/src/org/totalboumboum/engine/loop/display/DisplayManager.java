package org.totalboumboum.engine.loop.display;

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
	{	List<Display> displays = displaysMap.get(event.getName());
		if(displays!=null)
		{	for(Display display: displays)
			{	display.switchShow(event);
				feedback.processEvent(event,display);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAYS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Display> displaysList = new ArrayList<Display>();
	private HashMap<String,List<Display>> displaysMap = new HashMap<String,List<Display>>();
	private DisplayFeedbackMessage feedback = new DisplayFeedbackMessage();
	
	public void addDisplay(Display display)
	{	// list
		displaysList.add(display);
		
		// map
		List<String> events = display.getEventNames();
		for(String event: events)
		{	List<Display> displays = displaysMap.get(event);
			if(displays==null)
				displays = new ArrayList<Display>();
			displays.add(display);
			displaysMap.put(event,displays);
		}
	}
	
	public void removeDisplay(Display display)
	{	// list
		displaysList.remove(display);
		
		// map
		List<String> events = display.getEventNames();
		for(String event: events)
		{	List<Display> displays = displaysMap.get(event);
			displays.remove(display);
			if(displays.isEmpty())
				displaysMap.remove(event);
			else
				displaysMap.put(event,displays);
		}
	}

	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void draw(Graphics g)
	{	for(Display d: displaysList)
			d.draw(g);
		if(!displaysList.isEmpty())
			feedback.draw(g);
	}
}
