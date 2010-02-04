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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.engine.Cachable;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.bomb.BombFactory;
import org.xml.sax.SAXException;

public class Bombset implements Serializable, Cachable
{	private static final long serialVersionUID = 1L;

	public Bombset()
	{	bombFactories = new ArrayList<BombFactory>();
		requiredAbilities = new ArrayList<ArrayList<StateAbility>>();
	}
	
	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient Instance instance = null;
	
	public void setInstance(Instance instance) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.instance = instance;
		for(BombFactory bombFactory: bombFactories)
			bombFactory.setInstance(instance);
	}
	
	public Instance getInstance()
	{	return instance;	
	}

	/////////////////////////////////////////////////////////////////
	// REQUIRED ABILITIES		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<ArrayList<StateAbility>> requiredAbilities;
	
	@SuppressWarnings("unused")
	private void setRequiredAbilities(ArrayList<ArrayList<StateAbility>> requiredAbilities)
	{	this.requiredAbilities = requiredAbilities;
	}
		
	/////////////////////////////////////////////////////////////////
	// BOMB FACTORIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<BombFactory> bombFactories;
	
	@SuppressWarnings("unused")
	private void setBombFactories(ArrayList<BombFactory> bombFactories)
	{	this.bombFactories = bombFactories;
	}
	
	public void addBombFactory(BombFactory bombFactory, ArrayList<StateAbility> abilities)
	{	bombFactories.add(bombFactory);
		requiredAbilities.add(abilities);
	}
	
	/**
	 * only for player-generated bombs
	 * @param sprite
	 * @return
	 */
	public Bomb makeBomb(Sprite sprite)
	{	Bomb result = null;
		Tile tile = sprite.getTile();
		Iterator<ArrayList<StateAbility>> i = requiredAbilities.iterator();
		int ind = 0;
		while(result==null && i.hasNext())
		{	ArrayList<StateAbility> abilities = i.next();
			Iterator<StateAbility> j = abilities.iterator();
			boolean goOn = true;
			while(goOn && j.hasNext())
			{	StateAbility ability = j.next();
				StateAbility tp = sprite.modulateStateAbility(ability.getName());
				if(tp==null || !tp.isActive())
					goOn = false;
			}
			if(goOn)
			{	// make sprite
				BombFactory bf = bombFactories.get(ind);
				result = bf.makeSprite(tile);
				// set owner
				result.setOwner(sprite);
				// set time
				StateAbility ab = result.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
				if(ab.isActive())
				{	float time = ab.getStrength();
					ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_TIMER_COEFFICIENT);
					float coef = ab.getStrength();
					float delta = time*coef - time;
					StateAbility ab2 = new StateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
					ab2.setStrength(delta);
					result.addDirectAbility(ab2);
//System.out.println("coef:"+coef);			
//System.out.println("time:"+time);					
				}
			}
			else
				ind++;
		}
		return result;
	}

	/**
	 * only for level-generated bombs
	 * @param tile
	 * @param name
	 * @return
	 */
	public Bomb makeBomb(String name, Tile tile, int flameRange)
	{	Bomb result = null;
		Iterator<BombFactory> it = bombFactories.iterator();
		while(it.hasNext() && result==null)
		{	BombFactory bombFactory = it.next();
			if(bombFactory.getBombName().equalsIgnoreCase(name))
			{	result = bombFactory.makeSprite(tile);
				result.setFlameRange(flameRange); //NOTE this is performed in BombsetManager.dropBomb() for the Heroes. Maybe should it be normalized ? use an ability ? (cf note in BombsetMgr)
			}
		}
		return result;
	}

	public BombFactory getBombFactory(String name)
	{	BombFactory result = null;
		Iterator<BombFactory> i = bombFactories.iterator();
		while(i.hasNext() && result==null)
		{	BombFactory temp = i.next();
			if(temp.getBombName().equalsIgnoreCase(name))
				result = temp;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Bombset copy()
	{	Bombset result = new Bombset();
		for(int i=0;i<bombFactories.size();i++)
		{	BombFactory bf = bombFactories.get(i).copy();
			ArrayList<StateAbility> ra = requiredAbilities.get(i);
			result.addBombFactory(bf,ra);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// CACHE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public long getMemSize()
	{	long result = 0;
		
		// bombs
		for(BombFactory bf: bombFactories)
			result = result + bf.getMemSize();
		
		return result;
	}

	/*
	 * the Bombset has already been copied/loaded, so it is taken from the level
	 */
/*	public Bombset cacheCopy()
	{	Bombset result = RoundVariables.level.getBombset();
		return result;
	}*/
	public Bombset cacheCopy(double zoomFactor)
	{	Bombset result = new Bombset();
		for(int i=0;i<bombFactories.size();i++)
		{	BombFactory bf = bombFactories.get(i).cacheCopy(zoomFactor);
			ArrayList<StateAbility> ra = requiredAbilities.get(i);
			result.addBombFactory(bf,ra);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
