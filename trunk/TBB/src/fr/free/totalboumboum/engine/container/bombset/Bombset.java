package fr.free.totalboumboum.engine.container.bombset;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.bomb.BombFactory;

public class Bombset
{	
	private ArrayList<BombFactory> bombFactories;
	private ArrayList<ArrayList<StateAbility>> requiredAbilities;
	
	public Bombset()
	{	bombFactories = new ArrayList<BombFactory>();
		requiredAbilities = new ArrayList<ArrayList<StateAbility>>();
	}
	
	@SuppressWarnings("unused")
	private void setBombFactories(ArrayList<BombFactory> bombFactories)
	{	this.bombFactories = bombFactories;
	}
	@SuppressWarnings("unused")
	private void setRequiredAbilities(ArrayList<ArrayList<StateAbility>> requiredAbilities)
	{	this.requiredAbilities = requiredAbilities;
	}
		
	public void addBombFactory(BombFactory bombFactory, ArrayList<StateAbility> abilities)
	{	bombFactories.add(bombFactory);
		requiredAbilities.add(abilities);
	}
	
	public Bomb makeBomb(Sprite sprite)
	{	Bomb result = null;
		Iterator<ArrayList<StateAbility>> i = requiredAbilities.iterator();
		int ind = 0;
		while(result==null && i.hasNext())
		{	ArrayList<StateAbility> abilities = i.next();
			Iterator<StateAbility> j = abilities.iterator();
			boolean goOn = true;
			while(goOn && j.hasNext())
			{	StateAbility ability = j.next();
				StateAbility tp = sprite.getAbility(ability);
				if(tp==null || !tp.isActive())
					goOn = false;
			}
			if(goOn)
			{	BombFactory bf = bombFactories.get(ind);
				result = bf.makeSprite();
				result.setOwner(sprite);
			}
			else
				ind++;
		}
		return result;
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// factories
			{	Iterator<BombFactory> it = bombFactories.iterator();
				while(it.hasNext())
				{	BombFactory temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// abilities
			{	Iterator<ArrayList<StateAbility>> it = requiredAbilities.iterator();
				while(it.hasNext())
				{	ArrayList<StateAbility> temp = it.next();
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
