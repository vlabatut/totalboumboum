package org.totalboumboum.engine.content.sprite.item;

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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import org.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import org.totalboumboum.engine.content.feature.action.gather.SpecificGather;
import org.totalboumboum.engine.content.feature.action.release.SpecificRelease;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.manager.delay.DelayManager;
import org.totalboumboum.engine.content.manager.event.EventManager;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ItemEventManager extends EventManager
{	
	public ItemEventManager(Item sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// ACTION EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ActionEvent event)
	{	if(event.getAction() instanceof SpecificConsume)
			actionConsume(event);
		else if(event.getAction() instanceof SpecificGather)
			actionGather(event); 
		else if(event.getAction() instanceof SpecificRelease)
			actionRelease(event); 
	}

	private void actionConsume(ActionEvent event)
	{	if(gesture.equals(GestureName.STANDING))
		{	StateAbility ability = sprite.modulateStateAbility(StateAbilityName.ITEM_INDESTRUCTIBLE);
			if(ability.isActive())
			{	// make the item disapear
				gesture = GestureName.DISAPPEARING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
				// then release it somewhere
				sprite.addIterDelay(DelayManager.DL_RELEASE,1);
			}
			else
			{	gesture = GestureName.BURNING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
		}
	}
		
	private void actionGather(ActionEvent event)
	{	if(gesture.equals(GestureName.STANDING) || gesture.equals(GestureName.APPEARING))
		{	gesture = GestureName.DISAPPEARING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}
		
	private void actionRelease(ActionEvent event)
	{	if(gesture.equals(GestureName.HIDING) || gesture.equals(GestureName.NONE))
		{	spriteDirection = event.getAction().getDirection();
			released();
		}
	}
		
	/////////////////////////////////////////////////////////////////
	// CONTROL EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ControlEvent event)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// ENGINE EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(EngineEvent event)
	{	if(event.getName().equals(EngineEvent.ANIME_OVER))
			engAnimeOver(event);
		else if(event.getName().equals(EngineEvent.DELAY_OVER))
			engDelayOver(event);
		else if(event.getName().equals(EngineEvent.ROUND_ENTER))
			engEnter(event);
		else if(event.getName().equals(EngineEvent.ROUND_START))
			engStart(event);
	}	

	private void engAnimeOver(EngineEvent event)
	{	if(gesture.equals(GestureName.APPEARING))
		{	gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
		else if(gesture.equals(GestureName.BURNING))
		{	endSprite();
		}
		else if(gesture.equals(GestureName.DISAPPEARING))
		{	gesture = GestureName.HIDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
		else if(gesture.equals(GestureName.ENTERING))
		{	gesture = GestureName.PREPARED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
		else if(gesture.equals(GestureName.RELEASED))
		{	gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

	private void engDelayOver(EngineEvent event)
	{	// could not enter at first, but can now appear (i.e. this happens after the begining of the round)
		if(gesture.equals(GestureName.NONE) && event.getStringParameter().equals(DelayManager.DL_ENTER))
		{	SpecificAction action = new SpecificAppear(sprite);
			ActionAbility actionAbility = sprite.modulateAction(action);
			// can appear >> appears
			if(actionAbility.isActive())
			{	appear();
			}
			// cannot appear >> wait for next iteration
			else
			{	sprite.addIterDelay(DelayManager.DL_ENTER,1);
			}
		}
		else if(gesture.equals(GestureName.DISAPPEARING) && event.getStringParameter().equals(DelayManager.DL_RELEASE))
		{	// delay is over too soon, relaunch it
			sprite.addIterDelay(DelayManager.DL_RELEASE,1);
		}
		else if(gesture.equals(GestureName.HIDING) && event.getStringParameter().equals(DelayManager.DL_RELEASE))
		{	// the item is released on the ground
			released();
		}
	}
	
	private void engEnter(EngineEvent event)
	{	if(gesture.equals(GestureName.NONE))
		{	spriteDirection = event.getDirection();
			SpecificAction action = new SpecificAppear(sprite);
			ActionAbility actionAbility = sprite.modulateAction(action);
			// can appear >> appears
			if(actionAbility.isActive())
			{	gesture = GestureName.ENTERING;
				StateAbility stateAbility = sprite.modulateStateAbility(StateAbilityName.SPRITE_ENTRY_DURATION);
				double duration = stateAbility.getStrength();
				if(duration<=0)
					duration = 1;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
			}
			// cannot appear >> wait for next iteration
			else
			{	sprite.addIterDelay(DelayManager.DL_ENTER,1);
			}
		}
	}
	
	private void engStart(EngineEvent event)
	{	sprite.startItemManager();
		if(gesture.equals(GestureName.PREPARED))
		{	gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

	/////////////////////////////////////////////////////////////////
	// ACTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/*
	 * action supposedly already tested
	 */
	private void appear()
	{	gesture = GestureName.APPEARING;
		sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		EngineEvent event = new EngineEvent(EngineEvent.TILE_LOW_ENTER,sprite,null,sprite.getActualDirection()); //TODO to be changed by a GESTURE_CHANGE event (or equiv.)
		sprite.getTile().spreadEvent(event);
	}
	
	private void released()
	{	// randomly find a tile
		Tile tile = randomlyFindTile();
		// if no tile available : wait for the next iteration
		if(tile==null)
			sprite.addIterDelay(DelayManager.DL_RELEASE,1);
		// else move the item to the tile and make it appear
		else
		{	gesture = GestureName.RELEASED;
			spriteDirection = Direction.NONE;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			EngineEvent evt = new EngineEvent(EngineEvent.TILE_LOW_ENTER,sprite,null,sprite.getActualDirection()); //TODO to be changed by a GESTURE_CHANGE event (or equiv.)
			sprite.getTile().spreadEvent(evt);
		}		
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public EventManager copy(Sprite sprite)
	{	EventManager result = new ItemEventManager((Item)sprite);
		return result;
	}
}
