package org.totalboumboum.engine.content.feature.gesture.anime.color;

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

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ColorMap extends ColorRule
{	private static final long serialVersionUID = 1L;	

	public ColorMap(PredefinedColor color)
	{	super(color);
	}

	/////////////////////////////////////////////////////////////////
	// MAP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<Integer,byte[]> map = new HashMap<Integer, byte[]>();
	
	public void addColor(Integer key, byte[] value)
	{	map.put(key,value);		
	}
	
	public Set<Entry<Integer,byte[]>> entrySet()
	{	return map.entrySet();		
	}
}
