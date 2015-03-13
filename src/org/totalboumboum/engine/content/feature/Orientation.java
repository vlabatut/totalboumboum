package org.totalboumboum.engine.content.feature;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.xml.XmlNames;

/**
 * Represents the compared directions of the action and of the target
 * (the levels is closed, so the target direction has to be considered in terms of shortest distance).
 * 
 * @author Vincent Labatut
 *
 */
public enum Orientation implements Serializable
{	/** No target or no action direction */
	NONE,
	/** The action is performed facing the target, or the action and the target are exactly on the same spot */
	FACE,
	/** The action is performed back to the target */
	BACK,
	/** The action is not performed facing nor back to the target */
	OTHER,
	/** The actor and target are exactly at the same place */
	NEUTRAL;
	
	/**
	 * Returns the orientation, or {@link #NONE} if the target is {@code null} or if the action is not directed.
	 * If the actor and the target are exactly on the same spot, the result is {@link #NEUTRAL}.
	 * 
	 * @param actor	
	 * 		Sprite performing the action.
	 * @param target
	 * 		Sprite undergoing the action.
	 * @return	
	 * 		Orientation of the action.
	 */
	public static Orientation getOrientation(Sprite actor, Sprite target)
	{	Orientation result;
		Direction facingDir = actor.getCurrentFacingDirection();
		// no orientation
		if(facingDir==Direction.NONE || target==null)
			result = Orientation.NONE;
		else
		{	Direction relativeDir = RoundVariables.level.getCompositeFromSprites(actor,target);
			
			// actor facing target
			if(relativeDir.hasCommonComponent(facingDir))
				result = Orientation.FACE;
			
			// actor back to target
			else if(relativeDir.hasCommonComponent(facingDir.getOpposite()))
				result = Orientation.BACK;
			
			// no direction
			else if(relativeDir==Direction.NONE)
				result = Orientation.NEUTRAL;
			
			// other directions
			else
				result = Orientation.OTHER;
		}
		return result;
	}	

	/**
	 * Returns the orientation of the specified sprite
	 * relatively to the specified tile.
	 * 
	 * @param actor
	 * 		Sprite of interest.
	 * @param tile
	 * 		Tile of interest.
	 * @return
	 * 		Relative orientation of the sprite relatively to the tile.
	 */
	public static Orientation getOrientation(Sprite actor, Tile tile)
	{	Sprite target = tile.getFloors().get(0);
		Orientation result = getOrientation(actor,target);
		return result;
	}	

	/**
	 * Loads an orientation value.
	 * The XML value {@code SOME} represents any orientation except {@link #NONE}. 
	 * The XML value {@code ANY} represents any orientation including {@link #NONE}. 
	 * 
	 * @param root
	 * 		XML element. 
	 * @param attName 
	 * 		Attribute name.
	 * @return 
	 * 		List of orientation objects.
	 */
	public static List<Orientation> loadOrientationsAttribute(Element root, String attName)
	{	List<Orientation> result = new ArrayList<Orientation>();
		Attribute attribute = root.getAttribute(attName);
		String orientationStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
		String[] orientationsStr = orientationStr.split(" ");
		for(String str: orientationsStr)
		{	if(str.equalsIgnoreCase(XmlNames.VAL_SOME))
			{	result.add(Orientation.BACK);
				result.add(Orientation.OTHER);
				result.add(Orientation.FACE);
				result.add(Orientation.NEUTRAL);
			}
			else if(str.equalsIgnoreCase(XmlNames.VAL_ANY))
			{	result.add(Orientation.BACK);
				result.add(Orientation.OTHER);
				result.add(Orientation.FACE);
				result.add(Orientation.NEUTRAL);
				result.add(Orientation.NONE);
			}
			else
			{	Orientation orientation = Orientation.valueOf(str);
				result.add(orientation);
			}
		}
		return result;
	}
}