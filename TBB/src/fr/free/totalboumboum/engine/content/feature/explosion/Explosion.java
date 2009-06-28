package fr.free.totalboumboum.engine.content.feature.explosion;

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

import fr.free.totalboumboum.engine.container.fireset.Fireset;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;

public class Explosion
{
	
	public Explosion()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRESET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	Fireset fireset;

	public void setFireset(Fireset fireset)
	{	this.fireset = fireset;	
	}
	
	public Fire makeFire(String name)
	{	Fire result = null;
		result = fireset.makeFire(name);
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
}
