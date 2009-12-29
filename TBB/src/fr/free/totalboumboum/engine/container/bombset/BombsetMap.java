package fr.free.totalboumboum.engine.container.bombset;

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

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;

public class BombsetMap implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// BOMBSETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Bombset partialBombset = null;
	private final HashMap<PredefinedColor,Bombset> bombsets = new HashMap<PredefinedColor, Bombset>();
	
	public void initBombset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	partialBombset = BombsetLoader.initBombset(folderPath);
	}

	public Bombset completeBombset(String folderPath, PredefinedColor color) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Bombset result = bombsets.get(color);
		if(result==null)
		{	result = partialBombset.copy();
			BombsetLoader.completeBombset(folderPath,color,result);
			bombsets.put(color,result);
		}
		return result;
	}
	
	public Bombset getBombset(PredefinedColor color)
	{	return bombsets.get(color);		
	}
}
