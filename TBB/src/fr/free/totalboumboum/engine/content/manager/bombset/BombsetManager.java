package fr.free.totalboumboum.engine.content.manager.bombset;

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

import java.util.Iterator;
import java.util.LinkedList;

import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import fr.free.totalboumboum.engine.content.feature.action.detonate.SpecificDetonate;
import fr.free.totalboumboum.engine.content.feature.action.trigger.SpecificTrigger;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.game.statistics.StatisticAction;
import fr.free.totalboumboum.game.statistics.StatisticEvent;

public class BombsetManager
{	
	public BombsetManager(Sprite sprite)
	{	this.sprite = sprite;
		bombset = null;
		droppedBombs = new LinkedList<Bomb>();
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Bombset bombset;
	
	public void setBombset(Bombset bombset)
	{	this.bombset = bombset;		
	}
	
	public Bombset getBombset()
	{	return bombset;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite sprite;
	
	public Sprite getSprite()
	{	return sprite;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected LinkedList<Bomb> droppedBombs;
	
	public Bomb makeBomb()
	{	Bomb result = bombset.makeBomb(sprite);
		return result;
	}
	
	public void dropBomb(Bomb bomb)
	{	// direction
		Direction direction = sprite.getCurrentFacingDirection();

		// can the bomb appear here?
		SpecificAppear action = new SpecificAppear(bomb,direction);
		ActionAbility ablt = bomb.modulateAction(action);
//System.out.println(sprite.getCurrentPosX()+": "+ablt.isActive());		
		if(ablt.isActive())
		{	// bomb range
			StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE);
			int flameRange = (int)ability.getStrength();
			ability = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE_MAX);
			if(ability.isActive())
			{	int limit = (int)ability.getStrength();
				if(flameRange>limit)
					flameRange = limit;
			}
//System.out.println("flameRange: "+flameRange);	
		
			// bomb number
			ability = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER);
			int droppedBombLimit = (int)ability.getStrength();
			ability = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE_MAX);
			if(ability.isActive())
			{	int limit = (int)ability.getStrength();
				if(droppedBombLimit>limit)
					droppedBombLimit = limit;
			}
//System.out.println("droppedBombLimit: "+droppedBombLimit);	
//System.out.println("droppedBombs.size(): "+droppedBombs.size());	
			
			if(droppedBombs.size()<droppedBombLimit)
			{	if(bomb!=null)
				{	bomb.setFlameRange(flameRange); //NOTE maybe it should be more consistent to use a specific StateAbility, initialized automatically from the owner when the bomb is made (by the bombfactory)?
					Tile tile = sprite.getTile();
					SpecificAction specificAction = new SpecificAppear(bomb);
					ablt = bomb.modulateAction(specificAction);
					if(ablt.isActive())
					{	bomb.setCurrentPosX(tile.getPosX());
						bomb.setCurrentPosY(tile.getPosY());
						bomb.appear(direction);
						droppedBombs.offer(bomb);
						// stats
						StatisticAction statAction = StatisticAction.DROP_BOMB;
						long statTime = sprite.getLoopTime();
						String statActor = sprite.getPlayer().getFileName();
						String statTarget = bomb.getBombName();
						StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
						sprite.addStatisticEvent(statEvent);
//System.out.println("droppedBombCount:"+droppedBombCount);
					}
				}
			}
		}
	}

	public void update()
	{	// dropped bombs
		Iterator<Bomb> i = droppedBombs.iterator();
		while(i.hasNext())
		{	Bomb bomb = i.next();
			if(bomb.isEnded())
			{	i.remove();
//System.out.println("droppedBombCount:"+droppedBombCount);	
			}
		}
		
		// diarrhea disease
		StateAbility ab = sprite.modulateStateAbility(StateAbilityName.HERO_DIARRHEA);
//if(sprite instanceof Hero)
//System.out.println("diarrhea ("+sprite.getCurrentPosX()+"): "+ab.isActive());		
		if(ab.isActive())
		{	ControlEvent event = new ControlEvent(ControlEvent.DROPBOMB,true);
			sprite.putControlEvent(event);
//			event = new ControlEvent(ControlEvent.DROPBOMB,false);
//			sprite.putControlEvent(event);
		}
	}

	public void triggerBomb()
	{	boolean found = false;
		Iterator<Bomb> b = droppedBombs.iterator();
		while(!found && b.hasNext())
		{	Bomb bomb = b.next();
			SpecificAction action = new SpecificDetonate(bomb);
			if(bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_CONTROL).isActive() && bomb.modulateAction(action).isActive())
			{	found = true;
				SpecificAction specificAction = new SpecificTrigger(sprite,bomb);
				ActionEvent event = new ActionEvent(specificAction);
				bomb.processEvent(event);				
			}
		}
	}
	
	public LinkedList<Bomb> getDroppedBombs()
	{	return droppedBombs;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// dropped bombs
			{	Iterator<Bomb> it = droppedBombs.iterator();
				while(it.hasNext())
				{	Bomb temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// bombset
			bombset.finish();
			bombset = null;
			// misc
			sprite = null;
		}
	}
}
