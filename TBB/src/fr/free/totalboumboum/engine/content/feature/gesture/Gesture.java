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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.ActionName;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.gesture.anime.AnimeDirection;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.AbstractModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.StateModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ThirdModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.trajectory.TrajectoryDirection;

public class Gesture
{	
	public Gesture()
	{	
	}

	//TODO faut d�finir une liste de gestures pour chaque type de sprite...
	// et peut �tre charger/construire pour chaque TYPE de sprite cette liste de gestures
	// ensuite chaque sprite g�n�r� pour un type donn� partage ces donn�es avec ces coll�gues
	// seuls certains trucs peuvent �tre modifi�s comme les abilities, le reste est statique
	// sauf que non: les animes sont diff�rentes, elles, et pas que par la couleur (ex: diff�rents murs)
	// peut-�tre mettre une fonction statique dans la classe principale de chaque type de sprite, qui construit statiquement un gesture pack et utilise une copie vide comme base pour chaque sprite cr�� dans ce type?
	
	/*
	 * TODO la gestion des donn�es manquantes doit �tre effectu�e au chargement, et pas en cours de jeu	
	 */

	/////////////////////////////////////////////////////////////////
	// NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GestureName name;
	
	public GestureName getName()
	{	return name;
	}
	
	private void setName(GestureName name)
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
	
	public void addAnimeDirection(AnimeDirection anime)
	{	animes.put(anime.getDirection(),anime);
	}
	
	/////////////////////////////////////////////////////////////////
	// TRAJECTORIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<Direction,TrajectoryDirection> trajectories = new HashMap<Direction, TrajectoryDirection>();

	public TrajectoryDirection getTrajectoryDirection(Direction direction)
	{	TrajectoryDirection result = trajectories.get(direction);
		return result;
	}
	
	public void addTrajectoryDirection(TrajectoryDirection trajectory)
	{	trajectories.put(trajectory.getDirection(),trajectory);
	}
	
	/////////////////////////////////////////////////////////////////
	// MODULATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<StateModulation> stateModulations = new ArrayList<StateModulation>();
	private final ArrayList<ActorModulation> actorModulations = new ArrayList<ActorModulation>();
	private final ArrayList<TargetModulation> targetModulations = new ArrayList<TargetModulation>();
	private final ArrayList<ThirdModulation> thirdModulations = new ArrayList<ThirdModulation>();

	public void addModulation(AbstractModulation modulation)
	{	if(modulation instanceof StateModulation)
			stateModulations.add((StateModulation)modulation);
		else if(modulation instanceof ActorModulation)
			actorModulations.add((ActorModulation)modulation);
		else if(modulation instanceof TargetModulation)
			targetModulations.add((TargetModulation)modulation);
		else if(modulation instanceof ThirdModulation)
			thirdModulations.add((ThirdModulation)modulation);
//		modulation.setGestureName(name);
	}
	
	public StateModulation getModulation(StateModulation modulation)
	{	StateModulation result = null;
		int index = stateModulations.indexOf(modulation);
		if(index>=0)
			result = stateModulations.get(index);
		return result;
	}

	public ActorModulation getActorModulation(SpecificAction action)
	{	ActorModulation result = null;
		Iterator<ActorModulation> i = actorModulations.iterator();
		while(i.hasNext() && result==null)
		{	ActorModulation perm = i.next();
			if(perm.isAllowingAction(action)) //NOTE moduler directement ? ou renommer en subsume?
				result = perm;
		}
		return result;
	}
	
	public TargetModulation getTargetPermission(SpecificAction action)
	{	TargetModulation result = null;
		Iterator<TargetModulation> i = targetModulations.iterator();
		while(i.hasNext() && result==null)
		{	TargetModulation perm = i.next();
			if(perm.isAllowingAction(action))
				result = perm;
		}
		return result;
	}
	
	public ThirdModulation getThirdPermission(SpecificAction action)
	{	ThirdModulation result = null;
		Iterator<ThirdModulation> i = thirdModulations.iterator();
		while(i.hasNext() && result==null)
		{	ThirdModulation perm = i.next();
			if(perm.isAllowingAction(action))
				result = perm;
		}
		return result;
	}
	
	
	/////////////////////////////////////////////////////////////////
	// ACTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<ActionName> actions = new ArrayList<ActionName>();
	
	public void addAction(ActionName action)
	{	if(!actions.contains(action))
			actions.add(action);		
	}
	
	public boolean isAllowedAction(ActionName action)
	{	boolean result = actions.contains(action);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns a holllow copy, excepted for the animes: no copy at all. 
	 */
	public Gesture copy(ArrayList<BufferedImage> images, ArrayList<BufferedImage> copyImages)
	{	Gesture result = new Gesture();
	
		// name
		result.setName(name);
		
		// animes
//		for(Entry<Direction,AnimeDirection> e: animes.entrySet())
//		{	AnimeDirection temp = e.getValue().copy(images,copyImages);
//			result.addAnimeDirection(temp);
//		}
		
		// trajectories
		for(Entry<Direction,TrajectoryDirection> e: trajectories.entrySet())
		{	//TrajectoryDirection temp = e.getValue().copy();
			TrajectoryDirection temp = e.getValue();
			result.addTrajectoryDirection(temp);
		}
		
		// actor permissions
		for(ActorModulation e: actorModulations)
		{	//ActorModulation temp = e.copy();
			ActorModulation temp = e;
			result.addModulation(temp);
		}
		// state permissions
		for(StateModulation e: stateModulations)
		{	//StateModulation temp = e.copy();
			StateModulation temp = e;
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
			
			// actor permissions
			for(ActorModulation e: actorModulations)
				e.finish();			
			actorModulations.clear();
			// state permissions
			for(StateModulation e: stateModulations)
				e.finish();			
			stateModulations.clear();
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
