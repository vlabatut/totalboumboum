package fr.free.totalboumboum.engine.content.feature.trajectory;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;
import fr.free.totalboumboum.engine.content.sprite.Sprite;


public class TrajectoryDirection
{
	private ArrayList<TrajectoryStep> steps;
	private boolean repeat;
	private double xInteraction;
	private double yInteraction;
	private String gestureName; //debug
	private Direction direction; //debug
	//
	private boolean forceXPosition;
	private boolean forceYPosition;
	private boolean forceZPosition;
	private double forcedXPosition;
	private double forcedYPosition;
	private double forcedZPosition;
	private long forcedPositionTime;
	private boolean proportional;
	
	public TrajectoryDirection()
	{	gestureName="NA";
		steps = new ArrayList<TrajectoryStep>(0);
		repeat = false;
		xInteraction = 0;
		yInteraction = 0;
		//
		forceXPosition = false;
		forceYPosition = false;
		forceZPosition = false;
		forcedXPosition = 0;
		forcedYPosition = 0;
		forcedZPosition = 0;
		forcedPositionTime = 0;
		proportional = false;
	}
	
	public Iterator<TrajectoryStep> getIterator()
	{	return steps.iterator();		
	}
	public void add(TrajectoryStep trajectoryStep)
	{	steps.add(trajectoryStep);		
	}
	public void addAll(ArrayList<TrajectoryStep> trajectorySteps)
	{	steps.addAll(trajectorySteps);		
	}

	/**
	 * Compute the total duration of the animation.
	 * The result is 0 if there is no time limit. 
	 * @return	the duration of the animation
	 */
	public long getTotalDuration()
	{	long result = 0;
		Iterator<TrajectoryStep> i = steps.iterator();
		while(i.hasNext())
			result = result + i.next().getDuration();
		return result;
	}
	public double getTotalXShift()
	{	double result = 0;
		Iterator<TrajectoryStep> i = steps.iterator();
		while(i.hasNext())
			result = result + i.next().getXShift();
		return result;
	}
	public double getTotalYShift()
	{	double result = 0;
		Iterator<TrajectoryStep> i = steps.iterator();
		while(i.hasNext())
			result = result + i.next().getYShift();
		return result;
	}
	public double getTotalZShift(Sprite boundToSprite)
	{	double result = 0;
		Iterator<TrajectoryStep> i = steps.iterator();
		while(i.hasNext())
			result = result + i.next().getZShift(boundToSprite);
		return result;
	}

	public boolean getRepeat()
	{	return repeat;
	}
	public void setRepeat(boolean repeat)
	{	this.repeat = repeat;
	}

	public double getXInteraction()
	{	return xInteraction;
	}
	public void setXInteraction(double interaction)
	{	xInteraction = interaction;
	}

	public double getYInteraction()
	{	return yInteraction;
	}
	public void setYInteraction(double interaction)
	{	yInteraction = interaction;
	}

	public boolean isForceXPosition()
	{	return forceXPosition;
	}
	public void setForceXPosition(boolean forceXPosition)
	{	this.forceXPosition = forceXPosition;
	}

	public boolean isForceYPosition()
	{	return forceYPosition;
	}
	public void setForceYPosition(boolean forceYPosition)
	{	this.forceYPosition = forceYPosition;
	}

	public boolean isForceZPosition()
	{	return forceZPosition;
	}
	public void setForceZPosition(boolean forceZPosition)
	{	this.forceZPosition = forceZPosition;
	}

	public double getForcedXPosition()
	{	return forcedXPosition;
	}
	public void setForcedXPosition(double position)
	{	forcedXPosition = position;
	}

	public double getForcedYPosition()
	{	return forcedYPosition;
	}
	public void setForcedYPosition(double position)
	{	forcedYPosition = position;
	}

	public double getForcedZPosition()
	{	return forcedZPosition;
	}
	public void setForcedZPosition(double position)
	{	forcedZPosition = position;
	}

	public long getForcedPositionTime()
	{	return forcedPositionTime;
	}
	public void setForcedPositionTime(long forcedTime)
	{	this.forcedPositionTime = forcedTime;
	}

	public String getName()
	{	return gestureName+","+direction;
	}
	public void setGestureName(String gestureName)
	{	this.gestureName = gestureName;
	}
	public void setDirection(Direction direction)
	{	this.direction = direction;
	}
	public Direction getDirection()
	{	return direction;
	}
	
	public boolean getProportional()
	{	return proportional;
	}
	public void setProportional(boolean proportional)
	{	this.proportional = proportional;
	}

	public String toString()
	{	return getName();
	}	
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// steps
			{	Iterator<TrajectoryStep> it = steps.iterator();
				while(it.hasNext())
				{	TrajectoryStep temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// misc
			direction = null;
		}
	}
}