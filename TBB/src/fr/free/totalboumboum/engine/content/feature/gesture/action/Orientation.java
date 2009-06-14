package fr.free.totalboumboum.engine.content.feature.gesture.action;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

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
 * represents the compared directions of the action and of the target
 * (the levels is closed, so the target direction has to be considered in terms of shortest distance)  
 */
public enum Orientation
{	/** no orientation can be defined: there's no target or there's no action direction */
	UNDEFINED,
	/** the action is performed facing the target, or the action and the target are exactly on the same spot */
	SAME,
	/** the action is performed back to the target */
	OPPOSITE,
	/** the action is not performed facing nor back to the target */
	OTHER;

	/**
	 * returns the orientation, or UNDEFINED if the target is null or if the action is not directed.
	 * If the actor and the target are exactly on the same spot, the result is SAME.
	 * 
	 * @param actor	sprite performing the action
	 * @param target	sprite undergoing the action
	 * @return	orientation of the action
	 */
	public static Orientation getOrientation(Sprite actor, Sprite target)
	{	Orientation result;
		Direction facingDir = actor.getCurrentFacingDirection();
		// no orientation
		if(facingDir==Direction.NONE || target==null)
			result = UNDEFINED;
		else
		{	Direction relativeDir = Direction.getCompositeFromSprites(actor,target);
			// actor facing target
			if(relativeDir==Direction.NONE || relativeDir.hasCommonComponent(facingDir))
				result = Orientation.SAME;
			// actor back to target
			else if(relativeDir.hasCommonComponent(facingDir.getOpposite()))
				result = Orientation.OPPOSITE;
			// other directions
			else
				result = Orientation.OTHER;
		}
		return result;
	}	
}