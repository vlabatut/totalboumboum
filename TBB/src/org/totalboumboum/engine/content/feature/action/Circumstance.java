package org.totalboumboum.engine.content.feature.action;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Circumstance implements Serializable
{	private static final long serialVersionUID = 1L;

	public Circumstance()
	{			
	}
	
	public Circumstance(Sprite reference, Sprite other)
	{	initCircumstance(reference,other);		
	}
	
	public void initCircumstance()
	{	Orientation orientation = Orientation.NONE;
		setOrientation(orientation);
		Contact contact = Contact.NONE;
		setContact(contact);
		TilePosition tilePosition = TilePosition.NONE;
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
	private final List<Orientation> orientations = new ArrayList<Orientation>();

	public Orientation getOrientation()
	{	Orientation result = null;
		if(orientations.size()>0)
			result = orientations.get(0);
		return result;
	}

	public List<Orientation> getOrientations()
	{	return orientations;	
	}
	
	public void setOrientation(Orientation orientation)
	{	if(orientations.size()>0)
			orientations.set(0,orientation);
		else
			orientations.add(orientation);
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
		else
			tilePositions.add(tilePosition);
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
	private final List<Contact> contacts = new ArrayList<Contact>();

	public Contact getContact()
	{	Contact result = null;
		if(contacts.size()>0)
			result = contacts.get(0);
		return result;
	}

	public List<Contact> getContacts()
	{	return contacts;	
	}
	
	public void setContact(Contact contact)
	{	if(contacts.size()>0)
			contacts.set(0,contact);
		else
			contacts.add(contact);
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
		{	List<Contact> cts = circumstance.getContacts();
			result = contacts.containsAll(cts);		
		}
		
		// orientation
		if(result)
		{	List<Orientation> orts = circumstance.getOrientations();
			result = orientations.containsAll(orts);
		}
		
		// tile position
		if(result)
		{	List<TilePosition> tpos = circumstance.getTilePositions();
			result = tilePositions.containsAll(tpos);		
		}
		
		return result;
	}
}
