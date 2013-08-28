package org.totalboumboum.tools.images;

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

import java.awt.Color;
import java.io.Serializable;

/**
 * This class represents all the
 * color which can be assigned
 * to a player.
 * 
 * @author Vincent Labatut
 */
public enum PredefinedColor implements Serializable
{	/** Black */
	BLACK(new Color(0,0,0)),
	/** Blue */
	BLUE(new Color(3,3,250)),
	/** Brown */
	BROWN(new Color(82,33,14)),
	/** Cyan blue */
	CYAN(new Color(9,240,252)),
	/** Light green */
	GRASS(new Color(73,229,41)),
	/** Green */
	GREEN(new Color(24,109,9)),
	/** Grey */
	GREY(new Color(115,106,108)),
	/** Indigo */
	INDIGO(new Color(65,11,169)),
	/** Orange */
	ORANGE(new Color(255,118,6)),
	/** Pink */
	PINK(new Color(209,76,117)),
	/** Purple */
	PURPLE(new Color(149,114,234)),
	/** Red */
	RED(new Color(236,34,38)),
	/** Dark orange */
	RUST(new Color(196,57,2)),
	/** Dark blue */
	ULTRAMARINE(new Color(10,10,78)),
	/** White */
	WHITE(new Color(253,253,253)),
	/** Yellow */
	YELLOW(new Color(253,217,0));
	
	/**
	 * Builds a new color symbol,
	 * with the specified actual color.
	 * 
	 * @param color
	 * 		Actual color associated with the symbol.
	 */
	PredefinedColor(Color color)
	{	this.color = color;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Actual color associated to this symbol */
	private Color color = null;

	/**
	 * Returns the actual color
	 * associated to this symbol.
	 * 
	 * @return
	 * 		Actual color.
	 */
	public Color getColor()
	{	return color;
	}
}
