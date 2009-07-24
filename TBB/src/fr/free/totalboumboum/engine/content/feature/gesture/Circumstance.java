package fr.free.totalboumboum.engine.content.feature.gesture;

import java.util.ArrayList;

import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
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

public class Circumstance
{	
	public Circumstance()
	{			
	}
	
	public Circumstance(Sprite reference, Sprite other)
	{	initCircumstance(reference,other);		
	}
	
	public void initCircumstance()
	{	Orientation orientation = Orientation.UNDEFINED;
		setOrientation(orientation);
		Contact contact = Contact.NONE;
		setContact(contact);
		TilePosition tilePosition = TilePosition.UNDEFINED;
		setTilePosition(tilePosition);		
	}

	public void initCircumstance(Sprite reference, Sprite other)
	{	Orientation orientation = Orientation.getOrientation(reference,other);
		setOrientation(orientation);
		Contact contact = Contact.getContact(reference,other);
		setContact(contact);
		TilePosition tilePosition = TilePosition.getTilePosition(reference,other);
		setTilePosition(tilePosition);		
	}
	
	/////////////////////////////////////////////////////////////////
	// ORIENTATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<Orientation> orientations = new ArrayList<Orientation>();

	public Orientation getOrientation()
	{	Orientation result = null;
		if(orientations.size()>0)
			result = orientations.get(0);
		return result;
	}

	public ArrayList<Orientation> getOrientations()
	{	return orientations;	
	}
	
	public void setOrientation(Orientation orientation)
	{	if(orientations.size()>0)
			orientations.set(0,orientation);
	}
	
	public void addOrientation(Orientation orientation)
	{	orientations.add(orientation);
	}
	
	public boolean containsOrientation(Orientation orientation)
	{	return orientations.contains(orientation);	
	}

/*	public boolean containsAllOrientations(ArrayList<Orientation> orientations)
	{	return orientations.containsAll(orientations);	
	}
*/
	/////////////////////////////////////////////////////////////////
	// TILE POSITION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<TilePosition> tilePositions = new ArrayList<TilePosition>();

	public TilePosition getTilePosition()
	{	TilePosition result = null;
		if(tilePositions.size()>0)
			result = tilePositions.get(0);
		return result;
	}

	public ArrayList<TilePosition> getTilePositions()
	{	return tilePositions;	
	}
	
	public void setTilePosition(TilePosition tilePosition)
	{	if(tilePositions.size()>0)
			tilePositions.set(0,tilePosition);
	}

	public void addTilePosition(TilePosition tilePosition)
	{	tilePositions.add(tilePosition);
	}

	public boolean containsTilePosition(TilePosition tilePosition)
	{	return tilePositions.contains(tilePosition);	
	}

/*	public boolean containsAllTilePositions(ArrayList<TilePosition> tilePositions)
	{	return tilePositions.containsAll(tilePositions);	
	}
*/
	/////////////////////////////////////////////////////////////////
	// CONTACT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<Contact> contacts = new ArrayList<Contact>();

	public Contact getContact()
	{	Contact result = null;
		if(contacts.size()>0)
			result = contacts.get(0);
		return result;
	}

	public ArrayList<Contact> getContacts()
	{	return contacts;	
	}
	
	public void setContact(Contact contact)
	{	if(contacts.size()>0)
			contacts.set(0,contact);
	}

	public void addContact(Contact contact)
	{	contacts.add(contact);
	}

	public boolean containsContact(Contact contact)
	{	return contacts.contains(contact);	
	}

/*	public boolean containsAllContacts(ArrayList<Contact> contact)
	{	return contacts.containsAll(contacts);	
	}
*/
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean subsume(Circumstance circumstance)
	{	boolean result = true;
		// contact
		if(result)
			result = contacts.containsAll(circumstance.getContacts());
		// orientation
		if(result)
			result = orientations.containsAll(circumstance.getOrientations());
		// tile position
		if(result)
			result = tilePositions.containsAll(circumstance.getTilePositions());
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	contacts.clear();
			orientations.clear();
			tilePositions.clear();
		}
	}

}
