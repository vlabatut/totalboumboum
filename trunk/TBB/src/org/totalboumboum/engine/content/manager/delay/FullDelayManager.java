package org.totalboumboum.engine.content.manager.delay;

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

import java.util.Iterator;
import java.util.Map.Entry;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class FullDelayManager extends DelayManager
{	
	public FullDelayManager(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// DELAYS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void addDelay(String name, double duration)
	{	addedDelays.put(name, duration);		
	}
	
	@Override
	public void addIterDelay(String name, int iterations)
	{	double period = Configuration.getEngineConfiguration().getMilliPeriod();
		double speedCoeff = Configuration.getEngineConfiguration().getSpeedCoeff();
		double duration = iterations*period*speedCoeff;
		addDelay(name,duration);
	}
	
	@Override
	public void removeDelay(String name)
	{	removedDelays.add(name);		
	}
	
	@Override
	public double getDelay(String name)
	{	double result = -1;
		if(delays.containsKey(name))
			result = delays.get(name);
		else if(addedDelays.containsKey(name))
			result = addedDelays.get(name);
		return result;
	}
	
	@Override
	public boolean hasDelay(String name)
	{	return delays.containsKey(name) || addedDelays.containsKey(name);		
	}

	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
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
	
	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public DelayManager copy(Sprite sprite)
	{	DelayManager result = new FullDelayManager(sprite);
		return result;
	}
}
