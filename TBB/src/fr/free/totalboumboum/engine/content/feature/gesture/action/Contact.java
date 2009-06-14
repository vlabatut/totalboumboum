package fr.free.totalboumboum.engine.content.feature.gesture.action;

import fr.free.totalboumboum.engine.content.sprite.getModulationStateAbilities;

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
/**
 * represents the kind of contact between the actor and the target during an action:
 * 
 */
public enum Contact
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
	public static Contact getContact(getModulationStateAbilities actor, getModulationStateAbilities target)
	{	Contact result;
		if(actor.isCollidingSprite(target))
			result = Contact.COLLISION;
		else if((actor.isIntersectingSprite(target)))
			result = Contact.INTERSECTION;
		else
			result = Contact.NONE;
		return result;
	}	
}