package fr.free.totalboumboum.engine.content.sprite.fire;

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
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.action.detonate.SpecificDetonate;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class FireEventManager extends EventManager
{
	public FireEventManager(Fire sprite)
	{	super(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ActionEvent event)
	{	if(event.getAction() instanceof SpecificDetonate)
			SpecificDetonate(event);
	}

	private void SpecificDetonate(ActionEvent event)
	{	if(gesture.equals(GestureName.NONE)) 
		{	spriteDirection = event.getAction().getDirection();
			appear();
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
		else if(event.getName().equals(EngineEvent.TILE_LOW_ENTER))
			tileEnter(event);
		else if(event.getName().equals(EngineEvent.TOUCH_GROUND))
			tileEnter(event);
		else if(event.getName().equals(EngineEvent.ROUND_ENTER))
			engEnter(event);
		else if(event.getName().equals(EngineEvent.ROUND_START))
			engStart(event);
	}	

	private void engAnimeOver(EngineEvent event)
	{	if(gesture.equals(GestureName.APPEARING))
		{	gesture = GestureName.BURNING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
		else if(gesture.equals(GestureName.BURNING))
		{	endSprite();
		}
		else if(gesture.equals(GestureName.ENTERING))
		{	gesture = GestureName.PREPARED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

	private void tileEnter(EngineEvent event)
	{	if(gesture.equals(GestureName.BURNING) || gesture.equals(GestureName.APPEARING))
		{	Fire fire = (Fire)sprite;
			// fire enters a new tile
			if(event.getSource()==sprite)
			{	Tile tile = sprite.getTile();
				fire.consumeTile(tile);
			}
			// another sprite enters the fire's tile
			else
			{	Sprite s = event.getSource();
				fire.consumeSprite(s);
			}
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
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
