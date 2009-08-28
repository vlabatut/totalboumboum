package fr.free.totalboumboum.engine.content.sprite.block;

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

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import fr.free.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.manager.delay.DelayManager;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;

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
			{	gesture = GestureName.ENDED;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
				sprite.endSprite();
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
	{	if(gesture.equals(GestureName.HIDING))
		{	SpecificAction specificAction = new SpecificAppear(sprite,Direction.NONE);
			AbstractAbility ability = sprite.modulateAction(specificAction);
			if(ability.isActive())
			{	StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.BLOCK_SPAWN);
				sprite.modifyUse(ablt,-1);
				gesture = GestureName.APPEARING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
			else
			{	StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.BLOCK_SPAWN);
				sprite.addDelay(DelayManager.DL_SPAWN, ablt.getStrength());	
			}
		}
	}
	
	private void engEnter(EngineEvent event)
	{	gesture = GestureName.ENTERING;
		spriteDirection = event.getDirection();
		StateAbility ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_ENTRY_DURATION);
		double duration = ability.getStrength();
		if(duration<=0)
			duration = 1;
		sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
	}
	
	private void engStart(EngineEvent event)
	{	if(gesture.equals(GestureName.PREPARED))
		{	gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

	/////////////////////////////////////////////////////////////////
	// ACTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
