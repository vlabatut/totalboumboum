package org.totalboumboum.engine.content.feature.action;

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

import java.util.List;
import java.util.Locale;

import org.jdom.Element;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.tools.xml.XmlNames;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GeneralActionLoader
{		
	public static GeneralAction loadActionElement(Element root) throws ClassNotFoundException
    {	// name
		String strName = root.getAttribute(XmlNames.NAME).getValue().trim().toUpperCase(Locale.ENGLISH);
		ActionName name = ActionName.valueOf(strName);
		GeneralAction result = name.createGeneralAction();
		
		// actors
		{	List<Role> actors = Role.loadRolesAttribute(root,XmlNames.ACTOR);
			for(Role actor: actors)
				result.addActor(actor);
		}
		
		// targets
		{	List<Role> targets = Role.loadRolesAttribute(root,XmlNames.TARGET);
			for(Role target: targets)
				result.addTarget(target);
		}
		
		// directions
		{	List<Direction> directions = Direction.loadDirectionsAttribute(root,XmlNames.DIRECTION);
			for(Direction direction: directions)
				result.addDirection(direction);
		}
		
		// circumstances
		CircumstanceLoader.loadCircumstanceElement(root,result.getCircumstance());
		
		// results
		return result;
    }	
}
