package org.totalboumboum.engine.content.feature;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.Locale;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.tools.xml.XmlNames;

/**
 * represents the role of the actor or the target during an action.
 * 
 * @author Vincent Labatut
 *
 */
public enum Role implements Serializable
{	/** no object required (likely: no target) */
	NONE ("None"),
	/** the actor or target is a block sprite */
	BLOCK ("Block"),
	/** the actor or target is a bomb sprite */
	BOMB ("Bomb"),
	/** the actor or target is a fire sprite */
	FIRE ("Fire"),
	/** the actor or target is a floor sprite */
	FLOOR ("Floor"),
	/** the actor or target is a hero sprite */
	HERO ("Hero"),
	/** the actor or target is an item sprite */
	ITEM ("Item");

	/** String form of the enum value */
	public String name;
	
	/**
	 * Builds a Role value.
	 * 
	 * @param name
	 * 		String form of this value.
	 */
	Role(String name)
	{	this.name = name;
	}
	
	/**
	 * Loads a role value.
	 * <br/>
	 * The XML value {@code SOME} represents any role except {@code NONE}. 
	 * The XML value {@code ANY} represents any role including {@code NONE}.
	 *  
	 * @param root
	 * 		Root of the XML file. 
	 * @param attName 
	 * 		Name of the attribute to be processed.
	 * @return 
	 * 		The {@code Role} read.
	 */
	public static List<Role> loadRolesAttribute(Element root, String attName)
	{	List<Role> result = new ArrayList<Role>();
		Attribute attribute = root.getAttribute(attName);
		if(attribute!=null)
		{	String roleStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
			String[] rolesStr = roleStr.split(" ");
			for(String str: rolesStr)
			{	if(str.equalsIgnoreCase(XmlNames.VAL_SOME))
				{	result.add(Role.BLOCK);
					result.add(Role.BOMB);
					result.add(Role.FIRE);
					result.add(Role.FLOOR);
					result.add(Role.HERO);
					result.add(Role.ITEM);
				}
				else if(str.equalsIgnoreCase(XmlNames.VAL_ANY))
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
