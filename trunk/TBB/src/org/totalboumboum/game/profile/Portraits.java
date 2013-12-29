package org.totalboumboum.game.profile;

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

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to handle the image representing
 * a player in menus.
 *  
 * @author Vincent Labatut
 */
public class Portraits
{	
	/////////////////////////////////////////////////////////////////
	// IN-GAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Images used in game */
	Map<String, BufferedImage> ingame = new HashMap<String, BufferedImage>();
	/** Loser face */
	public static final String INGAME_LOST = "LOST";
	/** Still in the game face */
	public static final String INGAME_NORMAL = "NORMAL";
	/** Out of the game face */
	public static final String INGAME_OUT = "OUT";
	/** Winner face */
	public static final String INGAME_WON = "WON";
	
	/**
	 * Add an in-game image to the current map.
	 * 
	 * @param name
	 * 		Image itself.
	 * @param image
	 * 		Name of the image.
	 */
	public void addIngamePortrait(String name, BufferedImage image)
	{	ingame.put(name, image);		
	}
	
	/**
	 * Retrieves one of the in-game images.
	 * 
	 * @param name
	 * 		Name of the image.
	 * @return
	 * 		Corresponding image itself.
	 */
	public BufferedImage getIngamePortrait(String name)
	{	return ingame.get(name);	
	}
	
	/**
	 * Checks if the in-game image whose name is specified as
	 * a parameter is contained in this object.
	 * 
	 * @param name
	 * 		Name of the image.
	 * @return
	 * 		{@code true} iff the image exists.
	 */
	public boolean containsIngamePortrait(String name)
	{	return ingame.containsKey(name);	
	}

	/////////////////////////////////////////////////////////////////
	// OFF-GAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Images used off game */
	Map<String, BufferedImage> outgame = new HashMap<String, BufferedImage>();
	/** Body image */
	public static final String OUTGAME_BODY = "BODY";
	/** Face image */
	public static final String OUTGAME_HEAD = "HEAD";
	
	/**
	 * Add an off-game image to the current map.
	 * 
	 * @param name
	 * 		Image itself.
	 * @param image
	 * 		Name of the image.
	 */
	public void addOffgamePortrait(String name, BufferedImage image)
	{	outgame.put(name, image);		
	}
	
	/**
	 * Retrieves one of the off-game images.
	 * 
	 * @param name
	 * 		Name of the image.
	 * @return
	 * 		Corresponding image itself.
	 */
	public BufferedImage getOffgamePortrait(String name)
	{	return outgame.get(name);	
	}
	
	/**
	 * Checks if the off-game image whose name is specified as
	 * a parameter is contained in this object.
	 * 
	 * @param name
	 * 		Name of the image.
	 * @return
	 * 		{@code true} iff the image exists.
	 */
	public boolean containsOffgamePortrait(String name)
	{	return outgame.containsKey(name);	
	}
}
