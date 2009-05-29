package fr.free.totalboumboum.engine.content.feature.action;

import java.util.ArrayList;

import fr.free.totalboumboum.engine.content.feature.Direction;

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

public abstract class GeneralAction<T extends SpecificAction>
{	
	
	//TODO FAR was changed into REMOTE
	
//TODO to be removed?	to be replaced by "any" and "nany"
	public static final String ROLE_ALL = "all";
	public static final String ROLE_NONE = "none";
	public static final String DIRECTION_ALL = "all";
	public static final String CONTACT_ALL = "all";
	public static final String ORIENTATION_ALL = "all";
	public static final String TILE_POSITION_ALL = "all";

	/*NOTE
	 * - there's always a direction (the actor is always facing a direction)
	 * - there's always an actor
	 * - there may be no target
	 * - in that case orientation and tile position are irrelevant
	 */
	
	public GeneralAction(ActionName name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** name of this action */
	protected ActionName name;

	public ActionName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** directions of the action */
	private final ArrayList<Direction> directions = new ArrayList<Direction>();

	protected ArrayList<Direction> getDirections()
	{	return directions;
	}
	
	protected void addDirection(Direction direction)
	{	directions.add(direction);
	}

	/////////////////////////////////////////////////////////////////
	// CONTACTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** contacts between the actor and the target */
	private final ArrayList<Contact> contacts = new ArrayList<Contact>();

	protected ArrayList<Contact> getContacts()
	{	return contacts;
	}

	protected void addContact(Contact contact)
	{	contacts.add(contact);
	}

	/////////////////////////////////////////////////////////////////
	// ORIENTATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** compared directions of the target and the action */
	private final ArrayList<Orientation> orientations = new ArrayList<Orientation>();

	protected ArrayList<Orientation> getOrientations()
	{	return orientations;
	}
	
	protected void addOrientation(Orientation orientation)
	{	orientations.add(orientation);
	}

	/////////////////////////////////////////////////////////////////
	// TILE POSITIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** positions of the target in termes of tiles */
	private final ArrayList<TilePosition> tilePositions =  new ArrayList<TilePosition>();
	
	protected ArrayList<TilePosition> getTilePosition()
	{	return tilePositions;
	}
	
	protected void addTilePosition(TilePosition tilePosition)
	{	tilePositions.add(tilePosition);
	}

	/////////////////////////////////////////////////////////////////
	// ACTORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** roles of the acting sprite */
	private final ArrayList<Role> actors = new ArrayList<Role>();
	
	protected ArrayList<Role> getActors()
	{	return actors;
	}
	
	protected void addActor(Role actor)
	{	actors.add(actor);
	}
/*	
	protected void setActor(Role actor)
	{	actors.clear();
		actors.add(actor);
	}
*/
	
	/////////////////////////////////////////////////////////////////
	// TARGETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** role of the targeted sprite */
	private final ArrayList<Role> targets = new ArrayList<Role>();
	
	protected ArrayList<Role> getTargets()
	{	return targets;
	}
	
	protected void addTarget(Role target)
	{	targets.add(target);
	}
/*	
	protected void setTarget(Role actor)
	{	targets.clear();
		targets.add(actor);
	}
*/
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			actors.clear();
			contacts.clear();
			directions.clear();
			orientations.clear();
			targets.clear();
			tilePositions.clear();
		}
	}

	/////////////////////////////////////////////////////////////////
	// SPECIFIC ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * tests if this GeneralAction is more general than the specified SpecificAction
	 */
	public boolean subsume(T action)
	{	boolean result;
		// name
		result = name == action.getName();
		// actor
		if(result)
			result = actors.contains(action.getActor());
		// target
		if(result)
			result = targets.contains(action.getTarget());
		// direction
		if(result)
			result = directions.contains(action.getDirection());
		// contact
		if(result)
			result = contacts.contains(action.getContact());
		// orientation
		if(result)
			result = orientations.contains(action.getOrientation());
		// tile position
		if(result)
			result = tilePositions.contains(action.getTilePosition());
/*	
		else //if(a instanceof GeneralAction)
		{	GeneralAction action = (GeneralAction)a;
			// name
			result = name.equalsIgnoreCase(action.getName());
			// actor
			if(result)
				result = actors.containsAll(action.getActors());
			// target
			if(result)
				result = targets.containsAll(action.getTargets());
			// direction
			if(result)
				result = directions.containsAll(action.getDirections());
			// contact
			if(result)
				result = contacts.containsAll(action.getContacts());
			// orientation
			if(result)
				result = orientations.containsAll(action.getOrientations());
			// tile position
			if(result)
				result = tilePositions.containsAll(action.getTilePositions());
		}
*/		
		return result;
	}	

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = name.toString();
		// actors
		{	result = result+" ( ";
			for(Role r: actors)
				result = result + r.toString() + " "; 
			result = result+">> ";			
		}
		// targets
		{	for(Role r: targets)
				result = result + r.toString() + " "; 
			result = result+") ";			
		}
		// directions
		{	result = result+" [ ";
			for(Direction d: directions)
				result = result + d.toString() + " "; 
			result = result+"] ";
		}
		// contacts
		{	result = result+" [ ";
			for(Contact c: contacts)
				result = result + c.toString() + " "; 
			result = result+"] ";
		}
		// orientations
		{	result = result+" [ ";
			for(Orientation o: orientations)
				result = result + o.toString() + " "; 
			result = result+"] ";
		}
		// tile
		{	result = result+" [ ";
			for(TilePosition t: tilePositions)
				result = result + t.toString() + " "; 
			result = result+"] ";
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract T copy(T action);
	
	protected void copy(GeneralAction<?> or, GeneralAction<?> cp)
	{	// actors
		{	for(Role a: actors)
				cp.addActor(a);
		}
		// contacts
		{	for(Contact c: contacts)
				cp.addContact(c);
		}
		// orientations
		{	for(Orientation o: orientations)
			cp.addOrientation(o);
		}
		// targets
		{	for(Role t: targets)
				cp.addActor(t);
		}
		// directions
		{	for(Direction d: directions)
				cp.addDirection(d);
		}
		// tile positions
		{	for(TilePosition tp: tilePositions)
				cp.addTilePosition(tp);
		}
		cp.finished = finished;
	}
}
