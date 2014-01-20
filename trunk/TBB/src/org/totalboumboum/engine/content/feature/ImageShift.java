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

import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * Symbol representing how much a sprite must be shifted vertically when drawn.
 * 
 * @author Vincent Labatut
 */
public enum ImageShift implements Serializable
{	/** Regular position (position = down side of the picture) */	
	DOWN,
	/** On top (vertically, not in terms of layers) of the bounded sprite */
	BOUNDHEIGHT;
	
	/**
	 * Returns the pixel value corresponding to this symbol.
	 * 
	 * @param boundSprite
	 * 		Sprite of reference.
	 * @return
	 * 		Offset in pixels. 
	 */
	public double getValue(Sprite boundSprite)
	{	double result = 0;
		switch(this)
		{	case DOWN:
				result = 0;
				break;
			case BOUNDHEIGHT:
				result = boundSprite.getBoundHeight();
				break;
		}
		return result;
	}
}
