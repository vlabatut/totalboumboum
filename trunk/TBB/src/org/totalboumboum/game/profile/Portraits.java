package org.totalboumboum.game.profile;

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

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Portraits
{	
	public static final String INGAME_LOST = "LOST";
	public static final String INGAME_NORMAL = "NORMAL";
	public static final String INGAME_OUT = "OUT";
	public static final String INGAME_WON = "WON";
	
	public static final String OUTGAME_BODY = "BODY";
	public static final String OUTGAME_HEAD = "HEAD";
	
	
	HashMap<String, BufferedImage> ingame = new HashMap<String, BufferedImage>();
	HashMap<String, BufferedImage> outgame = new HashMap<String, BufferedImage>();
	
	public void addIngamePortrait(String name, BufferedImage image)
	{	ingame.put(name, image);		
	}
	public BufferedImage getIngamePortrait(String name)
	{	return ingame.get(name);	
	}
	public boolean containsIngamePortrait(String name)
	{	return ingame.containsKey(name);	
	}

	public void addOutgamePortrait(String name, BufferedImage image)
	{	outgame.put(name, image);		
	}
	public BufferedImage getOutgamePortrait(String name)
	{	return outgame.get(name);	
	}
	public boolean containsOutgamePortrait(String name)
	{	return outgame.containsKey(name);	
	}
}

