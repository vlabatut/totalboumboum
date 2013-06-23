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

import java.util.List;

import org.jdom.Element;
import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.tools.xml.XmlNames;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class CircumstanceLoader
{		
	public static void loadCircumstanceElement(Element root, Circumstance result) throws ClassNotFoundException
    {	// contacts
		{	List<Contact> contacts = Contact.loadContactsAttribute(root,XmlNames.CONTACT);
			for(Contact contact: contacts)
				result.addContact(contact);
		}
		
		// tilePositions
		{	List<TilePosition> tilePositions = TilePosition.loadTilePositionsAttribute(root,XmlNames.TILE_POSITION);
			for(TilePosition tilePosition: tilePositions)
				result.addTilePosition(tilePosition);
		}
		
		// orientations
		{	List<Orientation> orientations = Orientation.loadOrientationsAttribute(root,XmlNames.ORIENTATION);
			for(Orientation orientation: orientations)
				result.addOrientation(orientation);
		}
    }	
}
