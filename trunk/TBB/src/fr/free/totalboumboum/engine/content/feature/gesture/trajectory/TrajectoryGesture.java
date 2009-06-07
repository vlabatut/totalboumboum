package fr.free.totalboumboum.engine.content.feature.gesture.trajectory;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoryDirection;


public class TrajectoryGesture
{	private HashMap<Direction,TrajectoryDirection> trajectories;
	private String name; //debug
	
	public TrajectoryGesture()
	{	trajectories = new HashMap<Direction,TrajectoryDirection>();
	}

	public TrajectoryDirection getTrajectoryDirection(Direction direction)
	{	TrajectoryDirection result = trajectories.get(direction);
		if(result==null)
		{	result = new TrajectoryDirection();
			result.setGestureName(name);
			result.setDirection(direction);
			setTrajectoryDirection(direction,result);
		}
		return result;
	}
	public void addTrajectoryDirection(TrajectoryDirection trajectoryDirection)
	{	trajectories.put(trajectoryDirection.getDirection(), trajectoryDirection);
	}
	public void setTrajectoryDirection(Direction direction,TrajectoryDirection trajectoryDirection)
	{	trajectories.put(direction, trajectoryDirection);
	}
	
	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// gestures
			{	Iterator<Entry<Direction,TrajectoryDirection>> it = trajectories.entrySet().iterator();
				while(it.hasNext())
				{	Entry<Direction,TrajectoryDirection> t = it.next();
					TrajectoryDirection temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
		}
	}
	
	public TrajectoryGesture copy()
	{	TrajectoryGesture result = new TrajectoryGesture();
		Iterator<Entry<Direction,TrajectoryDirection>> it = trajectories.entrySet().iterator();
		while(it.hasNext())
		{	Entry<Direction,TrajectoryDirection> t = it.next();
			TrajectoryDirection value = t.getValue().copy();
			Direction key = t.getKey();
			result.setTrajectoryDirection(key,value);
		}
		result.name = name;
		result.finished = finished;
		return result;
	}
}
