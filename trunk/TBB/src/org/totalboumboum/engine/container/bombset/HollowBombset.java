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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.engine.container.CachableSpriteContainer;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.sprite.bomb.BombFactory;
import org.totalboumboum.engine.content.sprite.bomb.HollowBombFactory;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowBombset extends AbstractBombset implements Serializable, CachableSpriteContainer
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// BOMB FACTORIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<HollowBombFactory> bombFactories = new ArrayList<HollowBombFactory>();
	
	@SuppressWarnings("unused")
	private void setBombFactories(List<HollowBombFactory> bombFactories)
	{	this.bombFactories = bombFactories;
	}
	
	public void addBombFactory(HollowBombFactory bombFactory, List<StateAbility> abilities)
	{	bombFactories.add(bombFactory);
		requiredAbilities.add(abilities);
	}
	
	public HollowBombFactory getBombFactory(String name)
	{	HollowBombFactory result = null;
		Iterator<HollowBombFactory> i = bombFactories.iterator();
		while(i.hasNext() && result==null)
		{	HollowBombFactory temp = i.next();
			if(temp.getBombName().equalsIgnoreCase(name))
				result = temp;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	public HollowBombset surfaceCopy()
	{	HollowBombset result = new HollowBombset();
	
		for(int i=0;i<bombFactories.size();i++)
		{	BombFactory bf = bombFactories.get(i).surfaceCopy();
			ArrayList<StateAbility> ra = requiredAbilities.get(i);
			result.addBombFactory(bf,ra);
		}
		
		return result;
	}
*/	
	public Bombset fill(double zoomFactor, PredefinedColor color) throws IOException
	{	Bombset result = new Bombset();
	
		for(int i=0;i<bombFactories.size();i++)
		{	BombFactory bf = bombFactories.get(i).fill(zoomFactor,color);
			List<StateAbility> ra = requiredAbilities.get(i);
			List<StateAbility> raCopy = new ArrayList<StateAbility>();
			for(StateAbility ability: ra)
				raCopy.add((StateAbility)ability.copy());
			result.addBombFactory(bf,raCopy);
		}
		
		return result;
	}
}
