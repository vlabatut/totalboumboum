package org.totalboumboum.engine.content.feature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import org.jdom.Attribute;
import org.jdom.Element;
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
 * represents the role of the actor or the target during an action.
 */
public enum Role implements Serializable
{	/** no object required (likely: no target) */
	NONE,
	/** the actor or target is a block sprite */
	BLOCK,
	/** the actor or target is a bomb sprite */
	BOMB,
	/** the actor or target is a fire sprite */
	FIRE,
	/** the actor or target is a floor sprite */
	FLOOR,
	/** the actor or target is a hero sprite */
	HERO,
	/** the actor or target is an item sprite */
	ITEM;

	/**
	 * load a role value.
	 * the XML value SOME represents any role except NONE. 
	 * the XML value ANY represents any role including NONE. 
	 */
	public static ArrayList<Role> loadRolesAttribute(Element root, String attName)
	{	ArrayList<Role> result = new ArrayList<Role>();
		Attribute attribute = root.getAttribute(attName);
		if(attribute!=null)
		{	String roleStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
			String[] rolesStr = roleStr.split(" ");
			for(String str: rolesStr)
			{	if(str.equalsIgnoreCase(XmlTools.VAL_SOME))
				{	result.add(Role.BLOCK);
					result.add(Role.BOMB);
					result.add(Role.FIRE);
					result.add(Role.FLOOR);
					result.add(Role.HERO);
					result.add(Role.ITEM);
				}
				else if(str.equalsIgnoreCase(XmlTools.VAL_ANY))
				{	result.add(Role.BLOCK);
					result.add(Role.BOMB);
					result.add(Role.FIRE);
					result.add(Role.FLOOR);
					result.add(Role.HERO);
					result.add(Role.ITEM);
					result.add(Role.NONE);
				}
				else
				{	Role role = Role.valueOf(str);
					result.add(role);
				}
			}
		}
		return result;
	}
}
