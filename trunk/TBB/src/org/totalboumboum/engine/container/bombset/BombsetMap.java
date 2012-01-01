package org.totalboumboum.engine.container.bombset;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class BombsetMap
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Instance instance = null;
	
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
	private final HashMap<PredefinedColor,Bombset> bombsets = new HashMap<PredefinedColor, Bombset>();
	
	public void loadBombsets(String folderPath, List<PredefinedColor> playersColors) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// level (neutral)
		Bombset bombset = BombsetLoader.loadBombset(folderPath,null);
		bombsets.put(null,bombset);

		// players
		for(PredefinedColor color: playersColors)
    	{	bombset = BombsetLoader.loadBombset(folderPath,color);
			bombsets.put(color,bombset);
    	}
	}
	
	public Bombset getBombset(PredefinedColor color)
	{	Bombset result = bombsets.get(color);
		return result;
	}
}
