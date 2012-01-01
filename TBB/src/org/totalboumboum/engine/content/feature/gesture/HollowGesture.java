package org.totalboumboum.engine.content.feature.gesture;

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

import java.io.IOException;
import java.io.Serializable;
import java.util.Map.Entry;

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
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowGesture extends AbstractGesture<HollowAnimeDirection,HollowTrajectoryDirection> implements Serializable
{	private static final long serialVersionUID = 1L;

	public HollowGesture()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// ANIMATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setAnimes(HollowGesture gesture)
	{	for(Entry<Direction,HollowAnimeDirection> e: gesture.animes.entrySet())
		{	Direction direction = e.getKey();
			HollowAnimeDirection anime = e.getValue();
			addAnimeDirection(anime,direction);		
		}
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
	 * used to clone an abstract HollowFactory to be completed
	 * by additional data. All the content is keeped as is (same objects)
	 * but the containers are cloned, since their own content may be changed
	 * through inheritance.
	 */
	public HollowGesture copy()
	{	HollowGesture result = new HollowGesture();
	
		// name
		result.setName(name);
		
		// animes
		for(Entry<Direction,HollowAnimeDirection> e: animes.entrySet())
		{	HollowAnimeDirection anime = e.getValue();
			Direction direction = e.getKey();
			result.addAnimeDirection(anime,direction);
		}
		
		// trajectories
		for(Entry<Direction,HollowTrajectoryDirection> e: trajectories.entrySet())
		{	HollowTrajectoryDirection temp = e.getValue();
			result.addTrajectoryDirection(temp);
		}

		// modulations
		for(SelfModulation e: selfModulations)
			result.addModulation(e);
		for(OtherModulation e: otherModulations)
			result.addModulation(e);
		for(ActorModulation e: actorModulations)
			result.addModulation(e);
		for(TargetModulation e: targetModulations)
			result.addModulation(e);
		for(ThirdModulation e: thirdModulations)
			result.addModulation(e);
		
		return result;
	}

	/**
	 * used when generating an actual Factory from a HollowFactory.
	 * Images names are replaced by the actual images, scalable stuff
	 * is scaled, etc.
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
			result.addModulation(e/*.cacheCopy(zoomFactor)*/);
		for(OtherModulation e: otherModulations)
			result.addModulation(e/*.cacheCopy(zoomFactor)*/);
		for(ActorModulation e: actorModulations)
			result.addModulation(e/*.cacheCopy(zoomFactor)*/);
		for(TargetModulation e: targetModulations)
			result.addModulation(e/*.cacheCopy(zoomFactor)*/);
		for(ThirdModulation e: thirdModulations)
			result.addModulation(e/*.cacheCopy(zoomFactor)*/);
		
		return result;
	}
	
}
