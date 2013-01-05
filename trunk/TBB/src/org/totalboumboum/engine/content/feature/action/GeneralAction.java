package org.totalboumboum.engine.content.feature.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.sprite.Sprite;


/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

/**
 * represents a general action by specifying who can do what to who.
 * The subclasses define what is actually possible in the game. 
 * Instances must by subsumed by the class, they fit stricter situations,
 * usually user-defined to be used in abilities and modulations.
 * SpecificActions represent in-game specific situations. 
 * 
 * @author Vincent Labatut
 *
 */
public abstract class GeneralAction implements Serializable
{	private static final long serialVersionUID = 1L;

	
	/*NOTE
	 * - there's not always a direction (the actor can perform an undirected gesture)
	 * - there's always an actor
	 * - there may be no target
	 * - in that case orientation and tile position are irrelevant
	 */
	
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
	public abstract ActionName getName();
	
	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** directions of the action */
	protected final List<Direction> directions = new ArrayList<Direction>();

	protected List<Direction> getDirections()
	{	return directions;
	}
	
	public abstract void addDirection(Direction direction);

	public void addAnyDirections()
	{	for(Direction d: Direction.values())
			addDirection(d);
	}

	/////////////////////////////////////////////////////////////////
	// CIRCUMSTANCES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final Circumstance circumstance = new Circumstance();

	protected Circumstance getCircumstance()
	{	return circumstance;	
	}
	
/*	protected ArrayList<Contact> getContacts()
	{	return circumstance.getContacts();
	}
*/
	public abstract void addContact(Contact contact);
	
	public void addAnyContacts()
	{	for(Contact c: Contact.values())
			addContact(c);
	}

	public boolean containsContact(Contact contact)
	{	return circumstance.containsContact(contact);
	}

/*	public boolean containsAllContacts(ArrayList<Contact> contacts)
	{	return circumstance.containsAllContacts(contacts);
	}
*/
/*	protected ArrayList<Orientation> getOrientations()
	{	return circumstance.getOrientations();
	}
*/	
	public abstract void addOrientation(Orientation orientation);
	
	public void addAnyOrientations()
	{	for(Orientation o: Orientation.values())
			addOrientation(o);
	}

	public boolean containsOrientation(Orientation orientation)
	{	return circumstance.containsOrientation(orientation);
	}

/*	public boolean containsAllOrientations(ArrayList<Orientation> orientations)
	{	return circumstance.containsAllOrientations(orientations);
	}
*/
/*	protected ArrayList<TilePosition> getTilePositions()
	{	return circumstance.getTilePositions();
	}
*/	
	public abstract void addTilePosition(TilePosition tilePosition);
	
	public void addAnyTilePositions()
	{	for(TilePosition tp: TilePosition.values())
			addTilePosition(tp);
	}
	public boolean containsTilePosition(TilePosition tilePosition)
	{	return circumstance.containsTilePosition(tilePosition);
	}

/*	public boolean containsAllTilePositions(ArrayList<TilePosition> tilePositions)
	{	return circumstance.containsAllTilePositions(tilePositions);
	}
*/

	/////////////////////////////////////////////////////////////////
	// ACTORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** roles of the acting sprite */
	protected final List<Role> actors = new ArrayList<Role>();
	
	protected List<Role> getActors()
	{	return actors;
	}
	
	public abstract void addActor(Role actor);
	
	public void addAnyActors()
	{	for(Role r: Role.values())
			addActor(r);
	}

	/////////////////////////////////////////////////////////////////
	// TARGETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** role of the targeted sprite */
	protected final List<Role> targets = new ArrayList<Role>();
	
	protected List<Role> getTargets()
	{	return targets;
	}
	
	public abstract void addTarget(Role target);

	public void addAnyTargets()
	{	for(Role r: Role.values())
			addTarget(r);
	}

	/////////////////////////////////////////////////////////////////
	// SPECIFIC ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * tests if this GeneralAction is more general than the specified SpecificAction
	 */
	public boolean subsume(SpecificAction action)
	{	boolean result = true;
		// name
		if(result)
			result = getName()==action.getName();
		// actor
		if(result)
		{	Sprite actor = action.getActor();
			Role actorRole = actor.getRole();
			result = actors.contains(actorRole);		
		}
		// target
		if(result)
		{	Sprite target = action.getTarget();
			Role targetRole = Role.NONE;
			if(target!=null)
				targetRole = target.getRole();
			result = targets.contains(targetRole);
		}
		// direction
		if(result)
		{	Direction d = action.getDirection();
			result = directions.contains(d);
		
		}
		// circumstances
		if(result)
			result = circumstance.subsume(action.getCircumstance());
/*		// contact
		if(result)
			result = containsContact(action.getContact());
		// orientation
		if(result)
			result = containsOrientation(action.getOrientation());
		// tile position
		if(result)
			result = containsTilePosition(action.getTilePosition());
*/		
		return result;
	}
	
	public boolean subsume(GeneralAction action)
	{	boolean result = true;
		// name
		if(result)
			result = getName()==action.getName();
		// actor
		if(result)
			result = actors.containsAll(action.getActors());
		// target
		if(result)
			result = targets.containsAll(action.getTargets());
		// direction
		if(result)
			result = directions.containsAll(action.getDirections());
		if(result)
			result = circumstance.subsume(action.getCircumstance());
/*		// contact
		if(result)
			result = containsAllContacts(action.getContacts());
		// orientation
		if(result)
			result = containsAllOrientations(action.getOrientations());
		// tile position
		if(result)
			result = containsAllTilePositions(action.getTilePositions());
*/		
		return result;
	}	
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = getName().toString();
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
			for(Contact c: circumstance.getContacts())
				result = result + c.toString() + " "; 
			result = result+"] ";
		}
		// orientations
		{	result = result+" [ ";
			for(Orientation o: circumstance.getOrientations())
				result = result + o.toString() + " "; 
			result = result+"] ";
		}
		// tile
		{	result = result+" [ ";
			for(TilePosition t: circumstance.getTilePositions())
				result = result + t.toString() + " "; 
			result = result+"] ";
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
//	public abstract GeneralAction<?> copy(GeneralAction<?> action);
/*
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
*/

	/////////////////////////////////////////////////////////////////
	// COPY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * this is actually not so usefull,
	 * because general actions are not modified after their creation 
	 */
	public abstract GeneralAction copy();
	
	protected void copy(GeneralAction result)
	{	// actors
		for(Role a: actors)
			result.actors.add(a);
		
		// targets
		for(Role t: targets)
			result.targets.add(t);
		
		// directions
		for(Direction d: directions)
			result.directions.add(d);
		
		// contacts
		for(Contact c: circumstance.getContacts())
			result.circumstance.addContact(c);
		
		// orientations
		for(Orientation o: circumstance.getOrientations())
			result.circumstance.addOrientation(o);
		
		// tile positions
		for(TilePosition tp: circumstance.getTilePositions())
			result.circumstance.addTilePosition(tp);
	}
}
