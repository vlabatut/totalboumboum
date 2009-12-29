package fr.free.totalboumboum.engine.container.explosionset;

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

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.fireset.Fireset;
import fr.free.totalboumboum.engine.container.level.instance.Instance;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;

public class Explosion implements Serializable
{	private static final long serialVersionUID = 1L;
	
	public Explosion()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Instance instance = null;
	
	public void setInstance(Instance instance) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.instance = instance;
		fireset = instance.getFiresetMap().getFireset(firesetName);
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
	Fireset fireset = null;
	String firesetName = null;
	
	public void setFiresetName(String firesetName)
	{	this.firesetName = firesetName;	
	}
	
/*	public void setFireset(Fireset fireset)
	{	this.fireset = fireset;	
	}
*/	
	public Fire makeFire(String name, Tile tile)
	{	Fire result = null;
		result = fireset.makeFire(name,tile);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// fireset
			if(fireset!=null)
			{	fireset.finish();
				fireset = null;
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// CACHE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/*
	 * the fireset has already been copied/loaded, so it is taken from the current level
	 * the rest of the explosion is copied normally
	 */
/*	public Explosion cacheCopy()
	{	Explosion result = new Explosion();
		FiresetMap fsm = RoundVariables.level.getFiresetMap();
		Fireset fs = fsm.getFireset(fireset.getName());
		result.setFireset(fs);		
		return result;
	}*/
}
