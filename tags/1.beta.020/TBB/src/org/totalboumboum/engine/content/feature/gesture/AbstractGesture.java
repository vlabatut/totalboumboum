package org.totalboumboum.engine.content.feature.gesture;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.List;

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.AbstractAnimeDirection;
import org.totalboumboum.engine.content.feature.gesture.modulation.AbstractModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.OtherModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.SelfModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.ThirdModulation;
import org.totalboumboum.engine.content.feature.gesture.trajectory.direction.AbstractTrajectoryDirection;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractGesture<T extends AbstractAnimeDirection<?>, U extends AbstractTrajectoryDirection<?>>
{	private static final long serialVersionUID = 1L;

	public AbstractGesture()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GestureName name;
	
	public GestureName getName()
	{	return name;
	}
	
	public void setName(GestureName name)
	{	this.name = name;
	}

	/////////////////////////////////////////////////////////////////
	// ANIMATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final HashMap<Direction,T> animes = new HashMap<Direction, T>();
	
	public T getAnimeDirection(Direction direction)
	{	T result = animes.get(direction);
		return result;
	}
	
	public void addAnimeDirection(T anime, Direction direction)
	{	animes.put(direction,anime);
	}
	
	public boolean hasNoAnimes()
	{	return animes.isEmpty();		
	}
/*	
	public void setAnimes(AbstractGesture<T,U> gesture)
	{	for(Entry<Direction,T> e: gesture.animes.entrySet())
		{	Direction direction = e.getKey();
			T anime = e.getValue();
			addAnimeDirection(anime,direction);		
		}
	}
*/	
/*
	public void copyAnimesFrom(AbstractGesture<T,U> gesture)
	{	for(Entry<Direction,T> e: gesture.animes.entrySet())
		{	T cp = e.getValue();
			Direction direction = e.getKey();
			//T anime = cp.copy();
			T anime = cp;
			gesture.addAnimeDirection(anime,direction);
		}		
	}
*/
	/////////////////////////////////////////////////////////////////
	// TRAJECTORIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final HashMap<Direction,U> trajectories = new HashMap<Direction, U>();

	protected abstract U createTrajectoryDirection();
	
	public U getTrajectoryDirection(Direction direction)
	{	U result = trajectories.get(direction);
		if(result==null)
		{	result = createTrajectoryDirection();
			result.setDirection(direction);
			result.setGestureName(name);
			trajectories.put(direction,result);
		}
		return result;
	}
	
	public void addTrajectoryDirection(U trajectory)
	{	trajectories.put(trajectory.getDirection(),trajectory);
	}
	
	/////////////////////////////////////////////////////////////////
	// MODULATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final List<SelfModulation> selfModulations = new ArrayList<SelfModulation>();
	protected final List<OtherModulation> otherModulations = new ArrayList<OtherModulation>();
	protected final List<ActorModulation> actorModulations = new ArrayList<ActorModulation>();
	protected final List<TargetModulation> targetModulations = new ArrayList<TargetModulation>();
	protected final List<ThirdModulation> thirdModulations = new ArrayList<ThirdModulation>();

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
	public ThirdModulation getThirdModulation(GeneralAction action, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances)
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
}
