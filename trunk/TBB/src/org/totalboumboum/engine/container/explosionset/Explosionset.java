package org.totalboumboum.engine.container.explosionset;

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

import org.totalboumboum.engine.container.level.instance.Instance;
import org.xml.sax.SAXException;


public class Explosionset implements Serializable
{	private static final long serialVersionUID = 1L;

	public Explosionset()
	{	explosions = new HashMap<String,Explosion>();
	}
	
	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Instance instance = null;
	
	public void setInstance(Instance instance) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.instance = instance;
		for(Explosion explosion: explosions.values())
			explosion.setInstance(instance);
	}

	public Instance getInstance()
	{	return instance;	
	}

	/////////////////////////////////////////////////////////////////
	// EXPLOSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,Explosion> explosions;
	
	@SuppressWarnings("unused")
	private void setExplosions(HashMap<String,Explosion> explosions)
	{	this.explosions = explosions;
	}
	
	public void addExplosion(String name, Explosion explosion)
	{	explosions.put(name,explosion);
	}
	
	public Explosion getExplosion(String name)
	{	Explosion result = explosions.get(name);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	public Explosionset copy()
	{	Explosionset result = new Explosionset();
		for(Entry<String,Explosion> entry: explosions.entrySet())
		{	Explosion explosion = entry.getValue().copy();
			String name = entry.getKey();
			result.addExplosion(name,explosion);
		}
		return result;
	}
	*/
	/////////////////////////////////////////////////////////////////
	// CACHE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/*
	 * the Bombset has already been copied/loaded, so it is taken from the level
	 */
/*	public Explosionset cacheCopy()
	{	Explosionset result = RoundVariables.level.getBombset();
		return result;
	}
	public Explosionset cacheCopy(double zoomFactor)
	{	Explosionset result = new Explosionset();
		for(int i=0;i<bombFactories.size();i++)
		{	BombFactory bf = bombFactories.get(i).cacheCopy(zoomFactor,result);
			ArrayList<StateAbility> ra = requiredAbilities.get(i);
			result.addBombFactory(bf,ra);
		}
		return result;
	}
*/	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			for(Explosion explosion: explosions.values())
				explosion.finish();
			explosions.clear();
		}
	}
}
