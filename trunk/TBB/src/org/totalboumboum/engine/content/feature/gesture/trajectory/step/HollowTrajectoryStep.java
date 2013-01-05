package org.totalboumboum.engine.content.feature.gesture.trajectory.step;

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

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowTrajectoryStep extends AbstractTrajectoryStep
{	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used to clone an abstract HollowFactory to be completed
	 * by additional data (useless for now, might be usefull later) 
	 */
/*	public HollowTrajectoryStep copy()
	{	HollowTrajectoryStep result = new HollowTrajectoryStep();
	
		result.boundZShift = boundZShift;
		result.duration = duration;
		result.finished = finished;
		result.xShift = xShift;
		result.yShift = yShift;
		result.zShift = zShift;
		
		return result;
	}
*/	
	/**
	 * used when generating an actual Factory from a HollowFactory
	 */
	public TrajectoryStep fill(double zoomFactor)
	{	TrajectoryStep result = new TrajectoryStep();
		
		// location shifts
		result.xShift = xShift*zoomFactor;
		result.yShift = yShift*zoomFactor;
		result.zShift = zShift*zoomFactor;

		result.boundZShift = boundZShift;
		result.duration = duration;

		return result;
	}
}
