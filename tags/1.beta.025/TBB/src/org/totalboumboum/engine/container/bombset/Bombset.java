package org.totalboumboum.engine.container.bombset;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.AnimeDirection;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.bomb.BombFactory;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteInsertionEvent;
import org.totalboumboum.game.round.RoundVariables;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Bombset extends AbstractBombset
{	
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
	// BOMB FACTORIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<BombFactory> bombFactories = new ArrayList<BombFactory>();
	
	@SuppressWarnings("unused")
	private void setBombFactories(List<BombFactory> bombFactories)
	{	this.bombFactories = bombFactories;
	}
	
	public void addBombFactory(BombFactory bombFactory, List<StateAbility> abilities)
	{	bombFactories.add(bombFactory);
		requiredAbilities.add(abilities);
	}
	
	/**
	 * Only for player-generated bombs.
	 * 
	 * @param sprite
	 * 		The sprite creating the bomb.
	 * @return
	 * 		The created bomb.
	 */
	public Bomb makeBomb(Sprite sprite)
	{	Bomb result = null;
		Tile tile = sprite.getTile();
		
		// make sprite
		BombFactory bf = getCurrentBombFactory(sprite);
		result = bf.makeSprite(tile);
		// set range
		int flameRange = sprite.getBombsetManager().processBombRange();
		result.setFlameRange(flameRange);
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
				
			// record/transmit event
			String name = bf.getBombName();
			name = name + "/" + bf.getColor();
			SpriteCreationEvent creationEvent = new SpriteCreationEvent(result,name);
			RoundVariables.writeEvent(creationEvent);
		}

		return result;
	}
	
	private BombFactory getCurrentBombFactory(Sprite sprite)
	{	BombFactory result = null;
		
		Iterator<List<StateAbility>> i = requiredAbilities.iterator();
		int ind = 0;
		while(result==null && i.hasNext())
		{	List<StateAbility> abilities = i.next();
			Iterator<StateAbility> j = abilities.iterator();
			boolean goOn = true;
			while(goOn && j.hasNext())
			{	StateAbility ability = j.next();
				StateAbility tp = sprite.modulateStateAbility(ability.getName());
				if(tp==null || !tp.isActive())
					goOn = false;
			}
			if(goOn)
				result = bombFactories.get(ind);
			else
				ind++;
		}
		
		return result;
	}
	
	
	/**
	 * Only for level-generated bombs
	 * 
	 * @param tile
	 * 		tile where the bomb should be dropped
	 * @param name
	 * 		bomb name (if null: take the first bomb)
	 * @param flameRange
	 * 		bomb range
	 * @param duration
	 * 		time bomb duration (can be negative if not a time bomb)
	 * @param insert
	 * 		sprite not inserted in the level (for the AI API).
	 * @return
	 * 		The generated bomb.
	 */
	public Bomb makeBomb(String name, Tile tile, int flameRange, int duration)
	{	Bomb result = null;
		BombFactory bombFactory = null;
		if(name==null)
			bombFactory = bombFactories.get(bombFactories.size()-1);
		else
		{	Iterator<BombFactory> it = bombFactories.iterator();
			while(it.hasNext() && bombFactory==null)
			{	BombFactory bf = it.next();
				if(bf.getBombName().equalsIgnoreCase(name))
					bombFactory = bf;
			}
		}
		
		if(bombFactory!=null)
		{	result = bombFactory.makeSprite(tile);
			result.setFlameRange(flameRange); //NOTE this is performed in BombsetManager.dropBomb() for the Heroes. Maybe should it be normalized ? use an ability ? (cf note in BombsetMgr)
			if(duration>=0)
			{	StateAbility ability = new StateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
				ability.setStrength(duration);
				ability.setFrame(true);
				result.addDirectAbility(ability);
			}
			
			SpriteCreationEvent event = new SpriteCreationEvent(result,name);
			RoundVariables.writeEvent(event);
		}
		
		return result;
	}

	// NOTE not tested yet
	// NOTE we don't really have to make fire, we can directly check in the factory's abilities
	public long getCurrentExplosionDuration(Sprite sprite)
	{	BombFactory bf = getCurrentBombFactory(sprite);
		Tile tile = sprite.getTile();
		Fire fire = bf.getExplosion().makeFire(null,tile);
		Gesture gesture = fire.getGesturePack().getGesture(GestureName.BURNING);
		AnimeDirection anime = gesture.getAnimeDirection(Direction.LEFT);
		long result = anime.getTotalDuration();
		return result;
	}
	
	public long getCurrentBombDuration(Sprite sprite)
	{	BombFactory bf = getCurrentBombFactory(sprite);
		long result = bf.getBombDuration();
		return result;
	}
	
	public BombFactory getBombFactory(String name)
	{	BombFactory result = null;
		Iterator<BombFactory> i = bombFactories.iterator();
		while(i.hasNext() && result==null)
		{	BombFactory temp = i.next();
			if(temp.getBombName().equalsIgnoreCase(name))
			{	result = temp;
			}
		}
		return result;
	}
}
