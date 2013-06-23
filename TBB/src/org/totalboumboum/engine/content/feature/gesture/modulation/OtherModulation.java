package org.totalboumboum.engine.content.feature.gesture.modulation;

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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class OtherModulation extends AbstractStateModulation
{	private static final long serialVersionUID = 1L;

	public OtherModulation(String name)
	{	super(name);
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTACTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** contacts between the other sprite and the modulating sprite */
	protected final List<Contact> contacts = new ArrayList<Contact>();
/*
	protected ArrayList<Contact> getContacts()
	{	return contacts;
	}
*/
	public void addContact(Contact contact)
	{	contacts.add(contact);		
	}

	/////////////////////////////////////////////////////////////////
	// TILE POSITIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** positions of the other sprite in terms of tiles */
	protected final List<TilePosition> tilePositions =  new ArrayList<TilePosition>();
	
/*	protected ArrayList<TilePosition> getTilePositions()
	{	return tilePositions;
	}
*/	
	public void addTilePosition(TilePosition tilePosition)
	{	tilePositions.add(tilePosition);
	}

	/////////////////////////////////////////////////////////////////
	// MODULATE					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * tests if this modulation is related to the specified situation
	 */
	public boolean isConcerningSituation(Sprite modulator, Sprite modulated) 
	{	boolean result = true;
		// tile positions
		if(result)
		{	TilePosition tilePosition = TilePosition.getTilePosition(modulator,modulated);
			result = tilePositions.contains(tilePosition);
		}
		// contacts
		if(result)
		{	Contact contact = Contact.getContact(modulator,modulated);
			result = contacts.contains(contact);
		}
		//	
		return result;		
	}

	/////////////////////////////////////////////////////////////////
	// COMPARISON				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean equals(Object modulation)
	{	boolean result = false;
		if(modulation instanceof OtherModulation)
		{	OtherModulation m = (OtherModulation) modulation;
			result = getName().equals(m.getName());
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public OtherModulation copy()
	{	OtherModulation result = new OtherModulation(name);
		result.name = name;
		result.contacts.addAll(contacts);
		result.tilePositions.addAll(tilePositions);
		result.strength = strength;
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CACHE					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public OtherModulation cacheCopy(double zoomFactor)
	{	OtherModulation result = new OtherModulation(name);
		
		// name
		result.name = name;
		
		// contacts
		result.contacts.addAll(contacts);
		
		// tile positions
		result.tilePositions.addAll(tilePositions);

		// misc
		result.strength = strength;
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		
		return result;
	}
}
