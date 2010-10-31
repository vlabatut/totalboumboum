package org.totalboumboum.engine.content.manager.trajectory;

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

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class EmptyTrajectoryManager extends TrajectoryManager
{
	public EmptyTrajectoryManager(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// BINDING				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void setBoundToSprite(Sprite newSprite)
	{	// nothing to do here: the sprite can't be bound
	}

	/////////////////////////////////////////////////////////////////
	// UPDATE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void update()
	{	// nothing to do here, since there's no trajectory
	}

	/////////////////////////////////////////////////////////////////
	// GESTURE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateGesture(Gesture gesture, Direction spriteDirection, Direction controlDirection, boolean reinit, double forcedDuration)
	{	// nothing to do here, since there's no trajectory
	}	

	/////////////////////////////////////////////////////////////////
	// SPEED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getCurrentSpeed()
	{	return 0;
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public TrajectoryManager copy(Sprite sprite)
	{	TrajectoryManager result = new EmptyTrajectoryManager(sprite);
		return result;
	}
}
