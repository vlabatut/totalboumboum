package fr.free.totalboumboum.engine.content.feature.action;

import java.util.ArrayList;
import java.util.Arrays;

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

public abstract class GeneralAction<T extends SpecificAction<?>>
{	
	
	/* NOTE in tile position: 
	 * 	- FAR was changed into REMOTE, 
	 * 	- the meaning of same was changed (before no tile for actor -> same), 
	 * 	- undefined was added 
	 */

	/*NOTE
	 * - there's not always a direction (the actor can perform an undirected gesture)
	 * - there's always an actor
	 * - there may be no target
	 * - in that case orientation and tile position are irrelevant
	 */
	
	protected GeneralAction(ActionName name)
	{	this.name = name;
	}
	
	/*NOTE
	 * 
	 * il semble qu'on a parfois besoin d'une position sous forme de case.
	 * dans ce cas là, on peut passer la case (ou ses coordonnées) lors de la création de l'action,
	 * qui initialise ses champs en conséquence.
	 * 
	 * il semble logique de laisser la gestion complète des autorisations de transition aux modulations
	 * donc pas la peine de les coder en dur comme c'est actuellement fait dans les event managers
	 * 
	 * ability fait-elle partie de isPossible ? a priori, je dirais oui.
	 * donc: 	isPossible=compatibilité avec l'action générale + ability
	 * 			isAllowed=modulation
	 * en fait non, c'est trop lié aux modulations. il vaut mieux faire:
	 * 		isPossible=compatiblité avec l'action générale
	 * 		isAllowed=ability modulée
	 * 
	 */
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** name of this action */
	private ActionName name;

	protected ActionName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** directions of the action */
	private final ArrayList<Direction> directions = new ArrayList<Direction>();
	private final ArrayList<Direction> allowedDirections = new ArrayList<Direction>();

	protected ArrayList<Direction> getDirections()
	{	return directions;
	}
	
	public void addDirection(Direction direction) throws IncompatibleParameterException
	{	if(!allowedDirections.contains(direction))
			throw new IncompatibleParameterException("direction",direction.toString());
		else
		{	if(!getDirections().contains(direction))
				directions.add(direction);
		}
	}
	
	protected void setAllowedDirections(Direction directions[])
	{	allowedDirections.addAll(Arrays.asList(directions));
	}

	/////////////////////////////////////////////////////////////////
	// CONTACTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** contacts between the actor and the target */
	private final ArrayList<Contact> contacts = new ArrayList<Contact>();
	private final ArrayList<Contact> allowedContacts = new ArrayList<Contact>();

	protected ArrayList<Contact> getContacts()
	{	return contacts;
	}

	public void addContact(Contact contact) throws IncompatibleParameterException
	{	if(!allowedContacts.contains(contact))
			throw new IncompatibleParameterException("contact",contact.toString());
		else
		{	if(!getContacts().contains(contact))
				contacts.add(contact);
		}
	}

	protected void setAllowedContacts(Contact contacts[])
	{	allowedContacts.addAll(Arrays.asList(contacts));
	}

	/////////////////////////////////////////////////////////////////
	// ORIENTATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** compared directions of the target and the action */
	private final ArrayList<Orientation> orientations = new ArrayList<Orientation>();
	private final ArrayList<Orientation> allowedOrientations = new ArrayList<Orientation>();

	protected ArrayList<Orientation> getOrientations()
	{	return orientations;
	}
	
	public void addOrientation(Orientation orientation) throws IncompatibleParameterException
	{	if(!allowedOrientations.contains(orientation))
			throw new IncompatibleParameterException("orientation",orientation.toString());
		else
		{	if(!getOrientations().contains(orientation))
				orientations.add(orientation);
		}
	}
	
	protected void setAllowedOrientations(Orientation orientations[])
	{	allowedOrientations.addAll(Arrays.asList(orientations));
	}

	/////////////////////////////////////////////////////////////////
	// TILE POSITIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** positions of the target in termes of tiles */
	private final ArrayList<TilePosition> tilePositions =  new ArrayList<TilePosition>();
	private final ArrayList<TilePosition> allowedTilePositions =  new ArrayList<TilePosition>();
	
	protected ArrayList<TilePosition> getTilePositions()
	{	return tilePositions;
	}
	
	public void addTilePosition(TilePosition tilePosition) throws IncompatibleParameterException
	{	if(!allowedTilePositions.contains(tilePosition))
			throw new IncompatibleParameterException("tilePosition",tilePosition.toString());
		else
		{	if(!getTilePositions().contains(tilePosition))
				tilePositions.add(tilePosition);
		}
	}

	protected void setAllowedTilePositions(TilePosition tilePositions[])
	{	allowedTilePositions.addAll(Arrays.asList(tilePositions));
	}

	/////////////////////////////////////////////////////////////////
	// ACTORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** roles of the acting sprite */
	private final ArrayList<Role> actors = new ArrayList<Role>();
	private final ArrayList<Role> allowedActors = new ArrayList<Role>();
	
	protected ArrayList<Role> getActors()
	{	return actors;
	}
	
	public void addActor(Role actor) throws IncompatibleParameterException
	{	if(!allowedActors.contains(actor))
			throw new IncompatibleParameterException("actor",actor.toString());
		else
		{	if(!getActors().contains(actor))
				actors.add(actor);
		}
	}
	
	protected void setAllowedActors(Role actors[])
	{	allowedActors.addAll(Arrays.asList(actors));
	}

	/////////////////////////////////////////////////////////////////
	// TARGETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** role of the targeted sprite */
	private final ArrayList<Role> targets = new ArrayList<Role>();
	private final ArrayList<Role> allowedTargets = new ArrayList<Role>();
	
	protected ArrayList<Role> getTargets()
	{	return targets;
	}
	
	public void addTarget(Role target) throws IncompatibleParameterException
	{	if(!allowedTargets.contains(target))
			throw new IncompatibleParameterException("target",target.toString());
		else
		{	if(!getTargets().contains(target))
				targets.add(target);
		}
	}
	
	protected void setAllowedTargets(Role targets[])
	{	allowedTargets.addAll(Arrays.asList(targets));
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	protected void finish()
	{	if(!finished)
		{	finished = true;
			actors.clear();
			allowedActors.clear();
			contacts.clear();
			allowedContacts.clear();
			directions.clear();
			allowedDirections.clear();
			orientations.clear();
			allowedOrientations.clear();
			targets.clear();
			allowedTargets.clear();
			tilePositions.clear();
			allowedTilePositions.clear();
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
		}
		// targets
		{	if(targets.size()>0)
				result = result+">> ";			
			for(Role r: targets)
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
	public abstract GeneralAction<?> copy(GeneralAction<?> action);
	
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
