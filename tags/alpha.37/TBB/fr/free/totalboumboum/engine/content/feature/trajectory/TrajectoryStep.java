package fr.free.totalboumboum.engine.content.feature.trajectory;

import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.ImageShift;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class TrajectoryStep
{
	private double xShift;
	private double yShift;
	private double zShift;
	private long duration;
	private ImageShift boundZShift;
	
	public TrajectoryStep()
	{	xShift = 0;
		yShift = 0;
		zShift = 0;
		duration = 0;
		boundZShift = ImageShift.DOWN;
	}	
	
	public double getXShift()
	{	return xShift;
	}
	public void setXShift(double shift)
	{	xShift = shift;
	}

	public double getYShift()
	{	return yShift;
	}
	public void setYShift(double shift)
	{	yShift = shift;
	}

	public double getZShift(Sprite boundToSprite)
	{	double result = zShift;
		if(boundToSprite!=null)
			result = result + boundZShift.getValue(boundToSprite);
		return result;
	}
	public void setZShift(double shift)
	{	zShift = shift;
	}

	public long getDuration()
	{	return duration;
	}
	public void setDuration(long duration)
	{	this.duration = duration;
	}	
	
	public ImageShift getBoundZShift()
	{	return boundZShift;
	}
	public void setBoundZShift(ImageShift boundZShift)
	{	this.boundZShift = boundZShift;
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// misc
			boundZShift = null;
		}
	}
}
