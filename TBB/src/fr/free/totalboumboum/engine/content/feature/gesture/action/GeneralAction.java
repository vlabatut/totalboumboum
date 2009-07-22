package fr.free.totalboumboum.engine.content.feature.gesture.action;

import java.util.ArrayList;

import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.Role;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.feature.gesture.Circumstance;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

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

/**
 * represents a general action by specifying who can do what to who.
 * The subclasses define what is actually possible in the game. 
 * Instances must by subsumed by the class, they fit more strict situations,
 * usually user-defined to be used in abilities and modulations.
 * SpecificActions represent in-game specific situations. 
 */
public abstract class GeneralAction
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
	
	/*NOTE
	 * 
	 * il semble qu'on a parfois besoin d'une position sous forme de case.
	 * dans ce cas l�, on peut passer la case (ou ses coordonn�es) lors de la cr�ation de l'action,
	 * qui initialise ses champs en cons�quence.
	 * 
	 * il semble logique de laisser la gestion compl�te des autorisations de transition aux modulations
	 * donc pas la peine de les coder en dur comme c'est actuellement fait dans les event managers
	 * 
	 * ability fait-elle partie de isPossible ? a priori, je dirais oui.
	 * donc: 	isPossible=compatibilit� avec l'action g�n�rale + ability
	 * 			isAllowed=modulation
	 * en fait non, c'est trop li� aux modulations. il vaut mieux faire:
	 * 		isPossible=compatiblit� avec l'action g�n�rale
	 * 		isAllowed=ability modul�e
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
	protected final ArrayList<Direction> directions = new ArrayList<Direction>();

	protected ArrayList<Direction> getDirections()
	{	return directions;
	}
	
	public abstract void addDirection(Direction direction);

	/////////////////////////////////////////////////////////////////
	// CIRCUMSTANCES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final Circumstance circumstance = new Circumstance();

	protected Circumstance getCircumstance()
	{	return circumstance;	
	}
	
	protected ArrayList<Contact> getContacts()
	{	return circumstance.getContacts();
	}

	public abstract void addContact(Contact contact);
	
	public boolean containsContact(Contact contact)
	{	return circumstance.containsContact(contact);
	}

	public boolean containsAllContacts(ArrayList<Contact> contacts)
	{	return circumstance.containsAllContacts(contacts);
	}

	protected ArrayList<Orientation> getOrientations()
	{	return circumstance.getOrientations();
	}
	
	public abstract void addOrientation(Orientation orientation);
	
	public boolean containsOrientation(Orientation orientation)
	{	return circumstance.containsOrientation(orientation);
	}

	public boolean containsAllOrientations(ArrayList<Orientation> orientations)
	{	return circumstance.containsAllOrientations(orientations);
	}

	protected ArrayList<TilePosition> getTilePositions()
	{	return circumstance.getTilePositions();
	}
	
	public abstract void addTilePosition(TilePosition tilePosition);
	
	public boolean containsTilePosition(TilePosition tilePosition)
	{	return circumstance.containsTilePosition(tilePosition);
	}

	public boolean containsAllTilePositions(ArrayList<TilePosition> tilePositions)
	{	return circumstance.containsAllTilePositions(tilePositions);
	}

	/////////////////////////////////////////////////////////////////
	// ACTORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** roles of the acting sprite */
	protected final ArrayList<Role> actors = new ArrayList<Role>();
	
	protected ArrayList<Role> getActors()
	{	return actors;
	}
	
	public abstract void addActor(Role actor);

	/////////////////////////////////////////////////////////////////
	// TARGETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** role of the targeted sprite */
	protected final ArrayList<Role> targets = new ArrayList<Role>();
	
	protected ArrayList<Role> getTargets()
	{	return targets;
	}
	
	public abstract void addTarget(Role target);

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			actors.clear();
			directions.clear();
			targets.clear();
			circumstance.finish();
		}
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
			result = directions.contains(action.getDirection());
		// contact
		if(result)
			result = containsContact(action.getContact());
		// orientation
		if(result)
			result = containsOrientation(action.getOrientation());
		// tile position
		if(result)
			result = containsTilePosition(action.getTilePosition());
		
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
		// contact
		if(result)
			result = containsAllContacts(action.getContacts());
		// orientation
		if(result)
			result = containsAllOrientations(action.getOrientations());
		// tile position
		if(result)
			result = containsAllTilePositions(action.getTilePositions());
		
		return result;
	}	
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = "";
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
			for(Contact c: getContacts())
				result = result + c.toString() + " "; 
			result = result+"] ";
		}
		// orientations
		{	result = result+" [ ";
			for(Orientation o: getOrientations())
				result = result + o.toString() + " "; 
			result = result+"] ";
		}
		// tile
		{	result = result+" [ ";
			for(TilePosition t: getTilePositions())
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
}
