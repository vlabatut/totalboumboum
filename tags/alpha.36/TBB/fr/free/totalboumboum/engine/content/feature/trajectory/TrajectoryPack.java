package fr.free.totalboumboum.engine.content.feature.trajectory;

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
}
