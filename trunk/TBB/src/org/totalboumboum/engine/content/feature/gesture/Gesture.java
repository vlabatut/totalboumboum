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

import org.totalboumboum.engine.content.feature.gesture.anime.direction.AnimeDirection;
import org.totalboumboum.engine.content.feature.gesture.trajectory.direction.TrajectoryDirection;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Gesture extends AbstractGesture<AnimeDirection,TrajectoryDirection>
{	
	/////////////////////////////////////////////////////////////////
	// TRAJECTORIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected TrajectoryDirection createTrajectoryDirection()
	{	return new TrajectoryDirection();		
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used when generating a sprite from a factory: the content is the
	 * same, only the containers are copied.
	 * useless for now, since nothing is modified in-game.
	 */
/*	public Gesture copy()
	{	Gesture result = new Gesture();
	
		// name
		result.setName(name);
		
		// animes
		for(Entry<Direction,AnimeDirection> e: animes.entrySet())
		{	AnimeDirection anime = e.getValue();
			Direction direction = e.getKey();
			result.addAnimeDirection(anime,direction);
		}
		
		// trajectories
		for(Entry<Direction,TrajectoryDirection> e: trajectories.entrySet())
		{	TrajectoryDirection temp = e.getValue();
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
		
		result.finished = finished;
		
		return result;
	}
*/	
}
