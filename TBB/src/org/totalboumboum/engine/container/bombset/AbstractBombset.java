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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.engine.content.feature.ability.StateAbility;

public abstract class AbstractBombset
{	
	/////////////////////////////////////////////////////////////////
	// REQUIRED ABILITIES		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<List<StateAbility>> requiredAbilities = new ArrayList<List<StateAbility>>();;
	
	@SuppressWarnings("unused")
	private void setRequiredAbilities(List<List<StateAbility>> requiredAbilities)
	{	this.requiredAbilities = requiredAbilities;
	}
		
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;

			// abilities
			{	Iterator<List<StateAbility>> it = requiredAbilities.iterator();
				while(it.hasNext())
				{	List<StateAbility> temp = it.next();
					Iterator<StateAbility> it2 = temp.iterator();
					while(it2.hasNext())
					{	StateAbility temp2 = it2.next();
						temp2.finish();
						it2.remove();
					}
					it.remove();
				}
			}
		}
	}
}
