package fr.free.totalboumboum.engine.content.feature.trajectory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryDirection;


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
}
