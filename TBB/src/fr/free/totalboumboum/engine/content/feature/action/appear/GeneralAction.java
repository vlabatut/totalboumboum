package fr.free.totalboumboum.engine.content.feature.action.appear;

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
import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.Contact;
import fr.free.totalboumboum.engine.content.feature.action.Orientation;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.TilePosition;


public class GeneralAction extends AbstractAction
{
	public String toString()
	{	String result = name;
		// actors
		{	result = result+" ( ";
			Iterator<Class<?>> i = actors.iterator();
			while(i.hasNext())
			{	Class<?> a = i.next();
				result = result + a + " "; 
			}
			result = result+">> ";			
		}
		// targets
		{	Iterator<Class<?>> i = targets.iterator();
			while(i.hasNext())
			{	Class<?> a = i.next();
				result = result + a + " "; 
			}
			result = result+") ";			
		}
		// directions
		{	result = result+" [ ";
			Iterator<Direction> i = directions.iterator();
			while(i.hasNext())
			{	Direction d = i.next();
				result = result + d + " "; 
			}
			result = result+"] ";
		}
		return result;
	}

/*	
	public GeneralAction copy()
	{	GeneralAction result;
		result = new GeneralAction(name);
		result.setActor(actor);
		result.setTarget(target);
		result.setDirection(direction);
		result.setContact(contact);
		result.setOrientation(orientation);
		result.setTilePosition(tilePosition);
		return result;
	}
*/
	
	/**
	 * la specificAction passée en paramètre est elle généralisée par 
	 * cette generalAction ?
	 */
	public boolean subsume(AbstractAction a)
	{	boolean result;
		if(a instanceof SpecificAction)
		{	SpecificAction action = (SpecificAction)a;
			// name
			result = name.equalsIgnoreCase(action.getName());
			// actor
			if(result)
				result = actors.contains(action.getActor().getClass());
			// target
			if(result && action.getTarget()!=null)
				result = targets.contains(action.getTarget().getClass());
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
		}
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
		return result;
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
			// misc
			actors = null;
			contacts = null;
			directions = null;
			orientations = null;
			targets = null;
			tilePositions = null;
		}
	}
	
	public GeneralAction copy()
	{	GeneralAction result = new GeneralAction(name);
		// actors
		{	Iterator<Class<?>> i = actors.iterator();
			while(i.hasNext())
			{	Class<?> a = i.next();
				result.addActor(a);
			}
		}
		// contacts
		{	Iterator<Contact> i = contacts.iterator();
			while(i.hasNext())
			{	Contact c = i.next();
				result.addContact(c); 
			}
		}
		// orientations
		{	Iterator<Orientation> i = orientations.iterator();
			while(i.hasNext())
			{	Orientation o = i.next();
				result.addOrientation(o); 
			}
		}
		// targets
		{	Iterator<Class<?>> i = targets.iterator();
			while(i.hasNext())
			{	Class<?> t = i.next();
				result.addTarget(t); 
			}
		}
		// directions
		{	Iterator<Direction> i = directions.iterator();
			while(i.hasNext())
			{	Direction d = i.next();
				result.addDirection(d);
			}
		}
		// tile positions
		{	Iterator<TilePosition> i = tilePositions.iterator();
			while(i.hasNext())
			{	TilePosition tp = i.next();
				result.addTilePosition(tp);
			}
		}
		result.finished = finished;
		return result;
	}
}
