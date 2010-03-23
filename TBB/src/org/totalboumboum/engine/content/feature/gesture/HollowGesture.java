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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.AnimeDirection;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.HollowAnimeDirection;
import org.totalboumboum.engine.content.feature.gesture.modulation.AbstractModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.OtherModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.SelfModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.ThirdModulation;
import org.totalboumboum.engine.content.feature.gesture.trajectory.direction.TrajectoryDirection;
import org.totalboumboum.engine.content.sprite.Sprite;

public class HollowGesture extends AbstractGesture implements Serializable
{	private static final long serialVersionUID = 1L;

	public HollowGesture()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// ANIMATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<Direction,HollowAnimeDirection> animes = new HashMap<Direction, HollowAnimeDirection>();
	
	public HollowAnimeDirection getAnimeDirection(Direction direction)
	{	HollowAnimeDirection result = animes.get(direction);
		return result;
	}
	
	public void addAnimeDirection(HollowAnimeDirection anime, Direction direction)
	{	animes.put(direction,anime);
	}
	
	public boolean hasNoAnimes()
	{	return animes.isEmpty();		
	}
/*	
	public void setAnimes(HollowGesture gesture)
	{	for(Entry<Direction,AnimeDirection> e: gesture.animes.entrySet())
		{	Direction direction = e.getKey();
			AnimeDirection anime = e.getValue();
			addAnimeDirection(anime,direction);		
		}
	}
*/	
/*	public void copyAnimesFrom(Gesture gesture)
	{	for(Entry<Direction,AnimeDirection> e: gesture.animes.entrySet())
		{	AnimeDirection cp = e.getValue();
			Direction direction = e.getKey();
			//AnimeDirection anime = cp.copy();
			AnimeDirection anime = cp;
			gesture.addAnimeDirection(anime,direction);
		}		
	}
*/
	/////////////////////////////////////////////////////////////////
	// TRAJECTORIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<Direction,TrajectoryDirection> trajectories = new HashMap<Direction, TrajectoryDirection>();

	public TrajectoryDirection getTrajectoryDirection(Direction direction)
	{	TrajectoryDirection result = trajectories.get(direction);
		if(result==null)
		{	result = new TrajectoryDirection();
			result.setDirection(direction);
			result.setGestureName(name);
			trajectories.put(direction,result);
		}
		return result;
	}
	
	public void addTrajectoryDirection(TrajectoryDirection trajectory)
	{	trajectories.put(trajectory.getDirection(),trajectory);
	}
	
	/////////////////////////////////////////////////////////////////
	// MODULATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<SelfModulation> selfModulations = new ArrayList<SelfModulation>();
	private final ArrayList<OtherModulation> otherModulations = new ArrayList<OtherModulation>();
	private final ArrayList<ActorModulation> actorModulations = new ArrayList<ActorModulation>();
	private final ArrayList<TargetModulation> targetModulations = new ArrayList<TargetModulation>();
	private final ArrayList<ThirdModulation> thirdModulations = new ArrayList<ThirdModulation>();

	public void addModulation(AbstractModulation modulation)
	{	if(modulation instanceof SelfModulation)
			selfModulations.add((SelfModulation)modulation);
		if(modulation instanceof OtherModulation)
			otherModulations.add((OtherModulation)modulation);
		else if(modulation instanceof ActorModulation)
			actorModulations.add((ActorModulation)modulation);
		else if(modulation instanceof TargetModulation)
			targetModulations.add((TargetModulation)modulation);
		else if(modulation instanceof ThirdModulation)
			thirdModulations.add((ThirdModulation)modulation);
//		modulation.setGestureName(name);
	}
	
	public SelfModulation getSelfModulation(String name)
	{	SelfModulation result = null;
		Iterator<SelfModulation> i = selfModulations.iterator();
		while(i.hasNext() && result==null)
		{	SelfModulation modulation = i.next();
			if(modulation.getName().equals(name))
				result = modulation;
		}
		return result;
	}
/*	
	public ArrayList<StateModulation> getStateModulations()
	{	return stateModulations;	
	}
*/

	public OtherModulation getOtherModulation(String name, Sprite modulator, Sprite modulated)
	{	OtherModulation result = null;
		Iterator<OtherModulation> i = otherModulations.iterator();
		while(i.hasNext() && result==null)
		{	OtherModulation modulation = i.next();
			if(modulation.getName().equals(name) && modulation.isConcerningSituation(modulator,modulated))
				result = modulation;
		}
		return result;
	}

	public ActorModulation getActorModulation(SpecificAction action)
	{	ActorModulation result = null;
		Iterator<ActorModulation> i = actorModulations.iterator();
		while(i.hasNext() && result==null)
		{	ActorModulation modulation = i.next();
			if(modulation.isConcerningAction(action))
				result = modulation;
		}
		return result;
	}
	
	public TargetModulation getTargetModulation(SpecificAction action)
	{	TargetModulation result = null;
		Iterator<TargetModulation> i = targetModulations.iterator();
		while(i.hasNext() && result==null)
		{	TargetModulation modulation = i.next();
			if(modulation.isConcerningAction(action))
				result = modulation;
		}
		return result;
	}
	
	public ThirdModulation getThirdModulation(SpecificAction action, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	ThirdModulation result = null;
		Iterator<ThirdModulation> i = thirdModulations.iterator();
		while(i.hasNext() && result==null)
		{	ThirdModulation modulation = i.next();
			if(modulation.isConcerningAction(action,actorCircumstances,targetCircumstances))
				result = modulation;
		}
		return result;
	}
	public ThirdModulation getThirdModulation(GeneralAction action, ArrayList<AbstractAbility> actorProperties, ArrayList<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	ThirdModulation result = null;
		Iterator<ThirdModulation> i = thirdModulations.iterator();
		while(i.hasNext() && result==null)
		{	ThirdModulation modulation = i.next();
			if(modulation.isConcerningAction(action,actorProperties,targetProperties,actorCircumstances,targetCircumstances))
				result = modulation;
		}
		return result;
	}
	
/*	public void completeModulations(Role role)
	{	// actor modulations
		for(ActorModulation m: actorModulations)
			m.getAction().addActor(role);
		// target modulations
		for(TargetModulation m: targetModulations)
			m.getAction().addTarget(role);
	}
*/	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns a copy whose trajectories are the same and animes
	 * are hollow copies (images not copied, just the key to retrieve them in the cache)
	 * Used to generate a factory inheriting from an existing one
	 */
	public HollowGesture surfaceCopy()
	{	HollowGesture result = new HollowGesture();
	
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
	
	/**
	 * like surface copy, except images are copied, and possibly
	 * rescaled and recolored.
	 * Used to generate a new factory from a neutral, cached, one
	 * @throws IOException 
	 */
	public HollowGesture deepCopy(double zoomFactor, double scale, PredefinedColor color) throws IOException
	{	HollowGesture result = new HollowGesture();
		double zoom = zoomFactor/scale;
	
		// name
		result.setName(name);
		
		// animes
		for(Entry<Direction,AnimeDirection> e: animes.entrySet())
		{	AnimeDirection temp = e.getValue().deepCopy(zoom,color);
			Direction direction = e.getKey();
			result.addAnimeDirection(temp,direction);
		}
		
		// trajectories
		for(Entry<Direction,TrajectoryDirection> e: trajectories.entrySet())
		{	TrajectoryDirection temp = e.getValue().deepCopy(zoomFactor);
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
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			
			// animes
			for(Entry<Direction,AnimeDirection> e: animes.entrySet())
			{	AnimeDirection temp = e.getValue();
				temp.finish();
			}
			animes.clear();
						
			// trajectories
			for(Entry<Direction,TrajectoryDirection> e: trajectories.entrySet())
			{	TrajectoryDirection temp = e.getValue();
				temp.finish();
			}
			trajectories.clear();
			
			// modulations
			for(SelfModulation e: selfModulations)
				e.finish();			
			for(OtherModulation e: otherModulations)
				e.finish();			
			for(ActorModulation e: actorModulations)
				e.finish();			
			for(TargetModulation e: targetModulations)
				e.finish();			
			for(ThirdModulation e: thirdModulations)
				e.finish();			
			selfModulations.clear();
			otherModulations.clear();
			actorModulations.clear();
			targetModulations.clear();
			thirdModulations.clear();
		}
	}
}
