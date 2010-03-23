package org.totalboumboum.engine.content.feature.gesture;

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

import java.io.IOException;
import java.io.Serializable;
import java.util.Map.Entry;

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.AnimeDirection;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.HollowAnimeDirection;
import org.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.OtherModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.SelfModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.ThirdModulation;
import org.totalboumboum.engine.content.feature.gesture.trajectory.direction.HollowTrajectoryDirection;
import org.totalboumboum.engine.content.feature.gesture.trajectory.direction.TrajectoryDirection;

public class HollowGesture extends AbstractGesture<HollowAnimeDirection,HollowTrajectoryDirection> implements Serializable
{	private static final long serialVersionUID = 1L;

	public HollowGesture()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// TRAJECTORIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected HollowTrajectoryDirection createTrajectoryDirection()
	{	return new HollowTrajectoryDirection();		
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * like surface copy, except images are copied, and possibly
	 * rescaled and recolored.
	 * Used to generate a new factory from a neutral, cached, one
	 * @throws IOException 
	 */
	public Gesture fill(double zoomFactor, double scale, PredefinedColor color) throws IOException
	{	Gesture result = new Gesture();
		double zoom = zoomFactor/scale;
	
		// name
		result.setName(name);
		
		// animes
		for(Entry<Direction,HollowAnimeDirection> e: animes.entrySet())
		{	AnimeDirection temp = e.getValue().fill(zoom,color);
			Direction direction = e.getKey();
			result.addAnimeDirection(temp,direction);
		}
		
		// trajectories
		for(Entry<Direction,HollowTrajectoryDirection> e: trajectories.entrySet())
		{	TrajectoryDirection temp = e.getValue().fill(zoomFactor);
			result.addTrajectoryDirection(temp);
		}
	
		// modulations
		for(SelfModulation e: selfModulations)
			result.addModulation(e.cacheCopy(zoomFactor));
		for(OtherModulation e: otherModulations)
			result.addModulation(e.cacheCopy(zoomFactor));
		for(ActorModulation e: actorModulations)
			result.addModulation(e.cacheCopy(zoomFactor));
		for(TargetModulation e: targetModulations)
			result.addModulation(e.cacheCopy(zoomFactor));
		for(ThirdModulation e: thirdModulations)
			result.addModulation(e.cacheCopy(zoomFactor));
		
		return result;
	}
	
}
