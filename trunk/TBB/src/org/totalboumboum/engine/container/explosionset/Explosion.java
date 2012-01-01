package org.totalboumboum.engine.container.explosionset;

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

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.fireset.Fireset;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Explosion extends AbstractExplosion
{	
	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Instance instance = null;
	
	public void setInstance(Instance instance) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.instance = instance;
		fireset = instance.getFiresetMap().getFireset(firesetName);
if(fireset==null)
	System.err.println("fireset: can't find "+firesetName);
	}

	public Instance getInstance()
	{	return instance;	
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	String name;

	public String getName()
	{	return name;
	}

	public void setName(String name)
	{	this.name = name;
	}

	/////////////////////////////////////////////////////////////////
	// FIRESET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Fireset fireset = null;

	public Fire makeFire(String name, Tile tile)
	{	Fire result = null;
		
		if(fireset!=null)
			result = fireset.makeFire(name,tile);
		
		return result;
	}
}
