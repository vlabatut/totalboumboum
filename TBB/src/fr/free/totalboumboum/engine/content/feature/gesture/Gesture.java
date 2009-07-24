package fr.free.totalboumboum.engine.content.feature.gesture;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.action.Circumstance;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.gesture.anime.AnimeDirection;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.AbstractModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.OtherModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.SelfModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ThirdModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoryDirection;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class Gesture
{	
	public Gesture()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GestureName name;
	
	public GestureName getName()
	{	return name;
	}
	
	public void setName(GestureName name)
	{	this.name = name;
	}

	/////////////////////////////////////////////////////////////////
	// ANIMATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<Direction,AnimeDirection> animes = new HashMap<Direction, AnimeDirection>();
	
	public AnimeDirection getAnimeDirection(Direction direction)
	{	AnimeDirection result = animes.get(direction);
		return result;
	}
	
	public void addAnimeDirection(AnimeDirection anime, Direction direction)
	{	animes.put(direction,anime);
	}
	
	public boolean hasNoAnimes()
	{	return animes.isEmpty();		
	}
	
	public void setAnimes(Gesture gesture)
	{	for(Entry<Direction,AnimeDirection> e: gesture.animes.entrySet())
		{	Direction direction = e.getKey();
			AnimeDirection anime = e.getValue();
			addAnimeDirection(anime,direction);		
		}
	}
	
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
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns a hollow copy, excepted for the animes: no copy at all. 
	 */
	public Gesture copy()
	{	Gesture result = new Gesture();
	
		// name
		result.setName(name);
		
		// animes
		for(Entry<Direction,AnimeDirection> e: animes.entrySet())
		{	//AnimeDirection temp = e.getValue().copy(images,copyImages);
			AnimeDirection anime = e.getValue();
			Direction direction = e.getKey();
			result.addAnimeDirection(anime,direction);
		}
		
		// trajectories
		for(Entry<Direction,TrajectoryDirection> e: trajectories.entrySet())
		{	//TrajectoryDirection temp = e.getValue().copy();
			TrajectoryDirection temp = e.getValue();
			result.addTrajectoryDirection(temp);
		}
		
		// self permissions
		for(SelfModulation e: selfModulations)
		{	//SelfModulation temp = e.copy();
			SelfModulation temp = e;
			result.addModulation(temp);
		}
		// other permissions
		for(OtherModulation e: otherModulations)
		{	//OtherModulation temp = e.copy();
			OtherModulation temp = e;
			result.addModulation(temp);
		}
		
		// actor permissions
		for(ActorModulation e: actorModulations)
		{	//ActorModulation temp = e.copy();
			ActorModulation temp = e;
			result.addModulation(temp);
		}
		// target permissions
		for(TargetModulation e: targetModulations)
		{	//TargetModulation temp = e.copy();
			TargetModulation temp = e;
			result.addModulation(temp);
		}
		// third permissions
		for(ThirdModulation e: thirdModulations)
		{	//ThirdModulation temp = e.copy();
			ThirdModulation temp = e;
			result.addModulation(temp);
		}
		
		result.finished = finished;
		return result;
	}
	
	public void copyAnimesFrom(Gesture gesture)
	{	for(Entry<Direction,AnimeDirection> e: animes.entrySet())
		{	AnimeDirection anime = e.getValue();
			Direction direction = e.getKey();
			AnimeDirection anime0 = anime/*.copy()*/;
			gesture.addAnimeDirection(anime0,direction);
		}		
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
			
			// state permissions
			for(SelfModulation e: selfModulations)
				e.finish();			
			selfModulations.clear();
			// other permissions
			for(OtherModulation e: otherModulations)
				e.finish();			
			otherModulations.clear();
			// actor permissions
			for(ActorModulation e: actorModulations)
				e.finish();			
			actorModulations.clear();
			// target permissions
			for(TargetModulation e: targetModulations)
				e.finish();			
			targetModulations.clear();
			// third permissions
			for(ThirdModulation e: thirdModulations)
				e.finish();			
			thirdModulations.clear();
		}
	}
}
