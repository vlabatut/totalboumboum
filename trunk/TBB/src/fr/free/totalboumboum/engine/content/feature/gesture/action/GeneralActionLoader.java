package fr.free.totalboumboum.engine.content.feature.gesture.action;

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
import java.util.Locale;

import org.jdom.Element;

import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.Role;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.tools.XmlTools;

public class GeneralActionLoader
{		
	public static GeneralAction loadActionElement(Element root) throws ClassNotFoundException
    {	// name
		String strName = root.getAttribute(XmlTools.ATT_NAME).getValue().trim().toUpperCase(Locale.ENGLISH);
		ActionName name = ActionName.valueOf(strName);
		GeneralAction result = name.createGeneralAction();
		
		try
		{	// actors
			{	ArrayList<Role> actors = Role.loadRolesAttribute(root,XmlTools.ATT_ACTOR);
				for(Role actor: actors)
					result.addTarget(actor);
			}
			
			// targets
			{	ArrayList<Role> targets = Role.loadRolesAttribute(root,XmlTools.ATT_TARGET);
				for(Role target: targets)
					result.addTarget(target);
			}
			
			// directions
			{	ArrayList<Direction> directions = Direction.loadDirectionsAttribute(root,XmlTools.ATT_DIRECTION);
				for(Direction direction: directions)
					result.addDirection(direction);
			}
			
			// contacts
			{	ArrayList<Contact> contacts = Contact.loadContactsAttribute(root,XmlTools.ATT_CONTACT);
				for(Contact contact: contacts)
					result.addContact(contact);
			}
			
			// tilePositions
			{	ArrayList<TilePosition> tilePositions = TilePosition.loadTilePositionsAttribute(root,XmlTools.ATT_TILE_POSITION);
				for(TilePosition tilePosition: tilePositions)
					result.addTilePosition(tilePosition);
			}
			
			// orientations
			{	ArrayList<Orientation> orientations = Orientation.loadOrientationsAttribute(root,XmlTools.ATT_ORIENTATION);
				for(Orientation orientation: orientations)
					result.addOrientation(orientation);
			}
		}
		catch (IncompatibleParameterException e)
		{	e.printStackTrace();
		}
		
		// results
		return result;
    }	
}
