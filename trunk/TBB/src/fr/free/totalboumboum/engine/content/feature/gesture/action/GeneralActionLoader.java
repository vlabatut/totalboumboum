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

import java.util.Locale;

import org.jdom.Attribute;
import org.jdom.Element;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.tools.XmlTools;

public class GeneralActionLoader
{		
	public static final String ANY = "ANY";
	
	public static GeneralAction loadActionElement(Element root) throws ClassNotFoundException
    {	// name
		String strName = root.getAttribute(XmlTools.ATT_NAME).getValue().trim().toUpperCase(Locale.ENGLISH);
		ActionName name = ActionName.valueOf(strName);
		GeneralAction result = name.createGeneralAction();
		
		try
		{	// actors
			{	Attribute attribute = root.getAttribute(XmlTools.ATT_ACTOR);
				String actorStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
				String[] actorsStr = actorStr.split(" ");
				for(String str: actorsStr)
				{	if(str.equalsIgnoreCase(ANY))
					{	result.addActor(Role.BLOCK);
						result.addActor(Role.BOMB);
						result.addActor(Role.FIRE);
						result.addActor(Role.FLOOR);
						result.addActor(Role.HERO);
						result.addActor(Role.ITEM);
					}
					else
					{	Role role = Role.valueOf(str);
						result.addActor(role);
					}
				}
			}
			
			// targets
			{	Attribute attribute = root.getAttribute(XmlTools.ATT_TARGET);
				String targetStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
				String[] targetsStr = targetStr.split(" ");
				for(String str: targetsStr)
				{	if(str.equalsIgnoreCase(ANY))
					{	result.addTarget(Role.BLOCK);
						result.addTarget(Role.BOMB);
						result.addTarget(Role.FIRE);
						result.addTarget(Role.FLOOR);
						result.addTarget(Role.HERO);
						result.addTarget(Role.ITEM);
					}
					else
					{	Role role = Role.valueOf(str);
						result.addTarget(role);
					}
				}
			}
			
			// directions
			{	Attribute attribute = root.getAttribute(XmlTools.ATT_DIRECTION);
				String directionStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
				String[] directionsStr = directionStr.split(" ");
				for(String str: directionsStr)
				{	if(str.equalsIgnoreCase(ANY))
					{	result.addDirection(Direction.UP);
						result.addDirection(Direction.UPRIGHT);
						result.addDirection(Direction.RIGHT);
						result.addDirection(Direction.DOWNRIGHT);
						result.addDirection(Direction.DOWN);
						result.addDirection(Direction.DOWNLEFT);
						result.addDirection(Direction.LEFT);
						result.addDirection(Direction.UPLEFT);
					}
					else
					{	Direction direction = Direction.valueOf(str);
						result.addDirection(direction);
					}
				}
			}
			
			// contacts
			{	Attribute attribute = root.getAttribute(XmlTools.ATT_CONTACT);
				String contactStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
				String[] contactsStr = contactStr.split(" ");
				for(String str: contactsStr)
				{	if(str.equalsIgnoreCase(ANY))
					{	result.addContact(Contact.COLLISION);
						result.addContact(Contact.INTERSECTION);
					}
					else
					{	Contact contact = Contact.valueOf(str);
						result.addContact(contact);
					}
				}
			}
			
			// tilePositions
			{	Attribute attribute = root.getAttribute(XmlTools.ATT_TILE_POSITION);
				String tilePositionStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
				String[] tilePositionsStr = tilePositionStr.split(" ");
				for(String str: tilePositionsStr)
				{	if(str.equalsIgnoreCase(ANY))
					{	result.addTilePosition(TilePosition.NEIGHBOR);
						result.addTilePosition(TilePosition.REMOTE);
						result.addTilePosition(TilePosition.SAME);
						result.addTilePosition(TilePosition.UNDEFINED);
					}
					else
					{	TilePosition tilePosition = TilePosition.valueOf(str);
						result.addTilePosition(tilePosition);
					}
				}
			}
			
			// orientations
			{	Attribute attribute = root.getAttribute(XmlTools.ATT_ORIENTATION);
				String orientationStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
				String[] orientationsStr = orientationStr.split(" ");
				for(String str: orientationsStr)
				{	if(str.equalsIgnoreCase(ANY))
					{	result.addOrientation(Orientation.OPPOSITE);
						result.addOrientation(Orientation.OTHER);
						result.addOrientation(Orientation.SAME);
						result.addOrientation(Orientation.UNDEFINED);
					}
					else
					{	Orientation orientation = Orientation.valueOf(str);
						result.addOrientation(orientation);
					}
				}
			}
		}
		catch (IncompatibleParameterException e)
		{	e.printStackTrace();
		}
		
		// results
		return result;
    }
}
