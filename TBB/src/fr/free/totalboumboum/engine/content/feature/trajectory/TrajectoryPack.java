package fr.free.totalboumboum.engine.content.feature.trajectory;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryDirection;


public class TrajectoryPack
{	private HashMap<String,TrajectoryGesture> gestures;
	
	public TrajectoryPack()
	{	gestures = new HashMap<String,TrajectoryGesture>();
	}

	public TrajectoryDirection getTrajectoryDirection(String name,Direction direction)
	{	TrajectoryGesture gesture = gestures.get(name);
		TrajectoryDirection result;
		if(gesture==null)
		{	result = new TrajectoryDirection();
			setTrajectoryDirection(name, direction, result);
		}
		else
			result = gesture.getTrajectoryDirection(direction);
		return result;
	}
	
	public void setTrajectoryDirection(String name, Direction direction, TrajectoryDirection trajectory)
	{	TrajectoryGesture gesture;
		if(!gestures.containsKey(name))
		{	gesture = new TrajectoryGesture();
			gesture.setName(name);
			gestures.put(name, gesture);
		}
		else
			gesture = gestures.get(name);
		gesture.setTrajectoryDirection(direction, trajectory);
	}
	
	public void addTrajectoryGesture(TrajectoryGesture trajectoryGesture)
	{	gestures.put(trajectoryGesture.getName(), trajectoryGesture);
	}
	public void setTrajectoryGesture(String name,TrajectoryGesture trajectoryGesture)
	{	gestures.put(name,trajectoryGesture);
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// gestures
			{	Iterator<Entry<String,TrajectoryGesture>> it = gestures.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,TrajectoryGesture> t = it.next();
					TrajectoryGesture temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
		}
	}
	
	public TrajectoryPack copy()
	{	TrajectoryPack result = new TrajectoryPack();
		Iterator<Entry<String,TrajectoryGesture>> it = gestures.entrySet().iterator();
		while(it.hasNext())
		{	Entry<String,TrajectoryGesture> t = it.next();
			TrajectoryGesture value = t.getValue().copy();
			String key = t.getKey();
			result.setTrajectoryGesture(key,value);
		}
		result.finished = finished;
		return result;
	}
}
