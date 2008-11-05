package fr.free.totalboumboum.engine.content.feature.action;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.tools.ClassTools;
import fr.free.totalboumboum.tools.XmlTools;

public class GeneralActionLoader
{	
	
	public static GeneralAction loadActionElement(Element root) throws ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		GeneralAction result = new GeneralAction(name);
		
		// actor
		{	Attribute attribute = root.getAttribute(XmlTools.ATT_ACTOR);
			if(attribute!=null)
			{	String actorStr = attribute.getValue().trim();
				if(actorStr.equals(AbstractAction.ROLE_ALL))
				{	result.addActor(Block.class);
					result.addActor(Bomb.class);
					result.addActor(Fire.class);
					result.addActor(Floor.class);
					result.addActor(Hero.class);
					result.addActor(Item.class);
				}
				else if(actorStr.equals(AbstractAction.ROLE_NONE))
					result.addActor(null);
				else
					result.addActor(ClassTools.getClassFromRole(actorStr));
			}
		}
		
		// target
		{	Attribute attribute = root.getAttribute(XmlTools.ATT_TARGET);
			if(attribute!=null)
			{	String targetStr = attribute.getValue().trim();
				if(targetStr.equals(AbstractAction.ROLE_ALL))
				{	result.addTarget(Block.class);
					result.addTarget(Bomb.class);
					result.addTarget(Fire.class);
					result.addTarget(Floor.class);
					result.addTarget(Hero.class);
					result.addTarget(Item.class);
				}
				else if(targetStr.equals(AbstractAction.ROLE_NONE))
					result.addTarget(null);
				else
					result.addTarget(ClassTools.getClassFromRole(targetStr));
			}
		}
		
		// direction
		{	String directionStr = root.getAttribute(XmlTools.ATT_DIRECTION).getValue().trim();
			if(directionStr.equals(AbstractAction.DIRECTION_ALL))
			{	for(Direction d : Direction.values())
					result.addDirection(d);
			}
			else
				result.addDirection(Direction.valueOf(directionStr.toUpperCase(Locale.ENGLISH)));
		}
		
		// contact
		{	String contactStr = root.getAttribute(XmlTools.ATT_CONTACT).getValue().trim();
			if(contactStr.equals(AbstractAction.CONTACT_ALL))
			{	for(Contact c : Contact.values())
					result.addContact(c);
			}
			else
				result.addContact(Contact.valueOf(contactStr.toUpperCase(Locale.ENGLISH)));
		}
		
		// tilePosition
		{	String tilePositionStr = root.getAttribute(XmlTools.ATT_TILE_POSITION).getValue().trim();
			if(tilePositionStr.equals(AbstractAction.TILE_POSITION_ALL))
			{	for(TilePosition c : TilePosition.values())
					result.addTilePosition(c);
			}
			else
				result.addTilePosition(TilePosition.valueOf(tilePositionStr.toUpperCase(Locale.ENGLISH)));
		}
		
		// orientation
		{	String orientationStr = root.getAttribute(XmlTools.ATT_ORIENTATION).getValue().trim();
			if(orientationStr.equals(AbstractAction.ORIENTATION_ALL))
			{	for(Orientation c : Orientation.values())
					result.addOrientation(c);
			}
			else
				result.addOrientation(Orientation.valueOf(orientationStr.toUpperCase(Locale.ENGLISH)));
		}
		
		// result
		return result;
    }
}
