package org.totalboumboum.tools.images;

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
	BLACK(new Color(0,0,0), new Color(150,150,150)),
	/** Blue */
	BLUE(new Color(3,3,200), new Color(200,200,200)),
	/** Brown */
	BROWN(new Color(82,33,14), new Color(200,200,200)),
	/** Cyan blue */
	CYAN(new Color(9,240,252), new Color(0,0,0)),
	/** Light green */
	GRASS(new Color(73,229,41), new Color(0,0,0)),
	/** Green */
	GREEN(new Color(24,109,9), new Color(0,0,0)),
	/** Grey */
	GREY(new Color(115,106,108), new Color(0,0,0)),
	/** Indigo */
	INDIGO(new Color(65,11,169), new Color(180,180,180)),
	/** Orange */
	ORANGE(new Color(255,118,6), new Color(0,0,0)),
	/** Pink */
	PINK(new Color(209,76,117), new Color(0,0,0)),
	/** Purple */
	PURPLE(new Color(149,114,234), new Color(0,0,0)),
	/** Red */
	RED(new Color(236,34,38), new Color(0,0,0)),
	/** Dark orange */
	RUST(new Color(196,57,2), new Color(0,0,0)),
	/** Dark blue */
	ULTRAMARINE(new Color(10,10,78), new Color(180,180,180)),
	/** White */
	WHITE(new Color(253,253,253), new Color(0,0,0)),
	/** Yellow */
	YELLOW(new Color(253,217,0), new Color(0,0,0));
	
	/**
	 * Builds a new color symbol,
	 * with the specified actual color.
	 * 
	 * @param color
	 * 		Actual color associated with the symbol.
	 * @param secondary
	 * 		Text color associated with the symbol.
	 */
	PredefinedColor(Color color, Color secondary)
	{	this.color = color;
		this.secondary = secondary;
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

	/////////////////////////////////////////////////////////////////
	// SECONDARY COLOR	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Secondary color associated to this symbol */
	private Color secondary = null;

	/**
	 * Returns the secondary color
	 * associated to this symbol.
	 * It corresponds to the text color
	 * when writing on such background.
	 * 
	 * @return
	 * 		Secondary color.
	 */
	public Color getSecondaryColor()
	{	return secondary;
	}
}
