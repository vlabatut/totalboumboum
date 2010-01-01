package org.totalboumboum.engine.content.feature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.tools.XmlTools;


/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
 * represents the kind of contact between the actor and the target during an action:
 * 
 */
public enum Contact implements Serializable
{	
	/** absolutely no contact, or contact undefined (because there's no target) */
	NONE,
	/** the actor and the target are colliding (whichever is moving towards the other) */
	COLLISION,
	/** the actor and the target are currently intersecting */ 
	INTERSECTION;
	
	/**
	 * returns the contact type, or NONE if the target is null
	 * 
	 * @param actor	sprite performing the action
	 * @param target	sprite undergoing the action
	 * @return	contact of the action
	 */
	public static Contact getContact(Sprite actor, Sprite target)
	{	Contact result;
		if(actor.isCollidingSprite(target))
			result = Contact.COLLISION;
		else if((actor.isIntersectingSprite(target)))
			result = Contact.INTERSECTION;
		else
			result = Contact.NONE;
		return result;
	}
	
	public static Contact getContact(Sprite actor, Tile tile)
	{	Sprite target = tile.getFloors().get(0);
		Contact result = getContact(actor,target);
		return result;
	}	

	/**
	 * load a contact value.
	 * the XML value SOME represents any contact except NONE. 
	 * the XML value ANY represents any contact including NONE. 
	 */
	public static ArrayList<Contact> loadContactsAttribute(Element root, String attName)
	{	ArrayList<Contact> result = new ArrayList<Contact>();
		Attribute attribute = root.getAttribute(attName);
		String contactStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
		String[] contactsStr = contactStr.split(" ");
		for(String str: contactsStr)
		{	if(str.equalsIgnoreCase(XmlTools.VAL_SOME))
			{	result.add(Contact.COLLISION);
				result.add(Contact.INTERSECTION);
			}
			else if(str.equalsIgnoreCase(XmlTools.VAL_ANY))
			{	result.add(Contact.COLLISION);
				result.add(Contact.INTERSECTION);
				result.add(Contact.NONE);
			}
			else
			{	Contact contact = Contact.valueOf(str);
				result.add(contact);
			}
		}
		return result;
	}
}