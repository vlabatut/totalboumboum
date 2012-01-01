package org.totalboumboum.engine.content.feature.gesture.trajectory.direction;

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

import org.totalboumboum.engine.content.feature.gesture.trajectory.step.HollowTrajectoryStep;
import org.totalboumboum.engine.content.feature.gesture.trajectory.step.TrajectoryStep;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowTrajectoryDirection extends AbstractTrajectoryDirection<HollowTrajectoryStep>
{	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used to clone an abstract HollowFactory to be completed
	 * by additional data (useless for now, might be usefull later) 
	 */
/*	public HollowTrajectoryDirection copy()
	{	HollowTrajectoryDirection result = new HollowTrajectoryDirection();
	
		for(HollowTrajectoryStep step: steps)
		{	HollowTrajectoryStep temp = step.copy();
			result.add(temp);
		}
		result.direction = direction;
		result.finished = finished;
		result.forcedPositionTime = forcedPositionTime;
		result.forcedXPosition = forcedXPosition;
		result.forcedYPosition = forcedYPosition;
		result.forcedZPosition = forcedZPosition;
		result.forceXPosition = forceXPosition;
		result.forceYPosition = forceYPosition;
		result.forceZPosition = forceZPosition;
		result.gestureName = gestureName;
		result.proportional = proportional;
		result.repeat = repeat;
		result.xInteraction = xInteraction;
		result.yInteraction = yInteraction;
		
		return result;
	}
*/
	/**
	 * used when generating an actual Factory from a HollowFactory
	 */
	public TrajectoryDirection fill(double zoomFactor)
	{	TrajectoryDirection result = new TrajectoryDirection();
		Iterator<HollowTrajectoryStep> it = steps.iterator();
		while(it.hasNext())
		{	TrajectoryStep temp = it.next().fill(zoomFactor);
			result.add(temp);
		}
		result.gestureName = gestureName;
		result.direction = direction;

		result.forcedPositionTime = forcedPositionTime;
		result.forcedXPosition = forcedXPosition*zoomFactor;
		result.forcedYPosition = forcedYPosition*zoomFactor;
		result.forcedZPosition = forcedZPosition*zoomFactor;
		
		result.forceXPosition = forceXPosition;
		result.forceYPosition = forceYPosition;
		result.forceZPosition = forceZPosition;
		
		result.proportional = proportional;
		
		result.repeat = repeat;
		
		result.xInteraction = xInteraction*zoomFactor;
		result.yInteraction = yInteraction*zoomFactor;
		
		return result;
	}
}
