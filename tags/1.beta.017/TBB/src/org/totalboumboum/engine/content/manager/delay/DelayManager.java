package org.totalboumboum.engine.content.manager.delay;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.sprite.Sprite;


public class DelayManager
{	
	public static final String DL_DEFEAT = "DL_DEFEAT";
	public static final String DL_ENTER = "DL_ENTER";
	public static final String DL_EXPLOSION = "DL_EXPLOSION";
	public static final String DL_LATENCY = "DL_LATENCY";
	public static final String DL_OSCILLATION = "DL_OSCILLATION";
	public static final String DL_REBIRTH = "DL_REBIRTH";
	public static final String DL_RELEASE = "DL_RELEASE";
	public static final String DL_SPAWN = "DL_SPAWN";
	public static final String DL_START = "DL_START";
	public static final String DL_VICTORY = "DL_VICTORY";
	public static final String DL_WAIT = "DL_WAIT";
	

	public DelayManager(Sprite sprite)
	{	this.sprite = sprite;
		delays = new HashMap<String,Double>();
		addedDelays = new HashMap<String,Double>();
		removedDelays = new ArrayList<String>();
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite sprite;

	/////////////////////////////////////////////////////////////////
	// DELAYS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,Double> delays;
	private HashMap<String,Double> addedDelays;
	private ArrayList<String> removedDelays;

	public void addDelay(String name, double duration)
	{	addedDelays.put(name, duration);		
	}
	
	public void addIterDelay(String name, int iterations)
	{	double period = Configuration.getEngineConfiguration().getMilliPeriod();
		double speedCoeff = Configuration.getEngineConfiguration().getSpeedCoeff();
		double duration = iterations*period*speedCoeff;
		addDelay(name,duration);
	}
	
	public void removeDelay(String name)
	{	removedDelays.add(name);		
	}
	
	/**
	 * returns the current delay associated to the name parameter,
	 * or a negative value if no delay is associated to the name. 
	 * @param name
	 * @return	a double corresponding to a delay
	 */
	public double getDelay(String name)
	{	double result = -1;
		if(delays.containsKey(name))
			result = delays.get(name);
		else if(addedDelays.containsKey(name))
			result = addedDelays.get(name);
		return result;
	}
	public boolean hasDelay(String name)
	{	return delays.containsKey(name) || addedDelays.containsKey(name);		
	}

	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void update()
	{	
//if(sprite instanceof Bomb)
//	System.out.println();
		
		// added delays
		{	Iterator<Entry<String,Double>> i = addedDelays.entrySet().iterator();
			while(i.hasNext())
			{	Entry<String,Double> temp = i.next();
				Double value = temp.getValue();
				String key = temp.getKey();
				delays.put(key,value);
				i.remove();
			}
		}
		// removed delays
		{	Iterator<String> i = removedDelays.iterator();
			while(i.hasNext())
			{	String key = i.next();
				delays.remove(key);
				i.remove();
			}
		}
		
		// update remaining delays
		{	double period = Configuration.getEngineConfiguration().getMilliPeriod();
			double speedCoeff = Configuration.getEngineConfiguration().getSpeedCoeff();
			Iterator<Entry<String,Double>> i = delays.entrySet().iterator();
			while(i.hasNext())
			{	Entry<String,Double> temp = i.next();
				String name = temp.getKey();
				double duration = temp.getValue() - period*speedCoeff;
				if(duration<=0)
				{	i.remove();
					sprite.processEvent(new EngineEvent(EngineEvent.DELAY_OVER,name));
				}
				else
					temp.setValue(duration);
			}
		}
	}
}
