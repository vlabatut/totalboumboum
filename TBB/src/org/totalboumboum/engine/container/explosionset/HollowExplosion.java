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

import java.io.Serializable;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowExplosion extends AbstractExplosion implements Serializable
{	private static final long serialVersionUID = 1L;

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
	// CACHE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/*
	 * the fireset has already been copied/loaded, so it is taken from the current level
	 * the rest of the explosion is copied normally
	 */
	public Explosion fill(double zoomFactor)
	{	Explosion result = new Explosion();
	
		result.setFiresetName(firesetName);
		
		return result;
	}
}
