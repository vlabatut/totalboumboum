package org.totalboumboum.engine.content.sprite.block;

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

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import org.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
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
public class BlockEventManager extends EventManager
{
	public BlockEventManager(Block sprite)
	{	super(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ActionEvent event)
	{	if(event.getAction() instanceof SpecificConsume)
			actionConsume(event);
	}

	private void actionConsume(ActionEvent event)
	{	if(gesture.equals(GestureName.STANDING))
		{	gesture = GestureName.BURNING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
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
		{	// spawn or not ?
			StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.BLOCK_SPAWN);
			// can spawn
			if(ablt.isActive())
			{	sprite.addDelay(DelayManager.DL_SPAWN, ablt.getStrength());
				gesture = GestureName.HIDING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
			// cannot spawn
			else
			{	endSprite();
			}
			
			// item ?
/*			Item hs = (Item)sprite.getHiddenSprite();
			Tile tile = sprite.getTile(); 
			if(hs!=null)
			{	SpecificAction action = new SpecificAppear(hs,tile);
				AbstractAbility ab = hs.modulateAction(action);
				if(ab.isActive())
				{	hs.initGesture();
					tile.addSprite(hs);
					sprite.setHiddenSprite(null);
				}
			}
*/		}
		else if(gesture.equals(GestureName.ENTERING))
		{	gesture = GestureName.PREPARED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

	private void engDelayOver(EngineEvent event)
	{	if(gesture.equals(GestureName.HIDING) && event.getStringParameter().equals(DelayManager.DL_SPAWN))
		{	SpecificAction specificAction = new SpecificAppear(sprite/*,Direction.NONE*/);
			AbstractAbility ability = sprite.modulateAction(specificAction);
			if(ability.isActive())
			{	StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.BLOCK_SPAWN);
				sprite.modifyUse(ablt,-1);
				gesture = GestureName.APPEARING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
			else
			{	//StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.BLOCK_SPAWN);
				sprite.addIterDelay(DelayManager.DL_SPAWN,1);	
				//sprite.addDelay(DelayManager.DL_SPAWN, ablt.getStrength());	
			}
		}
		//
		else if(event.getStringParameter().equals(DelayManager.DL_START))
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
	{	EventManager result = new BlockEventManager((Block)sprite);
		return result;
	}
}
