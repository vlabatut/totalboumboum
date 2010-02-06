package org.totalboumboum.engine.container.bombset;

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

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.xml.sax.SAXException;

public class BombsetMap implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient Instance instance = null;
	
	public void setInstance(Instance instance) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.instance = instance;
		for(Bombset bombset: bombsets.values())
			bombset.setInstance(instance);
	}

	public Instance getInstance()
	{	return instance;	
	}

	/////////////////////////////////////////////////////////////////
	// BOMBSETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Bombset partialBombset = null;
	private final HashMap<PredefinedColor,Bombset> bombsets = new HashMap<PredefinedColor, Bombset>();
	private String path;
	
	public void initBombset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	path = folderPath;
		partialBombset = BombsetLoader.initBombset(folderPath);
	}

	public Bombset getBombset(PredefinedColor color) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Bombset result = bombsets.get(color);
		if(result==null)
		{	Bombset base = partialBombset.copy();
			result = BombsetLoader.completeBombset(path,color,base);
			bombsets.put(color,result);
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CACHE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public long getMemSize()
	{	long result = 0;
		
		// bombsets
		for(Bombset bs: bombsets.values())
			result = result + bs.getMemSize();
		
		return result;
	}
}
