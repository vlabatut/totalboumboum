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

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.GestureName;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.manager.delay.DelayManager;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.item.Item;

public class BlockEventManager extends EventManager
{
	public BlockEventManager(Block sprite)
	{	super(sprite);
	}
	
	public void initGesture()
	{	gesture = GestureName.STANDING;
		spriteDirection = Direction.NONE;
		sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
	}

/*
 * *****************************************************************
 * ACTION EVENTS
 * *****************************************************************
 */	
	@Override
	public void processEvent(ActionEvent event)
	{	if(event.getAction().getName().equals(AbstractAction.CONSUME))
			actionConsume(event);
	}

	private void actionConsume(ActionEvent event)
	{	if(gesture.equals(GestureName.STANDING))
		{	gesture = GestureName.BURNING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

/*
 * *****************************************************************
 * CONTROL EVENTS
 * *****************************************************************
 */
	@Override
	public void processEvent(ControlEvent event)
	{	
	}

/*
 * *****************************************************************
 * ENGINE EVENTS
 * *****************************************************************
 */
	@Override
	public void processEvent(EngineEvent event)
	{	if(event.getName().equals(EngineEvent.ANIME_OVER))
			engAnimeOver(event);
		else if(event.getName().equals(EngineEvent.DELAY_OVER))
			engDelayOver(event);
	}		

	private void engAnimeOver(EngineEvent event)
	{	if(gesture.equals(GestureName.BURNING))
		{	// spawn or not ?
			StateAbility ablt = sprite.getAbility(StateAbility.BLOCK_SPAWN);
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
			Item hs = (Item)sprite.getHiddenSprite();
			Tile tile = sprite.getTile(); 
			if(hs!=null)
			{	SpecificAction action = new SpecificAction(AbstractAction.APPEAR,hs,tile.getFloor(),Direction.NONE);
				AbstractAbility ab = hs.computeAbility(action);
				if(ab.isActive())
				{	hs.initGesture();
					tile.addSprite(hs);
					sprite.setHiddenSprite(null);
				}
			}
		}
		else if(gesture.equals(GestureName.SPAWNING))
		{	gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

	private void engDelayOver(EngineEvent event)
	{	if(gesture.equals(GestureName.HIDING))
		{	SpecificAction specificAction = new SpecificAction(AbstractAction.APPEAR,sprite,sprite.getTile().getFloor(),Direction.NONE);
			AbstractAbility ability = sprite.computeAbility(specificAction);
			if(ability.isActive())
			{	StateAbility ablt = sprite.getAbility(StateAbility.BLOCK_SPAWN);
				sprite.decrementUse(ablt,1);
				gesture = GestureName.SPAWNING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
			else
			{	StateAbility ablt = sprite.getAbility(StateAbility.BLOCK_SPAWN);
				sprite.addDelay(DelayManager.DL_SPAWN, ablt.getStrength());	
			}
		}
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
