package org.totalboumboum.engine.content.sprite.block;

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
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import org.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import org.totalboumboum.engine.content.feature.action.crush.SpecificCrush;
import org.totalboumboum.engine.content.feature.action.land.SpecificLand;
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
		else if(event.getAction() instanceof SpecificCrush)
			actionCrush(event);
	}

	private void actionConsume(ActionEvent event)
	{	if(gesture.equals(GestureName.STANDING))
		{	gesture = GestureName.BURNING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

	private void actionCrush(ActionEvent event)
	{	// crushed by a block: just disappear //NOTE could also have a specific animation
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
		else if(event.getName().equals(EngineEvent.COLLIDING_ON))
			engCollidingOn(event);
		else if(event.getName().equals(EngineEvent.DELAY_OVER))
			engDelayOver(event);
		else if(event.getName().equals(EngineEvent.TRAJECTORY_OVER))
			engTrajectoryOver(event);
		else if(event.getName().equals(EngineEvent.ROUND_ENTER))
			engEnter(event);
		else if(event.getName().equals(EngineEvent.ROUND_START))
			engStart(event);
		else if(event.getName().equals(EngineEvent.START_FALL))
			engStartFall(event);
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

	private void engCollidingOn(EngineEvent event)
	{	// bouncing : bouncing on the obstacle
		if(gesture.equals(GestureName.BOUNCING))
		{	spriteDirection = spriteDirection.getOpposite();
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,false);
		}
		// sliding : the block stops in the center of the tile
		else if(gesture.equals(GestureName.SLIDING) || gesture.equals(GestureName.SLIDING_FAILING))
		{	gesture = GestureName.STANDING;
			spriteDirection = Direction.NONE;
			sprite.center();
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
	
	private void engTrajectoryOver(EngineEvent event)
	{	// the sprite is currently bouncing
		if(gesture.equals(GestureName.BOUNCING))
		{	SpecificAction specificAction = new SpecificLand(sprite);
			ActionAbility ability = sprite.modulateAction(specificAction);
			// the sprite is allowed to land
			if(ability.isActive())
				gesture = GestureName.LANDING;
			// the sprite is not allowed to land
			else
				gesture = GestureName.BOUNCING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
		// the sprite is falling (sudden death)
		else if(gesture.equals(GestureName.FALLING))
		{	SpecificAction action = new SpecificLand(sprite);
			ActionAbility a = sprite.modulateAction(action);
//System.out.println("["+sprite.getTile().getRow()+","+sprite.getTile().getCol()+"] FALLING finished, landing authorization="+a.isActive());		
			// the sprite is allowed to land
			if(a.isActive())
			{	gesture = GestureName.LANDING;
				spriteDirection = Direction.DOWN;
			}
			else
			{	gesture = GestureName.BOUNCING;
				// bounces in a random direction
				spriteDirection = Direction.getRandomPrimaryDirection();
				//spriteDirection = Direction.LEFT;
			}
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
		// the sprite is landing
		else if(gesture.equals(GestureName.LANDING))
		{	//spriteDirection = Direction.NONE;
			gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			Tile tile = sprite.getTile();
			Block block = (Block) sprite;
			block.crushTile(tile);
		}
		// the sprite has been punched
		else if(gesture.equals(GestureName.PUNCHED))
		{	SpecificAction action = new SpecificLand(sprite);
			ActionAbility a = sprite.modulateAction(action);
			// the sprite is allowed to land
			if(a.isActive())
				gesture = GestureName.LANDING;
			else
				gesture = GestureName.BOUNCING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);			
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
//System.out.println("["+sprite.getTile().getRow()+","+sprite.getTile().getCol()+"] Starting FALLING");		
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
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
