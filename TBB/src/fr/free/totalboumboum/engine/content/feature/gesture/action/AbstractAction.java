package fr.free.totalboumboum.engine.content.feature.gesture.action;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.appear.GeneralAction0;

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

public abstract class AbstractAction
{	
//TODO to be removed?	
	public static final String ROLE_ALL = "all";
	public static final String ROLE_NONE = "none";
	public static final String DIRECTION_ALL = "all";
	public static final String CONTACT_ALL = "all";
	public static final String ORIENTATION_ALL = "all";
	public static final String TILE_POSITION_ALL = "all";
	
	public AbstractAction(ActionName name)
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
	/** positions of the target in termes of tile */
	private final ArrayList<TilePosition> tilePositions =  new ArrayList<TilePosition>();
	
	protected ArrayList<TilePosition> getTilePositions()
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
}
