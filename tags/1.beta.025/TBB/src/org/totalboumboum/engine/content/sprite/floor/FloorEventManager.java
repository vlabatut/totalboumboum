package org.totalboumboum.engine.content.sprite.floor;

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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.crush.SpecificCrush;
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
public class FloorEventManager extends EventManager
{
	public FloorEventManager(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// ACTION EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ActionEvent event)
	{	if(event.getAction() instanceof SpecificCrush)
			actionCrush(event);
	}

	private void actionCrush(ActionEvent event)
	{	// crushed by another floor: just disappear
		if(gesture.equals(GestureName.STANDING))
		{	endSprite();
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
		else if(event.getName().equals(EngineEvent.START_FALL))
			engStartFall(event);
		else if(event.getName().equals(EngineEvent.TILE_LOW_ENTER))
			engTileLowEnter(event);
	}	

	private void engAnimeOver(EngineEvent event)
	{	if(gesture.equals(GestureName.APPEARING))
		{	gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
		else if(gesture.equals(GestureName.ENTERING))
		{	gesture = GestureName.PREPARED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
		else if(gesture.equals(GestureName.FALLING))
		{	gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			EngineEvent e = new EngineEvent(EngineEvent.TILE_LOW_ENTER,sprite,null,sprite.getActualDirection());
			Tile tile = sprite.getTile();
			tile.spreadEvent(e);
			Floor floor = (Floor)sprite;
			floor.crushTile(tile);
		}
	}
	
	private void engDelayOver(EngineEvent event)
	{	if(event.getStringParameter().equals(DelayManager.DL_START))
		{	if(gesture.equals(GestureName.PREPARED))
				start();
			else
				sprite.addIterDelay(DelayManager.DL_START,1);
		}
	}
	
	private void engEnter(EngineEvent event)
	{	if(gesture.equals(GestureName.NONE))
		{	gesture = GestureName.ENTERING;
			spriteDirection = event.getDirection();
			StateAbility ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_ENTRY_DURATION);
			double duration = ability.getStrength();
			if(duration<=0)
				duration = 1;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
		}
	}

	private void engStart(EngineEvent event)
	{	if(gesture.equals(GestureName.PREPARED))
		{	start();
		}
		else if(gesture.equals(GestureName.ENTERING))
		{	sprite.addIterDelay(DelayManager.DL_START,1);			
		}
	}

	private void engStartFall(EngineEvent event)
	{	if(gesture.equals(GestureName.NONE))
		{	gesture = GestureName.FALLING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

	private void engTileLowEnter(EngineEvent event)
	{	
/*		if(gesture.equals(GestureName.STANDING))
		{	// another floor has been set: remove the current one
			if(event.getSource()!=sprite && event.getSource() instanceof Floor)
				endSprite();
		}
*/
	}

	/////////////////////////////////////////////////////////////////
	// ACTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void start()
	{	sprite.startItemManager();
		gesture = GestureName.STANDING;
		sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public EventManager copy(Sprite sprite)
	{	EventManager result = new FloorEventManager((Floor)sprite);
		return result;
	}
}
