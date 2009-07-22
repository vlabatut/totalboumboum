package fr.free.totalboumboum.engine.content.feature.gesture;

import java.util.ArrayList;

import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.TilePosition;

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
	/////////////////////////////////////////////////////////////////
	// ORIENTATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Orientation> orientations;

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

	public boolean containsAllOrientations(ArrayList<Orientation> orientations)
	{	return orientations.containsAll(orientations);	
	}

	/////////////////////////////////////////////////////////////////
	// TILE POSITION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<TilePosition> tilePositions;

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

	public boolean containsAllTilePositions(ArrayList<TilePosition> tilePositions)
	{	return tilePositions.containsAll(tilePositions);	
	}

	/////////////////////////////////////////////////////////////////
	// CONTACT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Contact> contacts;

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

	public boolean containsAllContacts(ArrayList<Contact> contact)
	{	return contacts.containsAll(contacts);	
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
